package com.github.waifu.handlers;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.List;
import java.util.ArrayList;

/**
 * RealmeyeRequestHandler class to retrieve data from Realmeye.
 */
public class RealmeyeRequestHandler {

    /**
     * GET method.
     *
     * Returns an Account object of the given username.
     * If the account is private or has no characters,
     * a default Account object is returned.
     *
     * @param username username of the raider.
     */
    public static Account GET(String username) throws IOException {
        Document doc;
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
            doc = Jsoup.connect("https://www.realmeye.com/player/" + username).proxy(proxy).get();
        } catch (Exception e){
            doc = Jsoup.connect("https://www.realmeye.com/player/" + username).get();
        }
        if (doc.select("h2").text().equals("Sorry, but we either:")) {
            return getPrivateAccount(username);
        } else if (doc.select("h3").text().equals("Characters are hidden")) {
            return getPrivateAccount(username);
        } else if (doc.select("table").eachText().get(0).split(" ")[1].equals("0")) {
            return getPrivateAccount(username);
        } else {
            try {
                int numberOfSkins = 0;
                int exaltations = 0;
                int stars = Integer.parseInt(Jsoup.parse(doc.html()).select("div[class=star-container]").text());
                long accountFame = 0;
                String guild = "";
                String guildRank = "";
                String creationDate = "";
                String lastSeen = "";
                List<String> characterData = Jsoup.parse(doc.html()).select("table[class=table table-striped tablesorter] tr").eachText();
                List<String> characterSkinData = Jsoup.parse(doc.html()).select("a.character").eachAttr("data-skin");
                List<String> itemData = Jsoup.parse(doc.html()).select("span.item").eachAttr("title");
                List<Character> characters = new ArrayList<>();
                for (int i = 1; i < characterData.size(); i++) {
                    String[] metadata = characterData.get(i).split(" ");
                    String type = metadata[0];
                    int level = Integer.parseInt(metadata[1]);
                    String cqc = metadata[2];
                    long fame = Integer.parseInt(metadata[3]);
                    long exp = Integer.parseInt(metadata[4]);
                    int place = Integer.parseInt(metadata[5]);
                    String stats = metadata[6];
                    long skin = Long.parseLong(characterSkinData.get(i-1));
                    List<String> items = itemData.subList(4 * i - 4, 4 * i);
                    List<Item> inventory = new ArrayList<>();
                    for (int j = 0; j < items.size(); j++) {
                        String itemType = switch (j) {
                            case 0 -> "weapon";
                            case 1 -> "ability";
                            case 2 -> "armor";
                            case 3 -> "ring";
                            default -> "";
                        };
                        inventory.add(new Item(items.get(j), itemType, type));
                    }
                    Character character = new Character(type, skin, level, cqc, fame, exp, place, stats, new Inventory(inventory));
                    characters.add(character);
                }
                return new Account(username, stars, numberOfSkins, exaltations, accountFame, guild, guildRank, creationDate, lastSeen, characters);
            } catch (Exception e) {
                return getPrivateAccount(username);
            }
        }
    }

    /**
     * getPrivateAccount method.
     *
     * Returns an empty Account object that contains:
     * The username of the private profile.
     * One wizard character with all empty slots.
     *
     * @param username username of the raider.
     */
    private static Account getPrivateAccount(String username) {
        List<Character> characters = new ArrayList<>();
        characters.add(new Character());
        return new Account(username, characters);
    }

    /**
     * GETExalts method.
     *
     * Returns a list of character exalts formatted as:
     * [Knight, 18, +25, +10, +0, +2, +2, +5, +1, +1]
     *
     * @param username username of the account to get the exalts from.
     */
    public static List<String[]> GETExalts(String username) throws IOException {
        List<String[]> collection = new ArrayList<>();
        Document doc;
        try {
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 9150));
            doc = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).proxy(proxy).get();
        } catch (Exception e){
            doc = Jsoup.connect("https://www.realmeye.com/exaltations-of/" + username).get();
        }
        /* Handles exaltations */
        if (doc.select("h2").text().equals("Sorry, but we either:")) {
            return null;
        }
        Elements exaltationTable = doc.select("#f").select("tr");
        // table label is either #f or #e
        if (!exaltationTable.hasText()) {
            exaltationTable = doc.select("#e").select("tr");
        }
        for (int i = 1; i < exaltationTable.size(); i++) {
            Elements data = exaltationTable.get(i).select("td");
            String[] Exalts = new String[10];

            for (int j = 1; j < data.size(); j++) {
                if (!data.get(j).hasText()) {
                    Exalts[j-1] = "+0";
                } else {
                    Exalts[j-1] = data.get(j).text();
                }
            }
            collection.add(Exalts);
        }
        return collection;
    }
}
