package com.github.waifu.gui;

import com.github.waifu.config.Settings;
import com.github.waifu.entities.*;
import com.github.waifu.entities.Character;
import com.github.waifu.handlers.PacketHandler;
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

    public static List<Account> accounts = new ArrayList<>();

    public static PacketProcessor packetProcessor;
    /**
     * Main method.
     *
     * Initializes the program.
     *
     * @param args input arguments that are not used.
     */
    public static void main(final String[] args) {
        try {
            Register.INSTANCE.register(PacketType.UPDATE, PacketHandler::handlePacket);
            packetProcessor = new PacketProcessor();
            packetProcessor.start();
            loadResources();
            SwingUtilities.invokeLater(GUI::new);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addAccount(Account account) {
        boolean found = false;
        for (Account a : accounts) {
            if (a.getName().equals(account.getName())) {
                found = true;
                accounts.set(accounts.indexOf(a), account);
                break;
            }
        }
        if (!found) {
            accounts.add(account);
        }
    }

    public static List<Raider> accountsToRaiders() {
        List<Raider> raiders = new ArrayList<>();
        for (Account account : accounts) {
            account.getRecentCharacter().getInventory().parseInventory();
            raiders.add(new Raider(account));
        }
        return raiders;
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
