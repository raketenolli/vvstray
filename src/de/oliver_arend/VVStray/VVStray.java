package de.oliver_arend.VVStray;

import java.io.*;
import java.lang.reflect.Type;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
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
	
    private String createParamString(String origin, String originId, String destination, String destinationId) {
    	String paramBase = "useRealtime=1&type_origin=any&type_destination=any&deselectedTariffzones=51&deselectedTariffzones=50&deselectedTariffzones=49&deselectedTariffzones=48&deselectedTariffzones=47&deselectedTariffzones=46&deselectedTariffzones=45&deselectedTariffzones=44&deselectedTariffzones=43&deselectedTariffzones=42&deselectedTariffzones=41&deselectedTariffzones=40&deselectedTariffzones=39&deselectedTariffzones=38&deselectedTariffzones=37&deselectedTariffzones=36&deselectedTariffzones=35&deselectedTariffzones=34&deselectedTariffzones=33&deselectedTariffzones=32&deselectedTariffzones=31&deselectedTariffzones=30&deselectedTariffzones=29&deselectedTariffzones=28&deselectedTariffzones=27&deselectedTariffzones=26&deselectedTariffzones=25&deselectedTariffzones=24&deselectedTariffzones=23&deselectedTariffzones=22&deselectedTariffzones=21&deselectedTariffzones=20&deselectedTariffzones=19&deselectedTariffzones=18&deselectedTariffzones=17&deselectedTariffzones=16&deselectedTariffzones=15&deselectedTariffzones=14&deselectedTariffzones=13&deselectedTariffzones=12&deselectedTariffzones=11&deselectedTariffzones=10&deselectedTariffzones=9&deselectedTariffzones=8&deselectedTariffzones=7&deselectedTariffzones=6&deselectedTariffzones=5&deselectedTariffzones=4&deselectedTariffzones=3&deselectedTariffzones=2&deselectedTariffzones=1&deselectedTariffzones=0&calcOneDirection=1";    			
    	String param = paramBase + "&itdTime=" + getTimePlusWalkingTimeAsQuerystring();
    	param += "&nameInfo_destination=" + destinationId; // z. B. 5000175 für Leinfelden
    	param += "&name_destination=" + destination; // z. B. Leinfelden+%2F+Leinfelden
    	param += "&nameInfo_origin=" + originId; // z.B. 5006002 für Stuttgart-Vaihingen
    	param += "&name_origin=" + origin; // z. B. Stuttgart+%2F+Vaihingen
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
    	StringBuilder result = new StringBuilder();
    	URL url = new URL("http://efastatic.vvs.de/vvs/XSLT_TRIP_REQUEST2?" + params);
    	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    	conn.setRequestMethod("GET");
    	BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    	String line;
    	while ((line = rd.readLine()) != null) {
    		result.append(line);
    	}
    	rd.close();
    	return result.toString();
    }
	
	private static Departure returnFirstDeparture(String resultBody) {
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
		String tooltipString = "Stuttgart / Vaihingen" + " – " + "Leinfelden / Leinfelden" + ": ";
		tooltipString += currentDeparture.getFirstLineNumber() + " at ";
		tooltipString += currentDeparture.getDepartureTimeString() + "–";
		tooltipString += currentDeparture.getArrivalTimeString() + ", ";
		tooltipString += currentDeparture.getNumberOfTransfers() + (currentDeparture.getNumberOfTransfers() == 1 ? " transfer" : " transfers");
		return tooltipString;
	}
	
	public void update() {
    	params = createParamString("Stuttgart+%2F+Vaihingen", "5006002", "Leinfelden+%2F+Leinfelden", "5000175");
    	try {
    		resultBody = postRequest(params);
    	} catch(MalformedURLException e) {
    	} catch(IOException e) {
    	}
		currentDeparture = returnFirstDeparture(resultBody);
		long delta = LocalTime.now().until(currentDeparture.getDepartureTime(), ChronoUnit.MINUTES);
        trayIcon.update(
        		createTooltipString(currentDeparture), 
        		(delta > 99 ? 99 : delta)+"", 
        		currentDeparture.getModeOfTransport(), 
        		currentDeparture.isDelayed()
		);
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
