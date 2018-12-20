package de.oliver_arend.VVStray;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.UIManager;

public class VVStray implements PropertyChangeListener {

	private SystemTrayIcon trayIcon;
	private DepartureProvider departureProvider;
	
	public VVStray() {
		UserSettingsProvider.addListener(this);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {}

		trayIcon = new SystemTrayIcon();
		departureProvider = new DepartureProvider();

        Runnable updateRunnable = new Runnable() {
			public void run() {
				update();
			}
		};
		ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
		exec.scheduleAtFixedRate(updateRunnable, 0, 1, TimeUnit.MINUTES);
    }
	
	@Override
	public void propertyChange(PropertyChangeEvent newSettings) {
		update();
	}
	
	public void update() {
    	try {
            trayIcon.update(departureProvider.getTrayIconDescriptor());
    	} catch(MalformedURLException e) {
    		trayIcon.update(new TrayIconDescriptor("MalformedURL Exception when trying to obtain new connection information", "!", ModesOfTransport.WARNING, false));
    	} catch(IOException e) {
    		trayIcon.update(new TrayIconDescriptor("IOException when trying to obtain new connection information", "!", ModesOfTransport.WARNING, false));
    	} catch(NullPointerException e) {
    		trayIcon.update(new TrayIconDescriptor("NullPointerException when trying to parse response body to departure", "!", ModesOfTransport.WARNING, false));
    	}
	}
	
	public void waitOneMinute() {
        try { Thread.sleep(60 * 1000); } 
        catch(InterruptedException e) { e.printStackTrace(); }
	}
	
    public void run() {
        while(true) {
        	update();
        	waitOneMinute();
        }
    }
	
	public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VVStray();
            }
        });
	}

}
