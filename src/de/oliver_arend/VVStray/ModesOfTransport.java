package de.oliver_arend.VVStray;

import java.awt.Color;

enum ModesOfTransport {
	SBAHN(35, 103, 33, "circle"),
	UBAHN(17, 81, 142, "square"),
	BUS(164, 26, 42, "hexagon");

	private final Color color;
	private final String shapeName;
	
	private ModesOfTransport(final int R, final int G, final int B, final String shapeName) {
		this.color = new Color(R, G, B);
		this.shapeName = shapeName;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getShapeName() {
		return shapeName;
	}
}
