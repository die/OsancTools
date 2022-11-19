package com.github.waifu.handlers;

import com.github.waifu.entities.*;
import com.github.waifu.entities.Character;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.Main;
import com.github.waifu.util.Utilities;
import packets.data.enums.StatType;
import packets.incoming.*;
import packets.Packet;
import packets.data.StatData;
import util.IdToName;

import java.io.File;
import java.net.URL;
import java.util.*;


public class PacketHandler {

    public static void testPacket(Packet packet) {
        UpdatePacket updatePacket = (UpdatePacket) packet;


        /**CLASS NAME
         *
         */

        for (int i = 0; i < updatePacket.newObjects.length; i++) {
            StatData[] stats = updatePacket.newObjects[i].status.stats;

            for (int k = 0; k < stats.length; k++){
                if(stats[k].statType == StatType.PLAYER_ID)
                    System.out.println(IdToName.objectName(updatePacket.newObjects[i].objectType));

            }


            /*for (int k = 0; k < objects.length; k++) {
            }*/
        }
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

            for(int k=0; k < stats.length; k++){
                switch(stats[k].statType){
                    case MAX_MP_STAT -> maxMPValue = stats[k].statValue;
                    case MP_STAT -> currentMPValue = stats[k].statValue;
                    case NUM_STARS_STAT -> stars = stats[k].statValue;
                    case MAX_HP_STAT -> maxHPValue = stats[k].statValue;
                    case HP_STAT -> currentHPValue = stats[k].statValue;
                    case LEVEL_STAT -> level = stats[k].statValue;
                    case INVENTORY_0_STAT -> weapon = getItemLabel(IdToName.objectName(stats[k].statValue));
                    case INVENTORY_1_STAT -> ability = getItemLabel(IdToName.objectName(stats[k].statValue));
                    case INVENTORY_2_STAT -> armor = getItemLabel(IdToName.objectName(stats[k].statValue));
                    case INVENTORY_3_STAT -> ring = getItemLabel(IdToName.objectName(stats[k].statValue));
                    case NAME_STAT -> userName = stats[k].stringStatValue;
                    case GUILD_NAME_STAT -> guildName = stats[k].stringStatValue;
                    case GUILD_RANK_STAT -> guildRank = stats[k].stringStatValue;
                    case FAME_STAT -> fame = stats[k].statValue;
                    case CURR_FAME_STAT -> currentFame = stats[k].statValue;
                    case EXALTED_HP -> exaltedHP = stats[k].statValue;
                    case EXALTED_MP -> exaltedMP = stats[k].statValue;
                    case DEXTERITY_STAT -> dexterity = stats[k].statValue;
                }
            }
        }
        if (userName.equals("") || weapon.equals("")) return;
        if(maxHPValue!=0 || maxHPValue==currentHPValue)
            maxedHP=true;
        if(maxMPValue!=0 || maxMPValue==currentMPValue)
            maxedMP=true;
        //todo: get weapon type and item class
        Item weaponItem = new Item(weapon, "weapon", "Wizard");
        Item abilityItem = new Item(ability, "ability", "Wizard");
        Item armorItem = new Item(armor, "armor", "Wizard");
        Item ringItem = new Item(ring, "ring", "Wizard");
        List<Item> items = new ArrayList<>();
        Collections.addAll(items, weaponItem, abilityItem, armorItem, ringItem);
        Account account = createAccount(userName, stars, fame, guildName, guildRank, items, level, maxedMP, maxedHP, currentFame, exaltedHP, exaltedMP, dexterity);
        System.out.println(account.getName() + " : " + account.getRecentCharacter().getInventory().printInventory());
        Main.addAccount(account);
    }

    public static String getItemLabel(String name) {
        if (name.equals("Unloaded")) {
            return "Empty slot";
        } else {
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
        }
        return name;
    }

    public static Account createAccount(String userName, int stars, int fame, String guildName, String guildRank, List<Item> items, int level,boolean maxedMP, boolean maxedHP, int currentFame, int exaltedHP, int exaltedMP, int dexterity) {

        Inventory inventory = new Inventory(items);
        Character character = new Character(inventory, level, currentFame, maxedHP, maxedMP, dexterity, exaltedHP, exaltedMP);

        List<Character> characterlist = new ArrayList<>();
        characterlist.add(character);

        return new Account(userName, characterlist, stars, fame, guildName, guildRank);
    }


}

        /*if(userName!="") {
            System.out.println("stars: " + stars);
            System.out.println("level: " + level);
            System.out.println("weapon: " + weapon);
            System.out.println("ability: " + ability);
            System.out.println("armor: " + armor);
            System.out.println("ring: " + ring);
            System.out.println("username: " + userName);
            System.out.println("guildname: " + guildName);
            System.out.println("guildrank: " + guildRank);
            System.out.println("fame: " + fame);
            System.out.println("current fame: " + currentFame);
            System.out.println("exalted hp: " + exaltedHP);
            System.out.println("exalted mp " + exaltedMP);
            System.out.println("dex: " + dexterity);
            System.out.println("");
        }*/