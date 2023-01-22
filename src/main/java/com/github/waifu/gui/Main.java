package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.util.Utilities;
import java.io.IOException;
import java.net.URL;
import javax.swing.SwingUtilities;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Main class to initialize the UI.
 */
public final class Main {

  /**
   * To be documented.
   */
  private static Settings settings = new Settings();

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
  public static void main(final String[] args) {
    try {
      loadRequirementSheet();
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
    final URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/master/src/main/resources/sheets/" + Main.settings.getRequirementSheetName() + ".json");
    final JSONTokener tokener = new JSONTokener(url.openStream());
    Utilities.setJson(new JSONObject(tokener));
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public static Settings getSettings() {
    return settings;
  }
}
