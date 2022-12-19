package com.github.waifu.handlers;

import com.github.waifu.entities.*;
import com.github.waifu.entities.Character;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.Main;
import packets.incoming.*;
import packets.Packet;
import packets.data.StatData;

import java.nio.charset.StandardCharsets;
import java.util.*;


public class PacketHandler {

    private static Map<String, String> equipmentData;

    public static void loadEquipmentData() {
        equipmentData = new HashMap<>();

        Scanner scanner = new Scanner (Objects.requireNonNull(Main.class.getClassLoader().getResourceAsStream("Equipment.txt")), StandardCharsets.UTF_8);
        scanner.useDelimiter(":");
        String line;
        while (scanner.hasNext()) {
            line = scanner.nextLine();
            String[] parsed = line.split(":");
            switch (parsed.length) {
                case 2 -> equipmentData.put(parsed[0], parsed[1]);
                case 3 -> equipmentData.put(parsed[0], parsed[1] + " " + parsed[2]);
                default -> {

                }
            }
        }
        scanner.close();

    }

    public static void handlePacket(Packet packet) {
        UpdatePacket updatePacket = (UpdatePacket) packet;

        boolean maxedMP = false;
        boolean maxedHP = false;
        int maxMPValue=0;
        int maxHPValue=0;
        int currentHPValue=0;
        int currentMPValue=0;
        int stars = 0;
        int level = 0;
        String charClass = "";
        String weapon="";
        String ability="";
        String armor="";
        String ring="";
        String userName = "";
        String guildName = "";
        String guildRank = "";
        int fame = 0;
        int currentFame = 0;
        int exaltedHP = 0;
        int exaltedMP = 0;
        int dexterity = 0;

        for (int i=0; i < updatePacket.newObjects.length; i++){
            StatData[] stats = updatePacket.newObjects[i].status.stats;

            for (StatData stat : stats) {
                switch (stat.statType) {
                    case MAX_MP_STAT -> maxMPValue = stat.statValue;
                    case MP_STAT -> currentMPValue = stat.statValue;
                    case NUM_STARS_STAT -> stars = stat.statValue;
                    case MAX_HP_STAT -> maxHPValue = stat.statValue;
                    case HP_STAT -> currentHPValue = stat.statValue;
                    case LEVEL_STAT -> level = stat.statValue;
                    case PLAYER_ID -> charClass = getClassName(String.valueOf(updatePacket.newObjects[i].objectType));
                    case INVENTORY_0_STAT -> weapon =  getItemName(String.valueOf(stat.statValue));
                    case INVENTORY_1_STAT -> ability = getItemName(String.valueOf(stat.statValue));
                    case INVENTORY_2_STAT -> armor =  getItemName(String.valueOf(stat.statValue));
                    case INVENTORY_3_STAT -> ring =  getItemName(String.valueOf(stat.statValue));
                    case NAME_STAT -> userName = stat.stringStatValue;
                    case GUILD_NAME_STAT -> guildName = stat.stringStatValue;
                    case GUILD_RANK_STAT -> guildRank = stat.stringStatValue;
                    case FAME_STAT -> fame = stat.statValue;
                    case CURR_FAME_STAT -> currentFame = stat.statValue;
                    case EXALTED_HP -> exaltedHP = stat.statValue;
                    case EXALTED_MP -> exaltedMP = stat.statValue;
                    case DEXTERITY_STAT -> dexterity = stat.statValue;
                }
            }
        }
        if (userName.equals("") || weapon.equals("")) return;
        if(maxHPValue!=0 || maxHPValue==currentHPValue) {
            maxedHP = true;
        }
        if(maxMPValue!=0 || maxMPValue==currentMPValue) {
            maxedMP = true;
        }
        Item weaponItem = new Item(weapon, "weapon", charClass);
        Item abilityItem = new Item(ability, "ability", charClass);
        Item armorItem = new Item(armor, "armor", charClass);
        Item ringItem = new Item(ring, "ring", charClass);
        List<Item> items = new ArrayList<>();
        Collections.addAll(items, weaponItem, abilityItem, armorItem, ringItem);
        Account account = createAccount(userName, stars, fame, guildName, guildRank, items, level, maxedMP, maxedHP, currentFame, exaltedHP, exaltedMP, dexterity);
        System.out.println(account.getName() + " : " + account.getRecentCharacter().getInventory().printInventory());
        GUI.raid.addSnifferAccount(account);
    }

    public static String getClassName(String id ) {
        return Objects.requireNonNullElse(equipmentData.get(id), "Wizard");
    }

    public static String getItemName(String id) {
        if (id.equals("-1")) {
            return "Empty slot";
        } else {
            return Objects.requireNonNullElse(equipmentData.get(id), "Failed To Parse Item UT");
        }
    }

    public static Account createAccount(String userName, int stars, int fame, String guildName, String guildRank, List<Item> items, int level,boolean maxedMP, boolean maxedHP, int currentFame, int exaltedHP, int exaltedMP, int dexterity) {

        Inventory inventory = new Inventory(items);
        Character character = new Character(inventory, level, currentFame, maxedHP, maxedMP, dexterity, exaltedHP, exaltedMP);

        List<Character> characterlist = new ArrayList<>();
        characterlist.add(character);

        return new Account(userName, characterlist, stars, fame, guildName, guildRank);
    }
}
