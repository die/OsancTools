package com.github.waifu.config;

import java.util.Arrays;
import java.util.List;
import java.util.prefs.Preferences;

/**
 * Class that stores user preferences.
 */
public class Settings {

  /**
   * Preferences class to store and retrieve preferences.
   */
  private final Preferences preferences;
  /**
   * List of strings that define what settings are stored.
   */
  private final List<String> settings;
  /**
   * List of strings that define available requirement sheets.
   */
  private final List<String> requirementSheets;

  /**
   * Constructor that defines available settings and requirement sheets.
   */
  public Settings() {
    preferences = Preferences.userRoot();
    settings = Arrays.asList(
            "token",
            "betaToken",
            "theme",
            "stat",
            "requirement",
            "requirementSheetName",
            "showAlert",
            "resourceDir");

    requirementSheets = Arrays.asList(
            "OryxSanctuary",
            "OryxSanctuaryVeteran",
            "PubHallsVeteran",
            "PubHallsSemicar",
            "PubHallsAdvanced",
            "PubHallsExalt",
            "PubHallsMisc");
  }

  /**
   * Retrieves WebApp token.
   *
   * @return WebApp token as a String.
   */
  public String getToken() {
    return preferences.get("token", "");
  }

  /**
   * Sets the WebApp token.
   *
   * @param token WebApp token as a String.
   */
  public void setToken(final String token) {
    preferences.put("token", token);
  }

  /**
   * Retrieves Beta WebApp token.
   *
   * @return Beta WebApp token as a String.
   */
  public String getBetaToken() {
    return preferences.get("betaToken", "");
  }

  /**
   * Sets the Beta WebApp token.
   *
   * @param betaToken Beta WebApp token as a String.
   */
  public void setBetaToken(final String betaToken) {
    preferences.put("betaToken", betaToken);
  }

  /**
   * Retrieves current theme name.
   *
   * @return theme name as a String.
   */
  public String getTheme() {
    return preferences.get("theme", "light");
  }

  /**
   * Sets the current theme name.
   *
   * @param theme theme name as a String.
   */
  public void setTheme(final String theme) {
    preferences.put("theme", theme);
  }

  /**
   * Retrieves current stat used to calculate exalts.
   *
   * @return Stat index (0-7) as an int.
   */
  public int getStat() {
    return preferences.getInt("stat", 0);
  }

  /**
   * Sets the current stat used to calculate exalts.
   *
   * @param stat Stat index (0-7) as an int.
   */
  public void setStat(final int stat) {
    preferences.putInt("stat", stat);
  }

  /**
   * Retrieves current requirement used to calculate exalts.
   *
   * @return Requirement quantity as an int.
   */
  public int getRequirement() {
    final int defaultRequirement = 100;
    return preferences.getInt("requirement", defaultRequirement);
  }

  /**
   * Sets current requirement used to calculate exalts.
   *
   * @param requirement Requirement quantity as an int.
   */
  public void setRequirement(final int requirement) {
    preferences.putInt("requirement", requirement);
  }

  /**
   * Retrieves boolean to show if Realm-eye is down.
   *
   * @return Boolean to show the alert.
   */
  public boolean showAlert() {
    return preferences.getBoolean("showAlert", true);
  }

  /**
   * Sets boolean to show if Realm-eye is down.
   *
   * @param showAlert Boolean to show the alert.
   */
  public void setShowAlert(final boolean showAlert) {
    preferences.putBoolean("showAlert", showAlert);
  }

  /**
   * Retrieves directory of resources.assets.
   *
   * @return Resource directory as a String.
   */
  public String getResourceDir() {
    return preferences.get("resourceDir", "");
  }

  /**
   * Sets directory of resources.assets.
   *
   * @param resourceDir Resource directory as a String.
   */
  public void setResourceDir(final String resourceDir) {
    preferences.put("resourceDir", resourceDir);
  }

  /**
   * Retrieves current requirement sheet name.
   *
   * @return Requirement sheet name as a String.
   */
  public String getRequirementSheetName() {
    // handling for anything set that isn't a valid requirement sheet on Github
    final String name = preferences.get("requirementSheetName", requirementSheets.get(0));
    if (!requirementSheets.contains(name)) {
      return "OryxSanctuary";
    } else {
      return preferences.get("requirementSheetName", requirementSheets.get(0));
    }
  }

  /**
   * Sets current requirement sheet name.
   *
   * @param name Requirement sheet name as a String.
   */
  public void setRequirementSheetName(final String name) {
    preferences.put("requirementSheetName", name);
  }

  /**
   * Retrieves list of all available requirement sheets.
   *
   * @return Requirement sheet names as a List.
   */
  public List<String> getRequirementSheets() {
    return requirementSheets;
  }

  /**
   * Iterates through all settings and deletes stored data.
   */
  public void clearSettings() {
    for (final String s : settings) {
      preferences.remove(s);
    }
  }
}
