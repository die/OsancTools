package com.github.waifu.gui;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.config.Settings;
import com.github.waifu.handlers.RequirementSheetHandler;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import javax.swing.*;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Main class to initialize the UI.
 */
public final class Main {

  /**
   * To be documented.
   */
  private static final Settings SETTINGS = new Settings();

  /**
   * To be documented.
   */
  private Main() {
  }

  /**
   * Main method.
   *
   * <p>Initializes the program.
   *
   * @param args input arguments that are not used.
   */
  public static void main(final String[] args) throws IOException, InterruptedException, InvocationTargetException {
    final SplashScreen splashScreen = new SplashScreen();

    try {
      final RotmgAssets assets = new RotmgAssets();
      boolean loaded = assets.loadAssets(splashScreen.getProgressBar1(), splashScreen.getLabel());

      if (!loaded) {
        SplashScreen.chooseResourcesFile();
        splashScreen.dispose();
        System.exit(0);
      }
      splashScreen.dispose();
    } catch (final OutOfMemoryError | IOException e) {
      e.printStackTrace();
      splashScreen.dispose();
      System.exit(0);
    }

    try {
      Main.loadRequirementSheet();
      SwingUtilities.invokeLater(Gui::new);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * loadRequirementSheet method.
   *
   * <p>Loads requirement sheets from the repository.
   */
  public static void loadRequirementSheet() throws IOException {
    final URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/master/src/main/resources/sheets/" + getSettings().getRequirementSheetName() + ".json");
    final JSONTokener tokener = new JSONTokener(url.openStream());
    RequirementSheetHandler.setRequirementSheet(new JSONObject(tokener));
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static Settings getSettings() {
    return SETTINGS;
  }
}
