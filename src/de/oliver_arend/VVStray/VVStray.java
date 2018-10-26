package de.oliver_arend.VVStray;

import java.io.*;
import java.net.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class VVStray {

	private SystemTrayIcon trayIcon;
	private String params;
	private String resultBody;
	private Departure currentDeparture;
	private int delay;
	private boolean useSBahn;
	private boolean useUBahn;
	private boolean useBus;
	
	public VVStray() {
		this.delay = 8;
		this.useSBahn = true;
		this.useUBahn = true;
		this.useBus = true;
        trayIcon = new SystemTrayIcon(this);
    }

	private static String getTimeWithDelayAsQuerystring(int delay) {
		LocalTime timeAfterDelay = LocalTime.now().plusMinutes(delay);
		DateTimeFormatter querystringTime = DateTimeFormatter.ofPattern("HH'%3A'mm"); 
		return timeAfterDelay.format(querystringTime);
	}
	
    private String createParamString(String origin, String destination) {
    	//somehow incorporate
    	// - desired modes of transport (S, U, Bus)
    	String paramBase = "type_origin=any&type_destination=any&deselectedTariffzones=51&deselectedTariffzones=50&deselectedTariffzones=49&deselectedTariffzones=48&deselectedTariffzones=47&deselectedTariffzones=46&deselectedTariffzones=45&deselectedTariffzones=44&deselectedTariffzones=43&deselectedTariffzones=42&deselectedTariffzones=41&deselectedTariffzones=40&deselectedTariffzones=39&deselectedTariffzones=38&deselectedTariffzones=37&deselectedTariffzones=36&deselectedTariffzones=35&deselectedTariffzones=34&deselectedTariffzones=33&deselectedTariffzones=32&deselectedTariffzones=31&deselectedTariffzones=30&deselectedTariffzones=29&deselectedTariffzones=28&deselectedTariffzones=27&deselectedTariffzones=26&deselectedTariffzones=25&deselectedTariffzones=24&deselectedTariffzones=23&deselectedTariffzones=22&deselectedTariffzones=21&deselectedTariffzones=20&deselectedTariffzones=19&deselectedTariffzones=18&deselectedTariffzones=17&deselectedTariffzones=16&deselectedTariffzones=15&deselectedTariffzones=14&deselectedTariffzones=13&deselectedTariffzones=12&deselectedTariffzones=11&deselectedTariffzones=10&deselectedTariffzones=9&deselectedTariffzones=8&deselectedTariffzones=7&deselectedTariffzones=6&deselectedTariffzones=5&deselectedTariffzones=4&deselectedTariffzones=3&deselectedTariffzones=2&deselectedTariffzones=1&deselectedTariffzones=0&calcOneDirection=1";    			
    	String param = paramBase + "&itdTime=" + getTimeWithDelayAsQuerystring(delay);
    	param += "&nameInfo_destination=" + destination;
//    	param += "&name_destination=" + destinationName; z.B. Leinfelden+%2F+Leinfelden
//    	param += "&nameInfo_origin=" + origin; z.B. 5006002 für Stuttgart-Vaihingen
    	param += "&name_origin=" + origin;
    	param += "&includedMeans=checkbox";
    	param += "&inclMOT_11=on"; // pedestrians
    	if(useSBahn) { param += "&inclMOT_1=on"; }
    	if(useUBahn) { param += "&inclMOT_3=on"; }
    	if(useBus) {
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
		if (departureTimeCell.className() == "notice") { delayed = true; }

		Element arrivalTimeCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(4)");
		String arrivalTime = arrivalTimeCell.text();

		Element lineNumberCell = doc.selectFirst("table.ausgabe tr#Trip1 td:nth-child(5)");
		String lineNumber = lineNumberCell.text();

		return new Departure(departureTime, arrivalTime, lineNumber, delayed);
	}
	
	public void setDelay(int delay) {
		this.delay = delay;
	}
	
	public void setModeOfTransportPreferences(boolean useSBahn, boolean useUBahn, boolean useBus) {
		this.useSBahn = useSBahn;
		this.useUBahn = useUBahn;
		this.useBus = useBus;
	}
	
	public boolean useSBahn() {
		return this.useSBahn;
	}
	
	public boolean useUBahn() {
		return this.useUBahn;
	}
	
	public boolean useBus() {
		return this.useBus;
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
    	params = createParamString("Stuttgart+%2F+Vaihingen", "5000175");
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
        VVStray myTray = new VVStray();
        myTray.run();
	}

}
