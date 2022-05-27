package com.github.waifu.gui;

import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import javax.swing.SwingUtilities;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Main class to initialize the UI.
 */
final class Main {

    private Main() { }

    /**
     * Main method.
     *
     * Initializes the program.
     *
     * @param args input arguments that are not used.
     */
    public static void main(final String[] args) {
        try {
            loadResources();
            SwingUtilities.invokeLater(GUI::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * loadResources method.
     *
     * Loads ROTMG.json from the resources file for other classes to use.
     */
    public static void loadResources() {
        JSONTokener tokener = new JSONTokener(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("resources/ROTMG.json")), StandardCharsets.UTF_8));
        Utilities.json = new JSONObject(tokener);
    }
}
