package de.oliver_arend.VVStray;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
    
    public TextToGraphics(String text, ModesOfTransport vehicle, boolean delayed) {
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
		} else {
			g2d.setColor(Color.WHITE);
		}
		
        if(iconStyle == IconStyle.COLOR) { setRenderingHints(g2d); }

        switch(vehicle) {
        	case SBAHN:
        		if(iconStyle == IconStyle.COLOR) { 
        			g2d.fillOval(0, 0, 16, 16); 
    			} else { 
    				g2d.drawOval(0, 0, 15, 15); 
				}
    			break;
        	case UBAHN:
        		if(iconStyle == IconStyle.COLOR) { 
        			g2d.fillRect(0, 0, 16, 16); 
    			} else { 
    				g2d.drawRect(0, 0, 15, 15); 
				}
        		break;
        	case BUS:
        		if(iconStyle == IconStyle.COLOR) {
        			int[] hexX = {0, 3, 13, 16, 13, 3};
        			int[] hexY = {8, 0, 0, 8, 16, 16};
        			g2d.fillPolygon(hexX, hexY, 6); 
    			}
        		else { 
        			int[] hexX = {0, 0, 3, 12, 15, 15, 12, 3};
        			int[] hexY = {8, 7, 0, 0, 7, 8, 15, 15};
        			g2d.drawPolygon(hexX, hexY, 8);
    			}
        		break;
        	case WARNING:
        		if(iconStyle == IconStyle.COLOR) {
        			int[] triX = {0, 16, 8};
        			int[] triY = {16, 16, 0};
        			g2d.fillPolygon(triX, triY, 3);
        		}
        		else {
        			int[] triX = {0, 15, 8, 7};
        			int[] triY = {15, 15, 0, 0};
        			g2d.drawPolygon(triX, triY, 4);
        		}
        		break;
        }
        
        if(iconStyle == IconStyle.WINDOWS10) { setRenderingHints(g2d); }

        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        if(delayed) {
//        	g2d.setColor(new Color(255, 102, 0)); // Win10 style
        	g2d.setColor(new Color(255, 153, 85)); // Color style
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