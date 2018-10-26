package de.oliver_arend.VVStray;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.IOException;
//import javax.imageio.ImageIO;
import de.oliver_arend.VVStray.ModesOfTransport;

public class TextToGraphics {
    private BufferedImage img;

    public TextToGraphics(String text, ModesOfTransport vehicle, boolean delayed) {
        
        int[] hex_x = {0, 3, 13, 16, 13, 3};
        int[] hex_y = {8, 0, 0, 8, 16, 16};

        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Segoe UI", Font.BOLD, 10);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
//        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

		g2d.setColor(vehicle.getColor());
        switch(vehicle) {
        	case SBAHN:
    			g2d.fillOval(0, 0, 16, 16);
    			break;
        	case UBAHN:
        		g2d.fillRect(0, 0, 16, 16);
        		break;
        	case BUS:
        		g2d.fillPolygon(hex_x, hex_y, 6);
        		break;
        }
        
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        if(delayed) {
//        	g2d.setColor(new Color(255, 102, 0));
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