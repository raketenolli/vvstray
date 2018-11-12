package de.oliver_arend.VVStray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;

public class ParseStationJson {
	
	public static void main(String[] args) {
		try {
			String stationJson = Utils.readFile("C:\\dev\\eclipse-workspace\\VVStray\\src\\de\\oliver_arend\\VVStray\\vvs_stops.json", StandardCharsets.UTF_8);
			Gson g = new Gson();
			Type stationListType = new TypeToken<ArrayList<Station>>(){}.getType();
			ArrayList<Station> allStations = g.fromJson(stationJson, stationListType);
			System.out.println("Number of stations: " + allStations.size());
			System.out.println(allStations.stream()
					.filter(s -> s.getId() <= 5000010)
					.sorted()
					.map(Station::getDistrictAndName)
					.collect(Collectors.joining("\r\n")));
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}

}
