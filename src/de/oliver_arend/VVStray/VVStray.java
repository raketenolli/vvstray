package de.oliver_arend.VVStray;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import javax.swing.UIManager;

import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class VVStray {

	private SystemTrayIcon trayIcon;
	private String params;
	private String resultBody;
	private Departure currentDeparture;
	private ArrayList<Station> allStations;
	
	public VVStray() {
		ParseStationJson();
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {}
        trayIcon = new SystemTrayIcon(this);

        Runnable updateRunnable = new Runnable() {
			public void run() {
				update();
			}
		};
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleAtFixedRate(updateRunnable, 0, 1, TimeUnit.MINUTES);
    }

	private void ParseStationJson() {
		try {
			String stationJson = Utils.readFile("vvs_stops.json", StandardCharsets.UTF_8);
			Gson g = new Gson();
			Type stationListType = new TypeToken<ArrayList<Station>>(){}.getType();
			this.allStations = g.fromJson(stationJson, stationListType);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
	public ArrayList<Station> getStationsArrayList() {
		return this.allStations;
	}
	
	public Station[] getStationsArray(String currentEntry) {
		Stream<Station> beginsWith = this.allStations.stream()
				.filter(s -> s.getName().startsWith(currentEntry))
				.sorted();
		Stream<Station> contains = this.allStations.stream()
				.filter(s -> s.getName().substring(1).contains(currentEntry))
				.sorted();
		return Stream.concat(beginsWith, contains)
				.toArray(Station[]::new);
	}
	
	private static String getTimePlusWalkingTimeAsQuerystring() {
		int walkingTime = UserSettingsProvider.getUserSettings().getWalkingTimeToStation();
		LocalTime timeAfterWalking = LocalTime.now().plusMinutes(walkingTime);
		DateTimeFormatter querystringTime = DateTimeFormatter.ofPattern("HH'%3A'mm"); 
		return timeAfterWalking.format(querystringTime);
	}
	
	private static String getDateAsQuerystring() {
		LocalDate date = LocalDate.now();
		DateTimeFormatter querystringDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		return querystringDate.format(date);
	}
	
    private String createParamString() throws UnsupportedEncodingException {
    	String origin = UserSettingsProvider.getUserSettings().getOriginStation().getDistrictAndName(); 
    	String originId = Long.toString(UserSettingsProvider.getUserSettings().getOriginStation().getId());
    	String destination = UserSettingsProvider.getUserSettings().getDestinationStation().getDistrictAndName(); 
    	String destinationId = Long.toString(UserSettingsProvider.getUserSettings().getDestinationStation().getId());
    	String param = "useRealtime=1";
    	param += "&type_origin=any";
    	param += "&type_destination=any";
    	param += "&selectedTariffzones=53&selectedTariffzones=52&selectedTariffzones=51&selectedTariffzones=50&selectedTariffzones=49&selectedTariffzones=48&selectedTariffzones=47&selectedTariffzones=46&selectedTariffzones=45&selectedTariffzones=44&selectedTariffzones=43&selectedTariffzones=42&selectedTariffzones=41&selectedTariffzones=40&selectedTariffzones=39&selectedTariffzones=38&selectedTariffzones=37&selectedTariffzones=36&selectedTariffzones=35&selectedTariffzones=34&selectedTariffzones=33&selectedTariffzones=32&selectedTariffzones=31&selectedTariffzones=30&selectedTariffzones=29&selectedTariffzones=28&selectedTariffzones=27&selectedTariffzones=26&selectedTariffzones=25&selectedTariffzones=24&selectedTariffzones=23&selectedTariffzones=22&selectedTariffzones=21&selectedTariffzones=20&selectedTariffzones=19&selectedTariffzones=18&selectedTariffzones=17&selectedTariffzones=16&selectedTariffzones=15&selectedTariffzones=14&selectedTariffzones=13&selectedTariffzones=12&selectedTariffzones=11&selectedTariffzones=10&selectedTariffzones=9&selectedTariffzones=8&selectedTariffzones=7&selectedTariffzones=6&selectedTariffzones=5&selectedTariffzones=4&selectedTariffzones=3&selectedTariffzones=2&selectedTariffzones=1&selectedTariffzones=0";
    	param += "&calcOneDirection=1";    			
    	param += "&changeSpeed=normal";
    	param += "&dwellTimeMinutes=0";
    	param += "&efaResult=submit";
    	param += "&itOptionsActive=1";
    	param += "&routeType=LEASTTIME";
    	param += "&trlTArrMOTvalue100=15";
    	param += "&trlTDepMOTvalue100=15";
    	param += "&useSuburb=1";
    	param += "&itdTripDateTimeDepArr=dep";
    	param += "&itdDateDayMonthYear=" + getDateAsQuerystring();
    	param += "&itdTime=" + getTimePlusWalkingTimeAsQuerystring();
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
    	return param;
    }
	
	private static String postRequest(String params) throws MalformedURLException, IOException {
    	byte[] requestBody = params.getBytes(StandardCharsets.UTF_8);
    	int contentLength = requestBody.length;

    	URL url = new URL("http://efastatic.vvs.de/vvs/XSLT_TRIP_REQUEST2");
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    	conn.setRequestMethod("POST");
    	conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
    	conn.setRequestProperty("Content-Length", Integer.toString(contentLength));
    	conn.setRequestProperty("charset", "utf-8");
    	conn.setDoOutput(true);
        conn.getOutputStream().write(requestBody);

    	StringBuilder result = new StringBuilder();
    	BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String line;
    	while ((line = rd.readLine()) != null) {
    		result.append(line);
    	}
    	rd.close();

    	return result.toString();
    }
	
	private static Departure returnFirstDeparture(String resultBody) throws NullPointerException {
		Document doc = Jsoup.parse(resultBody);

		Element departureTimeCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(3)");
		String departureTime = departureTimeCell.text();

		boolean delayed = false;
		if (departureTimeCell.className().equals("notice")) { delayed = true; }

		Element arrivalTimeCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(4)");
		String arrivalTime = arrivalTimeCell.text();

		Element lineNumberCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(5)");
		String lineNumber = lineNumberCell.text();

		return new Departure(departureTime, arrivalTime, lineNumber, delayed);
	}
	
	public static String createTooltipString(Departure currentDeparture) {
		String originStation = UserSettingsProvider.getUserSettings().getOriginStation().getDistrictAndName();
		String destinationStation = UserSettingsProvider.getUserSettings().getDestinationStation().getDistrictAndName();
		String tooltipString = originStation + " – " + destinationStation + ": ";
		tooltipString += currentDeparture.getFirstLineNumber() + " at ";
		tooltipString += currentDeparture.getDepartureTimeString() + "–";
		tooltipString += currentDeparture.getArrivalTimeString() + ", ";
		tooltipString += currentDeparture.getNumberOfTransfers() + (currentDeparture.getNumberOfTransfers() == 1 ? " transfer" : " transfers");
		return tooltipString;
	}
	
	public void update() {
    	try {
        	params = createParamString();
    		resultBody = postRequest(params);
    		currentDeparture = returnFirstDeparture(resultBody);
    		long delta = LocalTime.now().until(currentDeparture.getDepartureTime(), ChronoUnit.MINUTES);
            trayIcon.update(
            		createTooltipString(currentDeparture), 
            		(delta > 99 ? 99 : delta)+"", 
            		currentDeparture.getModeOfTransport(), 
            		currentDeparture.isDelayed()
    		);
    	} catch(MalformedURLException e) {
    		trayIcon.update("MalformedURL Exception when trying to obtain new connection information", "!", ModesOfTransport.WARNING, false);
    	} catch(IOException e) {
    		trayIcon.update("IOException when trying to obtain new connection information", "!", ModesOfTransport.WARNING, false);
    	} catch(NullPointerException e) {
    		trayIcon.update("NullPointerException when trying to parse response body to departure", "!", ModesOfTransport.WARNING, false);
    	}
	}
	
	public void waitOneMinute() {
        try { Thread.sleep(60 * 1000); } 
        catch(InterruptedException e) { e.printStackTrace(); }
	}
	
    public void run() {
        while(true) {
        	update();
        	waitOneMinute();
        }
    }
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VVStray();
            }
        });
	}

}
