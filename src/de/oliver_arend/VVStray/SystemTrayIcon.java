package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;
import de.oliver_arend.VVStray.ModesOfTransport;

public class SystemTrayIcon {
    private TrayIcon trayIcon;
    private Image image;
    private ChangeWalkingTimeDialog changeWalkingTimeDialog;
    private ChangeTransportPreferencesDialog changeTransportPreferencesDialog;
    private ChangeOriginStationDialog changeOriginStationDialog;
    private ChangeIconStyleDialog changeIconStyleDialog;
    
    public SystemTrayIcon(VVStray parent) {
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        image = Toolkit.getDefaultToolkit().getImage("default.png");

        PopupMenu trayPopupMenu = new PopupMenu();
        
        changeWalkingTimeDialog = new ChangeWalkingTimeDialog(parent);
        changeTransportPreferencesDialog = new ChangeTransportPreferencesDialog(parent);
        changeOriginStationDialog = new ChangeOriginStationDialog(parent);
        changeIconStyleDialog = new ChangeIconStyleDialog(parent);
        
        MenuItem changeOriginStation = new MenuItem("Change departure station");
        changeOriginStation.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeOriginStationDialog.open();
        	}
        });
        trayPopupMenu.add(changeOriginStation);
        
        MenuItem changeTransportPreferences = new MenuItem("Change mode of transport preferences");
        changeTransportPreferences.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeTransportPreferencesDialog.open();
        	}
        });
        trayPopupMenu.add(changeTransportPreferences);
        
        MenuItem changeWalkingTime = new MenuItem("Change walking time to stop");
        changeWalkingTime.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeWalkingTimeDialog.open();
        	}
        });
        trayPopupMenu.add(changeWalkingTime);
        
        MenuItem changeIconStyle = new MenuItem("Change icon style");
        changeIconStyle.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeIconStyleDialog.open();
        	}
        });
        trayPopupMenu.add(changeIconStyle);
        
        MenuItem close = new MenuItem("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        trayPopupMenu.add(close);
    
        trayIcon = new TrayIcon(image, "VVStray", trayPopupMenu);
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