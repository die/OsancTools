package com.github.waifu.entities;

import javax.swing.*;
import java.util.Objects;

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

    /**
     * Character method.
     *
     * Constructs a default Character as a level 0 Wizard.
     */
    public Character() {
        this.type = "Wizard";
        this.skin = "";
        this.skinImage = new ImageIcon(Objects.requireNonNull(Character.class.getClassLoader().getResource("images/skins/Wizard.png")));
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
        this.skinImage = new ImageIcon(Objects.requireNonNull(Character.class.getClassLoader().getResource("images/skins/" + type + ".png")));
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

    public String getType() {
        return this.type;
    }

    public String getSkin() {
        return this.skin;
    }

    public ImageIcon getSkinImage() {
        return this.skinImage;
    }

    public void setSkinImage(ImageIcon skinImage) {
        this.skinImage = skinImage;
    }

    public String getLevel() {
        return this.level;
    }

    public String getCqc() { return this.cqc; }

    public String getFame() {
        return this.fame;
    }

    public String getExp() {
        return this.exp;
    }

    public String getPlace() {
        return this.place;
    }

    public String getStats() { return this.stats; }

    public Inventory getInventory() {
        return this.inventory;
    }
}
