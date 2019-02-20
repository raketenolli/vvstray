package de.oliver_arend.VVStray;

import de.oliver_arend.VVStray.ModesOfTransport;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

public class Departure {
	private LocalTime departureTime;
	private String departureTimeString;
	private String arrivalTimeString;
	private String lineNumber;
	private ModesOfTransport modeOfTransport;
	private boolean delayed;
	private int transferNumber;
	private ArrayList<String> transfers;
	private ArrayList<String> alerts;
	
	public Departure(String departureTimeString, String arrivalTimeString, String lineNumber, boolean delayed, int transferNumber, ArrayList<String> transfers, ArrayList<String> alerts) {
		if(departureTimeString.length() > 5) {
			this.departureTime = LocalDateTime.parse(departureTimeString).toLocalTime();
		} else {
			this.departureTime = LocalTime.parse(departureTimeString);
		}
		this.departureTimeString = departureTimeString;
		this.arrivalTimeString = arrivalTimeString;
		this.lineNumber = lineNumber;
		char firstCharOfLineNumber = lineNumber.charAt(0);
		if (firstCharOfLineNumber == 'S' ) { this.modeOfTransport = ModesOfTransport.SBAHN; }
		else if (firstCharOfLineNumber == 'U' ) { this.modeOfTransport = ModesOfTransport.UBAHN; }
		else { this.modeOfTransport = ModesOfTransport.BUS; }
		this.transferNumber = transferNumber;
		this.transfers = transfers;
		this.delayed = delayed;
		this.alerts = alerts;
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
	
	public String getLineNumber() {
		return this.lineNumber;
	}
	
	public boolean isDelayed() {
		return this.delayed;
	}
	
	public int getTransferNumber() {
		return this.transferNumber;
	}

	public ArrayList<String> getTransfers() {
		return this.transfers;
	}
	
	public ArrayList<String> getAlerts() {
		return this.alerts;
	}

	public boolean hasAlerts() {
		return this.alerts.size() > 0;
	}
}
