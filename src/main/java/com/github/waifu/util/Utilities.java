package com.github.waifu.util;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.gui.GUI;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Utilities {

    public static JSONObject json;


    public static Raider constructRaider(String username, Account account) {
        JSONArray members = GUI.getJson().getJSONObject("raid").getJSONArray("members");

        for (int i = 0; i < members.length(); i++) {
                Set<String> usernames = getRaiderUsernames(members, i);
                if (usernames.stream().anyMatch(username::equalsIgnoreCase)) {
                    boolean gotPriority = members.getJSONObject(i).getBoolean("got_priority");
                    boolean gotEarlyLocation = members.getJSONObject(i).getBoolean("got_earlyloc");
                    boolean inWaitingList = members.getJSONObject(i).getBoolean("in_waiting_list");
                    boolean inVC = members.getJSONObject(i).getBoolean("in_vc");
                    JSONArray reacts = members.getJSONObject(i).getJSONArray("reacts");
                    JSONArray roles = members.getJSONObject(i).getJSONArray("roles");
                    String id = members.getJSONObject(i).getString("user_id");
                    String avatar = members.getJSONObject(i).getString("avatar");
                    String timestampJoined = members.getJSONObject(i).getString("timestamp_joined");
                    List<Account> accounts = new ArrayList<>();
                    accounts.add(account);
                    return new Raider(id, timestampJoined, avatar, gotPriority, gotEarlyLocation, inWaitingList, inVC, roles, reacts, accounts);
                }
        }
        return null;
    }

    /**
     * parseRaiders method.
     *
     * Returns a Set containing all usernames in the raid for parsing sets.
     * Those who are runes are excluded from this Set.
     *
     * @param json JSONObject returned from the WebApp.
     */
    public static Set<String> parseRaiders(JSONObject json) throws IOException, InterruptedException {
        JSONArray members = json.getJSONObject("raid").getJSONArray("members");
        Set<String> set = new HashSet<>();

        for (int i = 0; i < members.length(); i++) {
            Set<String> usernames = getRaiderUsernames(members, i);
            set.addAll(usernames);
        }
        return set;
    }

    /**
     * parseRaiders method.
     *
     * Returns a Set containing all usernames in the raid for parsing sets.
     * Those who are runes are excluded from this Set.
     *
     * @param json JSONObject returned from the WebApp.
     */
    public static Set<String> parseRaiderSets(JSONObject json) throws IOException, InterruptedException {
        JSONArray members = json.getJSONObject("raid").getJSONArray("members");
        Set<String> set = new HashSet<>();
        Set<String> runes = new HashSet<>();
        Map<String, React> map = parseRaiderReacts(json);
        for (Map.Entry<String, React> m : map.entrySet()) {
            switch (m.getValue().getName()) {
                case "Helmet Rune", "Sword Rune", "Shield Rune":
                    for (int i = 0; i < m.getValue().getRaiders().size(); i++) {
                        runes.add(m.getValue().getRaiders().get(i).getName());
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
    public static Map<String, React> parseRaiderReacts(org.json.JSONObject json) throws IOException, InterruptedException {
        JSONArray usernames = json.getJSONObject("raid").getJSONArray("members");
        JSONArray reacts = json.getJSONObject("raid").getJSONArray("reacts");
        Map<String, React> m = new HashMap<>();
        for (int i = 0; i < reacts.length(); i++) {
            String id = String.valueOf(reacts.getJSONObject(i).getInt("id"));
            String name = reacts.getJSONObject(i).getString("name");
            String icon = reacts.getJSONObject(i).getString("icon");
            String requirement = String.valueOf(reacts.getJSONObject(i).get("reqs"));
            React react = new React(id, name, requirement, icon, new ArrayList<>());
            m.put(id, react);
        }
        for (int i = 0; i < usernames.length(); i++) {
            if (usernames.getJSONObject(i).getJSONArray("reacts") != null) {
                JSONArray userReacts = usernames.getJSONObject(i).getJSONArray("reacts");
                for (int j = 0; j < userReacts.length(); j++) {
                    Set<String> parsedUsernames = getRaiderUsernames(usernames, i);
                    for (String parsedUsername : parsedUsernames) {
                        m.get(String.valueOf(userReacts.get(j))).getRaiders().add(new Account(parsedUsername, null));
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
    public static Set<String> getRaiderUsernames(JSONArray usernames, int i) {
        Set<String> parsedUsernames = new HashSet<>();
        if (!usernames.getJSONObject(i).getBoolean("in_waiting_list")) {
            String[] username = usernames.getJSONObject(i).getString("server_nickname").split(" ");
            for (String u : username) {
                String replace = u.replaceAll("[^\\p{L}]", "");
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
     * @param color color of the background image
     */
    public static ImageIcon markImage(ImageIcon image, Color color) {
        BufferedImage bufferedImage = new BufferedImage(image.getIconWidth(), image.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());
        g.drawImage(image.getImage(), 0, 0, null);
        g.dispose();
        return new ImageIcon(bufferedImage);
    }

    public static URL getImageResource(String path) {
        if (Utilities.class.getClassLoader().getResource("resources/" + path) != null) {
            return Utilities.class.getClassLoader().getResource("resources/" + path);
        } else {
            return Utilities.class.getClassLoader().getResource(path);
        }
    }
}
