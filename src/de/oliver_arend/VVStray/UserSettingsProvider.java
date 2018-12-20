package de.oliver_arend.VVStray;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class UserSettingsProvider {
	
	private static final Gson gson = new Gson();
	private static UserSettings settings;
    private static List<PropertyChangeListener> listeners = new ArrayList<PropertyChangeListener>();
	
	private UserSettingsProvider() { }
	
	public static UserSettings getUserSettings() {
		if(settings == null) {
			try {
				String settingsString = Utils.readFile("settings.json", StandardCharsets.UTF_8);
				settings = gson.fromJson(settingsString, UserSettings.class);
			} catch(IOException e) {
				System.out.println(e.toString());
			}
		}
		return settings;
	}
	
	public static void setUserSettings(UserSettings u, Object source) {
		settings = u;
		String settingsString = gson.toJson(u);
		try {
			Utils.writeFile("settings.json", settingsString);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		notifyListeners(source, u);
	}
	
    private static void notifyListeners(Object source, UserSettings newSettings) {
        for (PropertyChangeListener listener : listeners) {
            listener.propertyChange(new PropertyChangeEvent(source, "", null, newSettings));
        }
    }
		
    public static void addListener(PropertyChangeListener newListener) {
        listeners.add(newListener);
    }
}
