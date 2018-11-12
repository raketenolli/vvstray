package de.oliver_arend.VVStray;

public class Station implements Comparable<Station>{
	public long id;
	public String name;
	public String nameWithPlace;
	public String town;
	public String townDistrict;
	public String districtAndName;
	
	public Station(long id, String name, String nameWithPlace, String town, String townDistrict, String districtAndName) {
		this.id = id;
		this.name = name;
		this.nameWithPlace = nameWithPlace;
		this.town = town;
		this.townDistrict = townDistrict;
		this.districtAndName = districtAndName;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNameWithPlace() {
		return nameWithPlace;
	}
	public void setNameWithPlace(String nameWithPlace) {
		this.nameWithPlace = nameWithPlace;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getTownDistrict() {
		return townDistrict;
	}
	public void setTownDistrict(String townDistrict) {
		this.townDistrict = townDistrict;
	}
	public String getDistrictAndName() {
		return districtAndName;
	}
	public void setDistrictAndName(String districtAndName) {
		this.districtAndName = districtAndName;
	}
	public String toString() {
		return this.districtAndName;
	}
	public int compareTo(Station s) {
		return this.name.compareTo(s.name);
	}

}
