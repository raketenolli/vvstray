package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.oliver_arend.VVStray.ModesOfTransport;

public class SystemTrayIcon {
    private JTrayIcon trayIcon;
    private Image image;
    private ChangeWalkingTimeDialog changeWalkingTimeDialog;
    private ChangeTransportPreferencesDialog changeTransportPreferencesDialog;
    private ChangeOriginStationDialog changeOriginStationDialog;
    private ChangeDestinationStationDialog changeDestinationStationDialog;
    private ChangeIconStyleDialog changeIconStyleDialog;
    
    public SystemTrayIcon(VVStray parent) {
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        image = Toolkit.getDefaultToolkit().getImage("default.png");

        JPopupMenu trayPopupMenu = new JPopupMenu();
        
        changeWalkingTimeDialog = new ChangeWalkingTimeDialog(parent);
        changeTransportPreferencesDialog = new ChangeTransportPreferencesDialog(parent);
        changeOriginStationDialog = new ChangeOriginStationDialog(parent);
        changeDestinationStationDialog = new ChangeDestinationStationDialog(parent);
        changeIconStyleDialog = new ChangeIconStyleDialog(parent);
        
        JMenuItem changeIconStyle = new JMenuItem("Change icon style");
        changeIconStyle.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeIconStyleDialog.open();
        	}
        });
        trayPopupMenu.add(changeIconStyle);
        
        JMenuItem changeOriginStation = new JMenuItem("Change departure station");
        changeOriginStation.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeOriginStationDialog.open();
        	}
        });
        trayPopupMenu.add(changeOriginStation);
        
        JMenuItem changeDestinationStation = new JMenuItem("Change destination station");
        changeDestinationStation.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeDestinationStationDialog.open();
        	}
        });
        trayPopupMenu.add(changeDestinationStation);
        
        JMenuItem changeTransportPreferences = new JMenuItem("Change mode of transport preferences");
        changeTransportPreferences.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeTransportPreferencesDialog.open();
        	}
        });
        trayPopupMenu.add(changeTransportPreferences);
        
        JMenuItem changeWalkingTime = new JMenuItem("Change walking time to stop");
        changeWalkingTime.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeWalkingTimeDialog.open();
        	}
        });
        trayPopupMenu.add(changeWalkingTime);
        
        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);
    
        trayIcon = new JTrayIcon(image, "VVStray");
        trayIcon.setMenu(trayPopupMenu);
        trayIcon.setImageAutoSize(true);

        try {
            systemTray.add(trayIcon);
        } catch(AWTException awtException) {
            awtException.printStackTrace();
        }
    }
    
    public void update(String tooltip, String delta, ModesOfTransport vehicle, boolean delayed) {
        TextToGraphics textImage = new TextToGraphics(delta, vehicle, delayed);
        trayIcon.setImage(textImage.getImage());
        trayIcon.setToolTip(tooltip);
    }

}