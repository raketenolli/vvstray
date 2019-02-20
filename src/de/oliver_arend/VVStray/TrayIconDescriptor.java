package de.oliver_arend.VVStray;

import java.util.ArrayList;

public class TrayIconDescriptor {

	private final String tooltip;
	private final ArrayList<String> transfers;
	private final ArrayList<String> alerts;
	private final String text;
	private final ModesOfTransport icon;
	private final boolean delayed;
	private final int transferNumber;
	private final boolean hasAlerts;

	public TrayIconDescriptor(String tooltip, ArrayList<String> transfers, ArrayList<String> alerts, String text, ModesOfTransport icon, boolean delayed, int transferNumber, boolean hasAlerts) {
		super();
		this.tooltip = tooltip;
		this.transfers = transfers;
		this.alerts = alerts;
		this.text = text;
		this.icon = icon;
		this.delayed = delayed;
		this.transferNumber = transferNumber;
		this.hasAlerts = hasAlerts;
	}

	public String getTooltip() {
		return tooltip;
	}

	public ArrayList<String> getTransfers() {
		return transfers;
	}

	public ArrayList<String> getAlerts() {
		return alerts;
	}

	public String getText() {
		return text;
	}

	public ModesOfTransport getIcon() {
		return icon;
	}

	public boolean isDelayed() {
		return delayed;
	}
	
	public int getTransferNumber() {
		return transferNumber;
	}
	
	public boolean hasAlerts() {
		return hasAlerts;
	}
}
