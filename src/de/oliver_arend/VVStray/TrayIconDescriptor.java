package de.oliver_arend.VVStray;

public class TrayIconDescriptor {

	private final String tooltip;
	private final String text;
	private final ModesOfTransport icon;
	private final boolean delayed;
	private final boolean hasAlerts;

	public TrayIconDescriptor(String tooltip, String text, ModesOfTransport icon, boolean delayed, boolean hasAlerts) {
		super();
		this.tooltip = tooltip;
		this.text = text;
		this.icon = icon;
		this.delayed = delayed;
		this.hasAlerts = hasAlerts;
	}

	public String getTooltip() {
		return tooltip;
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
	
	public boolean hasAlerts() {
		return hasAlerts;
	}
}
