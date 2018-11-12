package de.oliver_arend.VVStray;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;

public class UserSettingsProvider {
	
	private static final Gson gson = new Gson();
	private static UserSettings settings;
	
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
	
	public static void setUserSettings(UserSettings u) {
		settings = u;
		String settingsString = gson.toJson(u);
		try {
			Utils.writeFile("settings.json", settingsString);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
	}
	
}
