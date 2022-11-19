package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import javax.swing.*;

/**
 * Character class to store character data.
 */
public class Character {

    private final String type;
    private final String skin;
    private ImageIcon skinImage;
    private final String level;
    private final String cqc;
    private final String fame;
    private final String exp;
    private final String place;
    private final String stats;
    private final String lastSeen;
    private final String server;
    private final Inventory inventory;

    private boolean maxedHP;
    private boolean maxedMP;
    private String exaltedHP;
    private String exaltedMP;
    private String dexterity;

    /**
     * Character method.
     *
     * Constructs a default Character as a level 0 Wizard.
     */
    public Character() {
        this.type = "Wizard";
        this.skin = "";
        this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/Wizard.png"));
        this.level = "";
        this.cqc = "";
        this.fame = "";
        this.exp = "";
        this.place = "";
        this.stats = "";
        this.lastSeen = "";
        this.server = "";
        this.inventory = new Inventory();
    }

    /**
     * Character method.
     *
     * Constructs a Character with all information.
     *
     * @param type class of the Character.
     * @param inventory Inventory of the Character.
     */
    public Character(String type, Inventory inventory) {
        this.type = type;
        this.skin = "";
        this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/" + type + ".png"));
        this.level = "";
        this.cqc = "";
        this.fame = "";
        this.exp = "";
        this.place = "";
        this.stats = "";
        this.lastSeen = "";
        this.server = "";
        this.inventory = inventory;
    }

    /**
     * Character method.
     *
     * Constructs a Character with information by the Update Packet
     * @param maxedHP = true if maxed in hp
     * @param dexterity = dexterity stat value
     *
     */
    public Character(Inventory inventory, int level, int currentFame, boolean maxedHP, boolean maxedMP, int dexterity, int exaltedHP, int exaltedMP) {
        this.type = "Wizard";
        this.skin = "";
        this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/Wizard.png"));
        this.level = String.valueOf(level);;
        this.cqc = "";
        this.fame = String.valueOf(currentFame);
        this.exp = "";
        this.place = "";
        this.stats = "";
        this.lastSeen = "";
        this.server = "";
        this.inventory = inventory;
        this.maxedHP = maxedHP;
        this.maxedMP = maxedMP;
        this.exaltedHP = String.valueOf(exaltedHP);
        this.exaltedMP = String.valueOf(exaltedMP);
        this.dexterity = String.valueOf(dexterity);
    }

    /**
     * Character method.
     *
     * Constructs a Character with all information.
     *
     * @param type class of the Character.
     * @param skin skin the Character has equipped.
     * @param level level of the Character.
     * @param cqc number of class quests completed the Character has.
     * @param fame number of fame the Character has.
     * @param exp number of exp the Character has.
     * @param place rank of the Character among others of its type.
     * @param stats stats of the Character, currently as ?/8.
     * @param inventory Inventory of the Character.
     */
    public Character(String type, String skin, String level, String cqc, String fame, String exp, String place, String stats, String lastSeen, String server, Inventory inventory) {
        this.type = type;
        this.skin = skin;
        this.skinImage = new ImageIcon(Utilities.getImageResource("images/skins/" + type + ".png"));
        this.level = level;
        this.cqc = cqc;
        this.fame = fame;
        this.exp = exp;
        this.place = place;
        this.stats = stats;
        this.lastSeen = lastSeen;
        this.server = server;
        this.inventory = inventory;
    }

    /**
     *
     */
    public void printCharacter() {
        System.out.println("Type: " + this.type + "\n" +
                "Skin: " + this.skin + "\n" +
                "Level: " + this.level + "\n" +
                "CQC: " + this.cqc + "\n" +
                "Fame: " + this.fame + "\n" +
                "Exp: " + this.exp + "\n" +
                "Place: " + this.place + "\n" +
                "Stats: " + this.stats + "\n" +
                "Last Seen: " + this.lastSeen + "\n" +
                "Server: " + this.server + "\n");
    }

    /**
     *
     * @return
     */
    public String getType() {
        return this.type;
    }

    /**
     *
     * @return
     */
    public String getSkin() {
        return this.skin;
    }

    /**
     *
     * @return
     */
    public ImageIcon getSkinImage() {
        return this.skinImage;
    }

    /**
     *
     * @param skinImage
     */
    public void setSkinImage(ImageIcon skinImage) {
        this.skinImage = skinImage;
    }

    /**
     *
     * @return
     */
    public String getLevel() {
        return this.level;
    }

    /**
     *
     * @return
     */
    public String getCqc() {
        return this.cqc;
    }

    /**
     *
     * @return
     */
    public String getFame() {
        return this.fame;
    }

    /**
     *
     * @return
     */
    public String getExp() {
        return this.exp;
    }

    /**
     *
     * @return
     */
    public String getPlace() {
        return this.place;
    }

    /**
     *
     * @return
     */
    public String getStats() {
        return this.stats;
    }

    /**
     *
     * @return
     */
    public Inventory getInventory() {
        return this.inventory;
    }
}
