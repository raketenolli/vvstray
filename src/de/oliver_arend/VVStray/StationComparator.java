package de.oliver_arend.VVStray;

import java.util.Comparator;

public class StationComparator implements Comparator<Station>{
	public int compare(Station stationA, Station stationB) {
		String stationADistrictAndName = stationA.getDistrictAndName();
		String stationBDistrictAndName = stationB.getDistrictAndName();
		
		return stationADistrictAndName.compareTo(stationBDistrictAndName);
	}

}
