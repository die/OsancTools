package com.github.waifu.util;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class Utilities {

    public static JSONObject json;

    /**
     * parseRaiders method.
     *
     * Returns a Set containing all usernames in the raid for parsing sets.
     * Those who are runes are excluded from this Set.
     *
     * @param json JSONObject returned from the WebApp.
     */
    public static Set<String> parseRaiders(org.json.JSONObject json) {
        JSONArray members = json.getJSONObject("raid").getJSONArray("members");
        Set<String> set = new HashSet<>();
        Set<String> runes = new HashSet<>();
        Map<String, java.util.List<String>> map = parseRaiderReacts(json);
        for (Map.Entry<String, java.util.List<String>> m : map.entrySet()) {
            switch (m.getValue().get(1)) {
                case "Helmet Rune", "Sword Rune", "Shield Rune":
                    for (int i = 2; i < m.getValue().size(); i++) {
                        runes.add(m.getValue().get(i));
                    }
            }
        }
        for (int i = 0; i < members.length(); i++) {
            Set<String> usernames = getRaiderUsernames(members, i);
            /* Runes do not have to meet reqs. */
            for (String u : usernames) {
                if (!runes.contains(u)) {
                    set.add(u);
                }
            }
        }
        return set;
    }

    /**
     * parseRaiderReacts method.
     *
     * Returns a Map of react data and the usernames of those reacted, formatted as:
     * {22=[https://i.imgur.com/he4NSIZ.png, Helmet Rune, su, lag]}
     *
     * @param json JSONObject returned from the WebApp.
     */
    public static Map<String, java.util.List<String>> parseRaiderReacts(org.json.JSONObject json) {
        JSONArray usernames = json.getJSONObject("raid").getJSONArray("members");
        JSONArray reacts = json.getJSONObject("raid").getJSONArray("reacts");
        Map<String, List<String>> m = new HashMap<>();
        for (int i = 0; i < reacts.length(); i++) {
            java.util.List<String> l = new ArrayList<>();
            l.add(reacts.getJSONObject(i).getString("icon"));
            l.add(reacts.getJSONObject(i).getString("name"));
            m.put(String.valueOf(reacts.getJSONObject(i).getInt("id")), l);
        }
        for (int i = 0; i < usernames.length(); i++) {
            if (usernames.getJSONObject(i).getJSONArray("reacts") != null) {
                JSONArray userReacts = usernames.getJSONObject(i).getJSONArray("reacts");
                for (int j = 0; j < userReacts.length(); j++) {
                    Set<String> parsedUsernames = getRaiderUsernames(usernames, i);
                    for (String parsedUsername : parsedUsernames) {
                        m.get(String.valueOf(userReacts.get(j))).add(parsedUsername);
                    }
                }
            }
        }
        return m;
    }

    /**
     * getRaiderUsernames method.
     *
     * Returns a Set of usernames by splitting each nickname and removing invalid characters.
     * Ignores usernames that are in the waiting list, meaning they aren't in the raid.
     *
     * @param usernames contains JSONObjects for each raider.
     * @param i position in the JSONArray.
     */
    private static Set<String> getRaiderUsernames(JSONArray usernames, int i) {
        Set<String> parsedUsernames = new HashSet<>();
        if (!usernames.getJSONObject(i).getBoolean("in_waiting_list")) {
            String[] username = usernames.getJSONObject(i).getString("server_nickname").split(" ");
            for (String u : username) {
                String replace =  u.replaceAll("[^\\p{L}]", "");
                if (!replace.equals(""))
                {
                    parsedUsernames.add(replace);
                }
            }
        }
        return parsedUsernames;
    }

    /**
     * markImage method.
     *
     * Returns a modified ImageIcon that is marked with a background color.
     *
     * @param image image of the specific item.
     * @param severity determines the background color.
     */
    public static ImageIcon markImage(ImageIcon image, String severity) {
        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        switch (severity) {
            case "issue" -> g.setColor(Color.RED);
            case "warning" -> g.setColor(Color.YELLOW);
            case "good" -> g.setColor(Color.GREEN);
        }
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.drawImage(image.getImage(), 0, 0, null);
        g.dispose();
        return new ImageIcon(bufferedImage);
    }
}
