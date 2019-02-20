package de.oliver_arend.VVStray;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import de.oliver_arend.VVStray.ModesOfTransport;

public class TextToGraphics {
    private BufferedImage img;

    private void setRenderingHints(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
    }

    private void drawAlertDecorator(Graphics2D g2d) {
        g2d.setColor(new Color(255, 0, 192));
        g2d.fill(new Polygon(new int[] {0, 7, 0}, new int[] {0, 0, 7}, 3));
    }
    
    public TextToGraphics(String text, ModesOfTransport vehicle, boolean delayed, boolean hasAlerts) {
    	IconStyle iconStyle = UserSettingsProvider.getUserSettings().getIconStyle();

        img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        
        Font font = new Font("Segoe UI", Font.BOLD, 10);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        g2d.dispose();

        img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();

		if(iconStyle == IconStyle.COLOR) {
			g2d.setColor(vehicle.getColor());
			setRenderingHints(g2d);
            g2d.fill(vehicle.getShape(iconStyle));
            if(hasAlerts) { drawAlertDecorator(g2d); }
		} else {
			g2d.setColor(Color.WHITE);
        	g2d.draw(vehicle.getShape(iconStyle));
	        if(hasAlerts) { drawAlertDecorator(g2d); }
			setRenderingHints(g2d);
		}

        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        if(delayed) {
        	g2d.setColor(new Color(255, 153, 85));
        } else {
        	g2d.setColor(Color.WHITE);
        }
        g2d.drawString(text, 8 - width/2, fm.getAscent()+1);
        g2d.dispose();

    }
    
    public BufferedImage getImage() {
        return img;
    }

}