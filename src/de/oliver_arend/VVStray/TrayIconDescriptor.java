package de.oliver_arend.VVStray;

public class TrayIconDescriptor {

	private final String tooltip;
	private final String text;
	private final ModesOfTransport icon;
	private final boolean delayed;

	public TrayIconDescriptor(String tooltip, String text, ModesOfTransport icon, boolean delayed) {
		super();
		this.tooltip = tooltip;
		this.text = text;
		this.icon = icon;
		this.delayed = delayed;
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
	
}
