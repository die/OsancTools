package com.github.waifu.entities;

import com.github.waifu.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

/**
 * Item class to store item data.
 */
public class Item {

    private final String name;
    private final String type;
    private String itemClass;
    private ImageIcon image;

    /**
     * Item method.
     *
     * Constructs a default Item.
     */
    public Item() {
        this.name = "";
        this.type = "";
        this.image = null;
    }

    /**
     * Item method.
     *
     * Constructs an Item.
     *
     * @param name name of the Item.
     * @param type type of Item (Weapon/Ability/Armor/Ring).
     * @param itemClass class that can use the Item (Wizard/etc).
     */
    public Item(String name, String type, String itemClass) {
        this.name = name.replace(":", "");
        this.type = type;
        this.itemClass = itemClass;
        createImage();
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return this.type;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    /**
     * createImage method.
     *
     * Constructs the appropriate image for the item using given information.
     */
    public void createImage() {
        if (Item.class.getClassLoader().getResource("images/items/" + name + ".png") != null) {
            image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/" + name + ".png")));
        } else if (Item.class.getClassLoader().getResource("images/items/" + name.replace("UT", "ST") + ".png") != null) {
            image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/" + name.replace("UT", "ST") + ".png")));
        } else {
            if (itemClass != null) {
                switch (type) {
                    case "weapon" -> setEmptyWeaponImage(itemClass);
                    case "ability" -> setEmptyAbilityImage(itemClass);
                    case "armor" -> setEmptyArmorImage(itemClass);
                    case "ring" -> setEmptyRingImage();
                }
            } else {
                switch (type) {
                    case "weapon" -> setEmptyWeaponImage("Wizard");
                    case "ability" -> setEmptyAbilityImage("Wizard");
                    case "armor" -> setEmptyArmorImage("Wizard");
                    case "ring" -> setEmptyRingImage();
                }
            }
        }
    }

    /**
     * setEmptyWeaponImage method.
     *
     * Sets the appropriate image for the item using given information.
     *
     * @param itemClass class that can use the Item
     */
    private void setEmptyWeaponImage(String itemClass) {
        /* add resources/ */
        switch (itemClass) {
            case "Rogue", "Assassin", "Trickster" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyDagger.png")));
            case "Archer", "Huntress", "Bard" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyBow.png")));
            case "Wizard", "Necromancer", "Mystic" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyStaff.png")));
            case "Priest", "Sorcerer", "Summoner" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyWand.png")));
            case "Warrior", "Knight", "Paladin" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptySword.png")));
            case "Ninja", "Samurai", "Kensei" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyKatana.png")));
        }
    }

    /**
     * setEmptyAbilityImage method.
     *
     * Sets the appropriate image for the item using given information.
     *
     * @param itemClass class that can use the Item
     */
    private void setEmptyAbilityImage(String itemClass) {
        image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/Empty" + itemClass + "Ability.png")));
    }

    /**
     * setEmptyArmorImage method.
     *
     * Sets the appropriate image for the item using given information.
     *
     * @param itemClass class that can use the Item
     */
    private void setEmptyArmorImage(String itemClass) {
        switch (itemClass) {
            case "Rogue", "Assassin", "Trickster", "Archer", "Huntress", "Ninja" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyLeatherArmor.png")));
            case "Wizard", "Necromancer", "Mystic", "Priest", "Sorcerer", "Summoner", "Bard" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyRobe.png")));
            case "Warrior", "Knight", "Paladin", "Samurai", "Kensei" -> image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyHeavyArmor.png")));
        }
    }

    /**
     * setEmptyRingImage method.
     *
     * Sets the appropriate image for the item, where all classes can use rings.
     */
    private void setEmptyRingImage() {
        image = new ImageIcon(Objects.requireNonNull(Item.class.getClassLoader().getResource("images/items/EmptyRing.png")));
    }

    public ImageIcon getImage() {
        return this.image;
    }

}
