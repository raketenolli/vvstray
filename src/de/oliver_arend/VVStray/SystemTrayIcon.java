package de.oliver_arend.VVStray;

import java.awt.*;
import java.awt.event.*;
import de.oliver_arend.VVStray.ModesOfTransport;

public class SystemTrayIcon {
    private TrayIcon trayIcon;
    private Image image;
    private ChangeDelayDialog changeDelayDialog;
    private ChangeTransportPreferencesDialog changeTransportPreferencesDialog;
    
    public SystemTrayIcon(VVStray parent) {
        if(!SystemTray.isSupported()){
            System.out.println("System tray is not supported !!! ");
            return ;
        }
        SystemTray systemTray = SystemTray.getSystemTray();

        image = Toolkit.getDefaultToolkit().getImage("default.png");

        PopupMenu trayPopupMenu = new PopupMenu();
        
        changeDelayDialog = new ChangeDelayDialog(parent);
        changeTransportPreferencesDialog = new ChangeTransportPreferencesDialog(parent);
        
        MenuItem changeTransportPreferences = new MenuItem("Change mode of transport preferences");
        changeTransportPreferences.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeTransportPreferencesDialog.open();
        	}
        });
        trayPopupMenu.add(changeTransportPreferences);
        
        MenuItem changeDelay = new MenuItem("Change walking time to stop");
        changeDelay.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		changeDelayDialog.open();
        	}
        });
        trayPopupMenu.add(changeDelay);
        
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