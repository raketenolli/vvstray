package de.oliver_arend.VVStray;

public class UserSettings {
	
	private int walkingTimeToStation;
	private boolean useSBahn;
	private boolean useUBahn;
	private boolean useBus;
	private Station originStation;
	private Station destinationStation;
	private IconStyle iconStyle;
	
	public int getWalkingTimeToStation() {
		return walkingTimeToStation;
	}
	public void setWalkingTimeToStation(int walkingTimeToStation) {
		this.walkingTimeToStation = walkingTimeToStation;
	}
	public boolean isUseSBahn() {
		return useSBahn;
	}
	public void setUseSBahn(boolean useSBahn) {
		this.useSBahn = useSBahn;
	}
	public boolean isUseUBahn() {
		return useUBahn;
	}
	public void setUseUBahn(boolean useUBahn) {
		this.useUBahn = useUBahn;
	}
	public boolean isUseBus() {
		return useBus;
	}
	public void setUseBus(boolean useBus) {
		this.useBus = useBus;
	}
	public Station getOriginStation() {
		return originStation;
	}
	public void setOriginStation(Station originStation) {
		this.originStation = originStation;
	}
	public Station getDestinationStation() {
		return destinationStation;
	}
	public void setDestinationStation(Station destinationStation) {
		this.destinationStation = destinationStation;
	}
	public IconStyle getIconStyle() {
		return iconStyle;
	}
	public void setIconStyle(IconStyle iconStyle) {
		this.iconStyle = iconStyle;
	}
		
}
