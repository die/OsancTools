package com.github.waifu.config;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

public class Settings {

    private final Preferences preferences;
    private final List<String> settings;

    public Settings() {
        preferences = Preferences.userRoot();
        settings = Arrays.asList("token",
                "betaToken",
                "theme",
                "stat",
                "requirement",
                "showAlert");
    }

    public String getToken() {
        return preferences.get("token", "");
    }

    public void setToken(String token) {
        preferences.put("token", token);
    }

    public String getBetaToken() {
        return preferences.get("betaToken", "");
    }

    public void setBetaToken(String betaToken) {
        preferences.put("betaToken", betaToken);
    }

    public String getTheme() {
        return preferences.get("theme", "light");
    }

    public void setTheme(String theme) {
        preferences.put("theme", theme);
    }

    public int getStat() {
        return preferences.getInt("stat", 0);
    }

    public void setStat(int stat) {
        preferences.putInt("stat", stat);
    }

    public int getRequirement() {
        return preferences.getInt("requirement", 100);
    }

    public void setRequirement(int requirement) {
        preferences.putInt("requirement", requirement);
    }

    public boolean showAlert() {
        return preferences.getBoolean("showAlert", true);
    }

    public void setShowAlert(boolean showAlert) {
        preferences.putBoolean("showAlert", showAlert);
    }

    public void clearSettings() {
        for (String s : settings) {
            preferences.remove(s);
        }
    }
}
