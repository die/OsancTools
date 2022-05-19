package com.github.waifu.entities;

import javax.swing.*;
import java.util.Objects;

/**
 * Character class to store character data.
 */
public class Character {

    private final String type;
    private final long skin;
    private ImageIcon skinImage;
    private final int level;
    private final String cqc;
    private final long fame;
    private final long exp;
    private final int place;
    private final String stats;
    private final Inventory inventory;

    /**
     * Character method.
     *
     * Constructs a default Character as a level 0 Wizard.
     */
    public Character() {
        this.type = "Wizard";
        this.skin = 0;
        this.skinImage = new ImageIcon(Objects.requireNonNull(Character.class.getClassLoader().getResource("resources/skins/Wizard.png")));
        this.level = 0;
        this.cqc = "";
        this.fame = 0;
        this.exp = 0;
        this.place = 0;
        this.stats = "";
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
    public Character(String type, long skin, int level, String cqc, long fame, long exp, int place, String stats, Inventory inventory) {
        this.type = type;
        this.skin = skin;
        this.skinImage = new ImageIcon(Objects.requireNonNull(Character.class.getClassLoader().getResource("resources/skins/" + type + ".png")));
        this.level = level;
        this.cqc = cqc;
        this.fame = fame;
        this.exp = exp;
        this.place = place;
        this.stats = stats;
        this.inventory = inventory;
    }

    public String getType() {
        return this.type;
    }

    public long getSkin() {
        return this.skin;
    }

    public ImageIcon getSkinImage() {
        return this.skinImage;
    }

    public void setSkinImage(ImageIcon skinImage) {
        this.skinImage = skinImage;
    }

    public int getLevel() {
        return this.level;
    }

    public String getCqc() { return this.cqc; }

    public long getFame() {
        return this.fame;
    }

    public long getExp() {
        return this.exp;
    }

    public int getPlace() {
        return this.place;
    }

    public String getStats() { return this.stats; }

    public Inventory getInventory() {
        return this.inventory;
    }
}
