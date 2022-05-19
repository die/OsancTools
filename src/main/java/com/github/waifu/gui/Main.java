package com.github.waifu.gui;

import com.github.waifu.util.Utilities;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.SwingUtilities;
import java.io.InputStreamReader;
import java.io.IOException;
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
    public static void loadResources() throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        Utilities.json = (JSONObject)jsonParser.parse(new InputStreamReader(Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("resources/ROTMG.json")), StandardCharsets.UTF_8));
    
}
