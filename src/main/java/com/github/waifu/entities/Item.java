package com.github.waifu.entities;

import com.github.waifu.util.Utilities;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    /**
     * Item method.
     *
     * Constructs an Item.
     *
     * @param name name of the Item.
     * @param type type of Item (Weapon/Ability/Armor/Ring).
     * @param itemClass class that can use the Item (Wizard/etc).
     */
    public Item(String name, String type, String itemClass, int x, int y) throws IOException {
        this.name = name.replace(":", "");
        this.type = type;
        this.itemClass = itemClass;
        BufferedImage image = ImageIO.read(Utilities.getImageResource("images/items/renders.png"));
        BufferedImage image1 = image.getSubimage(x, y, 46, 46);
        System.out.println("obtained image");
        this.image = new ImageIcon(image1);
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the item's label (UT/ST/T1/T12)
     * @return
     */
    public String getLabel() {
        String label = name.substring(name.length() - 2);
        if (label.contains("T")) {
            return label;
        } else {
            return name.substring(name.length() - 3);
        }
    }

    /**
     *
     * @return
     */
    public int getTier() {
        if (getLabel().contains("UT") || getLabel().contains("ST") || getName().equals("Empty slot")) return -1;

        String label = getLabel().substring(getLabel().length() - 2);
        int tier = -1;
        if (label.contains("T")) {
            tier = Integer.parseInt(label.substring(label.length() - 1));
        } else {
            tier = Integer.parseInt(label);
        }
        return tier;
    }

    /**
     *
     * @return
     */
    public String getNameWithoutLabel() {
        return name.replace(" " + getLabel(), "");
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
    public String getItemClass() {
       return itemClass;
    }

    /**
     *
     * @param image
     */
    public void setImage(ImageIcon image) {
        this.image = image;
    }

    /**
     * createImage method.
     *
     * Constructs the appropriate image for the item using given information.
     */
    public void createImage() {
        if (Utilities.getImageResource("images/items/" + name + ".png") != null) {
            image = new ImageIcon(Utilities.getImageResource("images/items/" + name + ".png"));
        } else if (Utilities.getImageResource("images/items/" + name.replace("UT", "ST") + ".png") != null) {
            image = new ImageIcon(Utilities.getImageResource("images/items/" + name.replace("UT", "ST") + ".png"));
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
        switch (itemClass) {
            case "Rogue", "Assassin", "Trickster" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyDagger.png"));
            case "Archer", "Huntress", "Bard" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyBow.png"));
            case "Wizard", "Necromancer", "Mystic" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyStaff.png"));
            case "Priest", "Sorcerer", "Summoner" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyWand.png"));
            case "Warrior", "Knight", "Paladin" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptySword.png"));
            case "Ninja", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyKatana.png"));
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
        image = new ImageIcon(Utilities.getImageResource("images/items/Empty" + itemClass + "Ability.png"));
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
            case "Rogue", "Assassin", "Trickster", "Archer", "Huntress", "Ninja" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyLeatherArmor.png"));
            case "Wizard", "Necromancer", "Mystic", "Priest", "Sorcerer", "Summoner", "Bard" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyRobe.png"));
            case "Warrior", "Knight", "Paladin", "Samurai", "Kensei" -> image = new ImageIcon(Utilities.getImageResource("images/items/EmptyHeavyArmor.png"));
        }
    }

    /**
     * setEmptyRingImage method.
     *
     * Sets the appropriate image for the item, where all classes can use rings.
     */
    private void setEmptyRingImage() {
        image = new ImageIcon(Utilities.getImageResource("images/items/EmptyRing.png"));
    }

    /**
     *
     * @return
     */
    public ImageIcon getImage() {
        return this.image;
    }

}
