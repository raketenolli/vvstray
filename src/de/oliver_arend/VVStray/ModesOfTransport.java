package de.oliver_arend.VVStray;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D.Float;
import java.awt.Rectangle;
import java.awt.Polygon;

enum ModesOfTransport {
	
	SBAHN(
			new Color(35, 103, 33), 
			new Float(0, 0, 16, 16), 
			new Float(0, 0, 15, 15)
	),
	UBAHN(
			new Color(17, 81, 142), 
			new Rectangle(0, 0, 16, 16), 
			new Rectangle(0, 0, 15, 15)
	),
	BUS(
			new Color(164, 26, 42),
			new Polygon(new int[] {0, 3, 13, 16, 13, 3}, new int[] {8, 0, 0, 8, 16, 16}, 6),
			new Polygon(new int[] {0, 0, 3, 12, 15, 15, 12, 3}, new int[] {8, 7, 0, 0, 7, 8, 15, 15}, 8)
	),
	WARNING(
			new Color(234, 189, 0), 
			new Polygon(new int[] {0, 16, 8}, new int[] {16, 16, 0}, 3), 
			new Polygon(new int[] {0, 15, 8, 7}, new int[] {15, 15, 0, 0}, 4)
	);

	private final Color color;
	private final Shape colorShape;
	private final Shape win10Shape;
	
	private ModesOfTransport(final Color color, final Shape colorShape, final Shape win10Shape) {
		this.color = color;
		this.colorShape = colorShape;
		this.win10Shape = win10Shape;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Shape getShape(IconStyle iconStyle) {
		if(iconStyle == IconStyle.COLOR) {
			return getColorShape();
		} else {
			return getWin10Shape();
		}
	}
	
	public Shape getColorShape() {
		return colorShape;
	}
	
	public Shape getWin10Shape() {
		return win10Shape;
	}
}
