package de.oliver_arend.VVStray;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class DepartureProvider {

	private String params;
	private String resultBody;
	private Departure currentDeparture;

	public DepartureProvider() {
		
	}
	
	private static LocalDateTime getNowPlusWalkingTime() {
		int walkingTime = UserSettingsProvider.getUserSettings().getWalkingTimeToStation();
		LocalDateTime nowPlusWalkingTime = LocalDateTime.now().plusMinutes(walkingTime);
		return nowPlusWalkingTime;
	}
	
	private static String getTimeAsQuerystring() {
		DateTimeFormatter querystringTime = DateTimeFormatter.ofPattern("HH'%3A'mm");
		return getNowPlusWalkingTime().format(querystringTime);
	}
	
	private static String getDateAsQuerystring() {
		DateTimeFormatter querystringDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return getNowPlusWalkingTime().format(querystringDate);
	}
	
    private void setParamString() throws UnsupportedEncodingException {
    	String origin = UserSettingsProvider.getUserSettings().getOriginStation().getDistrictAndName(); 
    	String originId = Long.toString(UserSettingsProvider.getUserSettings().getOriginStation().getId());
    	String destination = UserSettingsProvider.getUserSettings().getDestinationStation().getDistrictAndName(); 
    	String destinationId = Long.toString(UserSettingsProvider.getUserSettings().getDestinationStation().getId());
    	String param = "useRealtime=1";
    	param += "&type_origin=any";
    	param += "&type_destination=any";
    	param += "&selectedTariffzones=9&selectedTariffzones=8&selectedTariffzones=7&selectedTariffzones=6&selectedTariffzones=5&selectedTariffzones=4&selectedTariffzones=3&selectedTariffzones=2&selectedTariffzones=1&selectedTariffzones=0";
    	param += "&calcOneDirection=1";    			
    	param += "&changeSpeed=normal";
    	param += "&dwellTimeMinutes=0";
    	param += "&routeType=LEASTTIME";
    	param += "&trlTArrMOTvalue100=15";
    	param += "&trlTDepMOTvalue100=15";
    	param += "&useSuburb=1";
    	param += "&itdTripDateTimeDepArr=dep";
    	param += "&itdDateDayMonthYear=" + getDateAsQuerystring();
    	param += "&itdTime=" + getTimeAsQuerystring();
    	param += "&nameInfo_destination=" + destinationId; // z. B. 5000175 für Leinfelden
    	param += "&name_destination=" + URLEncoder.encode(destination, "UTF-8"); // z. B. Leinfelden+%2F+Leinfelden
    	param += "&nameInfo_origin=" + originId; // z.B. 5006002 für Stuttgart-Vaihingen
    	param += "&name_origin=" + URLEncoder.encode(origin, "UTF-8"); // z. B. Stuttgart+%2F+Vaihingen
    	param += "&includedMeans=checkbox";
    	param += "&inclMOT_11=on"; // pedestrians
    	if(UserSettingsProvider.getUserSettings().isUseSBahn()) { param += "&inclMOT_1=on"; }
    	if(UserSettingsProvider.getUserSettings().isUseUBahn()) { param += "&inclMOT_3=on"; }
    	if(UserSettingsProvider.getUserSettings().isUseBus()) {
    		param += "&inclMOT_5=on"; // bus
        	param += "&inclMOT_7=on"; // night bus 
    	}
    	this.params = param;
    }
	
	private void postRequest() throws MalformedURLException, IOException {
    	byte[] requestBody = this.params.getBytes(StandardCharsets.UTF_8);
    	int contentLength = requestBody.length;

    	URL url = new URL("http://efastatic.vvs.de/vvs/XSLT_TRIP_REQUEST2");
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    	conn.setRequestMethod("POST");
    	conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
    	conn.setRequestProperty("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
    	conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
    	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	conn.setRequestProperty("charset", "utf-8");
    	conn.setDoOutput(true);
        conn.getOutputStream().write(requestBody);

    	StringBuilder result = new StringBuilder();
		InputStream inputStream = conn.getInputStream();
		GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
		InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream, "UTF-8");
		BufferedReader rd = new BufferedReader(inputStreamReader);
		String line;
    	while ((line = rd.readLine()) != null) {
    		result.append(line);
    	}
    	rd.close();

    	this.resultBody = result.toString();
    }
	
	private void parseFirstDepartureFromResultBody() throws NullPointerException {
		Document doc = Jsoup.parse(this.resultBody);

		Element departureTimeCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(3)");
		String departureTime = departureTimeCell.text();

		boolean delayed = false;
		if (departureTimeCell.className().equals("notice")) { delayed = true; }

		Element arrivalTimeCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(4)");
		String arrivalTime = arrivalTimeCell.text();

		Element lineNumberCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(5)");
		String lineNumber = lineNumberCell.text();
		
		int transferNumber = 0;
		ArrayList<String> transfers = new ArrayList<String>();
		String[] transferStations = doc.select("div#detailTrip1 div.boxTripEntry tbody tr:nth-child(3) td:nth-child(4)").stream().map(Element::text).toArray(String[]::new);
		String[] transferDirections = doc.select("div#detailTrip1 div.boxTripEntry").not(":has(img[src='images/means/icon.fussweg.png'])").select("thead th:nth-child(5)").stream().map(Element::text).toArray(String[]::new);
		if(transferStations.length > 1) {
			transferNumber = transferStations.length - 1;
			for(int i = 0; i < transferStations.length-1; i++) {
				transfers.add("at " + transferStations[i] + " onto " + transferDirections[i+1]);
			}
		}

		ArrayList<String> alerts = new ArrayList<String>();
		Elements alertCells = doc.select("div#detailTrip1 td:has(a.stoerung-dialog-opener) + td");
		if(alertCells.size() > 0) {
			alerts = alertCells
						.stream()
						.map(Element::html)
						.map(text -> text.split("<br>"))
						.flatMap(Arrays::stream)
						.distinct()
						.collect(Collectors.toCollection(ArrayList::new));
		}

		currentDeparture = new Departure(departureTime, arrivalTime, lineNumber, delayed, transferNumber, transfers, alerts);
	}

	public String getTooltipString() {
		String originStation = UserSettingsProvider.getUserSettings().getOriginStation().getName();
		String destinationStation = UserSettingsProvider.getUserSettings().getDestinationStation().getName();
		String tooltipString = originStation + " – " + destinationStation + ": ";
		tooltipString += this.currentDeparture.getLineNumber() + " at ";
		tooltipString += this.currentDeparture.getDepartureTimeString() + "–";
		tooltipString += this.currentDeparture.getArrivalTimeString() + ", ";
		if(this.currentDeparture.getTransferNumber() == 0) {
			tooltipString += "no transfers";
		} else if (this.currentDeparture.getTransferNumber() == 1) {
			tooltipString += "1 transfer";
		} else {
			tooltipString += this.currentDeparture.getTransferNumber() + " transfers";
		}
		if(this.currentDeparture.hasAlerts()) {
			tooltipString += "\r\nTraffic alerts available";
		}
		return tooltipString;
	}
	
	public void update() throws UnsupportedEncodingException, MalformedURLException, IOException, NullPointerException {
    	setParamString();
		postRequest();
		parseFirstDepartureFromResultBody();
	}
	
	public TrayIconDescriptor getTrayIconDescriptor() throws UnsupportedEncodingException, MalformedURLException, IOException, NullPointerException {
		this.update();
		long delta = LocalTime.now().until(this.currentDeparture.getDepartureTime(), ChronoUnit.MINUTES);
		if(delta < 0) { delta += 1440; }
		return new TrayIconDescriptor(
				getTooltipString(), 
				this.currentDeparture.getTransfers(),
				this.currentDeparture.getAlerts(),
				(delta > 99 ? 99 : delta)+"", 
        		this.currentDeparture.getModeOfTransport(), 
				this.currentDeparture.isDelayed(),
				this.currentDeparture.getTransferNumber(),
				this.currentDeparture.hasAlerts()
		);
	}
	
}
