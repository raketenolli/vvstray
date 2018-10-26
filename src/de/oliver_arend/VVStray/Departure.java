package de.oliver_arend.VVStray;

import de.oliver_arend.VVStray.ModesOfTransport;
import java.time.LocalTime;

public class Departure {
	private LocalTime departureTime;
	private String departureTimeString;
	private String arrivalTimeString;
	private String firstLineNumber;
	private int numberOfTransfers;
	private ModesOfTransport modeOfTransport;
	private boolean delayed;
	
	public Departure(String departureTimeString, String arrivalTimeString, String lineNumber, boolean delayed) {
		this.departureTime = LocalTime.parse(departureTimeString);
		this.departureTimeString = departureTimeString;
		this.arrivalTimeString = arrivalTimeString;
		this.firstLineNumber = lineNumber.split(",")[0];
		this.numberOfTransfers = lineNumber.split(",").length - 1;
		char firstCharOfLineNumber = lineNumber.charAt(0);
		if (firstCharOfLineNumber == 'S' ) { this.modeOfTransport = ModesOfTransport.SBAHN; }
		else if (firstCharOfLineNumber == 'U' ) { this.modeOfTransport = ModesOfTransport.UBAHN; }
		else { this.modeOfTransport = ModesOfTransport.BUS; }
		this.delayed = delayed;
	}
	
	public LocalTime getDepartureTime() {
		return this.departureTime;
	}
	
	public ModesOfTransport getModeOfTransport() {
		return this.modeOfTransport;
	}
	
	public String getDepartureTimeString() {
		return this.departureTimeString;
	}
	
	public String getArrivalTimeString() {
		return this.arrivalTimeString;
	}
	
	public String getFirstLineNumber() {
		return this.firstLineNumber;
	}
	
	public int getNumberOfTransfers() {
		return this.numberOfTransfers;
	}
	
	public boolean isDelayed() {
		return this.delayed;
	}
}
