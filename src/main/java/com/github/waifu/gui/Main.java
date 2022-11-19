package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.util.Utilities;
import org.json.JSONObject;
import org.json.JSONTokener;
import packets.Packet;
import packets.PacketType;
import packets.data.StatData;
import packets.data.enums.StatType;
import packets.incoming.UpdatePacket;
import packets.packetcapture.PacketProcessor;
import packets.packetcapture.register.Register;
import util.IdToName;

import javax.swing.SwingUtilities;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static packets.data.enums.StatType.NAME_STAT;

/**
 * Main class to initialize the UI.
 */
public final class Main {

    public static Settings settings = new Settings();

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
            Register.INSTANCE.register(PacketType.UPDATE, Main::print);
            PacketProcessor packetProcessor = new PacketProcessor();
            packetProcessor.start();
            //loadResources();
            //SwingUtilities.invokeLater(GUI::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void print(Packet packet) {
        UpdatePacket updatePacket = (UpdatePacket) packet;
        String username = "";
        String weapon = "";
        String ability = "";
        String armor = "";
        String ring = "";

        for (int i = 0; i < updatePacket.newObjects.length; i++) {
            StatData[] stats = updatePacket.newObjects[i].status.stats;

            for (StatData stat : stats) {
                switch (stat.statType) {
                    case NAME_STAT -> username = stat.stringStatValue;
                    case INVENTORY_0_STAT -> weapon = getItemLabel(IdToName.objectName(stat.statValue));
                    case INVENTORY_1_STAT -> ability = getItemLabel(IdToName.objectName(stat.statValue));
                    case INVENTORY_2_STAT -> armor = getItemLabel(IdToName.objectName(stat.statValue));
                    case INVENTORY_3_STAT -> ring = getItemLabel(IdToName.objectName(stat.statValue));
                }
            }
        }
        if (!username.equals("")) {
            List<Item> itemList = new ArrayList<>();
            itemList.add(new Item(weapon, "weapon", "Wizard")); // fix itemClass
            itemList.add(new Item(ability, "ability", "Wizard"));
            itemList.add(new Item(armor, "armor", "Wizard"));
            itemList.add(new Item(ring, "ring", "Wizard"));
            Inventory inventory = new Inventory(itemList);
            List<Character> characterList = new ArrayList<>();
            characterList.add(new Character("Wizard", inventory));
            Account account = new Account(username, characterList);
            System.out.println(account.getName() + " : " + account.getRecentCharacter().getInventory().printInventory());
        }
    }

    public static String getItemLabel(String name) {
        if (name.equals("")) {
            return "Empty slot";
        }
        try {
            URL url = Utilities.getImageResource("images/items/");
            File file = new File(url.getFile());
            for (String s : Objects.requireNonNull(file.list())) {
                s = s.replace(".png", "");
                String[] split = s.split(" ");
                String label = split[split.length - 1];
                String nameFromImage = s.replace(" " + label, "");
                String result;
                if (nameFromImage.equals(name)) {
                    result = name + " " + label;
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * loadResources method.
     *
     * Loads osanc.json from the repository.
     */
    public static void loadResources() throws IOException {
        URL url = new URL("https://raw.githubusercontent.com/Waifu/OsancTools/master/src/main/resources/osanc.json");
        JSONTokener tokener = new JSONTokener(url.openStream());
        Utilities.json  = new JSONObject(tokener);
    }
}
