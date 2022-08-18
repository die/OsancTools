package com.github.waifu.entities;

import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.util.Utilities;
import org.json.JSONArray;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class to store inventory data.
 */
public class Inventory {

    private final List<Item> items;
    private final Issue issue;
    private String message;

    /**
     * Inventory method.
     *
     * Constructs a default Inventory with only empty slots.
     */
    public Inventory() {
        List<Item> empty = new ArrayList<>();
        empty.add(new Item("Empty slot", "weapon", "Wizard"));
        empty.add(new Item("Empty slot", "ability", "Wizard"));
        empty.add(new Item("Empty slot", "armor", "Wizard"));
        empty.add(new Item("Empty slot", "ring", "Wizard"));

        issue = new Issue(Problem.NONE);
        this.items = empty;
    }

    /**
     * Inventory method.
     *
     * Constructs an Inventory with all information.
     *
     * @param items list of items the Inventory has.
     */
    public Inventory(List<Item> items) {
        this.items = items;
        this.issue = new Issue(Problem.NONE);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public Item getAbility() { return this.items.get(1); }

    public Issue getIssue() {
        return this.issue;
    }
    /**
     * parseInventory method.
     *
     * Returns the Inventory after parsing each item.
     */
    public Inventory parseInventory() {

        if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
            issue.setProblem(Problem.PRIVATE_PROFILE);
            issue.setMessage("");
            issue.setWhisper("");
        } else {
            for (Item i : items) {
                String name = i.getName();
                String type = i.getType();
                if (name.equals("Empty slot")) {
                    issue.setProblem(Problem.EMPTY_SLOT);
                    issue.setMessage("");
                    issue.setWhisper(name);
                } else if (name.endsWith("UT") || name.endsWith("ST")) {
                    JSONArray banned = Utilities.json.getJSONArray("banned");
                    JSONArray swapouts = Utilities.json.getJSONArray("swapouts");
                    if (banned.toList().contains(name)) {
                        issue.setProblem(Problem.BANNED_ITEM);
                        issue.setMessage(name);
                        issue.setWhisper(name);
                        i.setImage(Utilities.markImage(i.getImage(), Problem.BANNED_ITEM.getColor()));
                    } else if (swapouts.toList().contains(name)) {
                        issue.setProblem(Problem.SWAPOUT_ITEM);
                        issue.setMessage(name);
                        issue.setWhisper(name);
                        i.setImage(Utilities.markImage(i.getImage(), Problem.SWAPOUT_ITEM.getColor()));
                    }
                } else {
                    int item;
                    String tier = name.substring(name.length() - 2);
                    if (tier.contains("T")) {
                        item = Integer.parseInt(name.substring(name.length() - 1));
                    } else {
                        item = Integer.parseInt(tier);
                    }
                    if (type.equals("ability") || type.equals("ring")) {
                        if (item < 4) {
                            issue.setProblem(Problem.UNDER_REQS);
                            issue.setMessage(name);
                            issue.setWhisper(name);
                            i.setImage(Utilities.markImage(i.getImage(), Problem.UNDER_REQS.getColor()));
                        }
                    } else if (type.equals("weapon") || type.equals("armor")) {
                        if (item < 12) {
                            issue.setProblem(Problem.UNDER_REQS);
                            issue.setMessage(name);
                            issue.setWhisper(name);
                            i.setImage(Utilities.markImage(i.getImage(), Problem.UNDER_REQS.getColor()));
                        }
                    }
                }
            }
        }
        return this;
    }

    /**
     * calculateDps method.
     *
     * Returns a boolean if the Inventory meets a certain DPS requirement.
     */
    public boolean calculateDps(String skin, int dps) {
        JSONArray dpsItems = (JSONArray) Utilities.json.get("dps");
        int count = 0;
        for (Item s : items) {
            if (dpsItems.toList().contains(s.getName())) {
                s.setImage(Utilities.markImage(s.getImage(), Problem.NONE.getColor()));
                count++;
            }
        }
        JSONArray exaltedSkins = (JSONArray) Utilities.json.get("exaltedSkins");
        if (exaltedSkins.toList().contains(skin)) {
            count++;
        }
        return count >= dps;
    }

    public ImageIcon createImage(int w, int h) {
        ImageIcon weapon = new ImageIcon(getItems().get(InventorySlots.WEAPON.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        ImageIcon ability = new ImageIcon(getItems().get(InventorySlots.ABILITY.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        ImageIcon armor = new ImageIcon(getItems().get(InventorySlots.ARMOR.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        ImageIcon ring = new ImageIcon(getItems().get(InventorySlots.RING.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
        int inventoryWidth = weapon.getIconWidth() + ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth();
        int inventoryHeight = weapon.getIconHeight();
        BufferedImage combined = new BufferedImage(inventoryWidth, inventoryHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics g = combined.getGraphics();
        g.drawImage(weapon.getImage(), 0, 0, null);
        g.drawImage(ability.getImage(), ability.getIconWidth(), 0, null);
        g.drawImage(armor.getImage(), ability.getIconWidth() + armor.getIconWidth(), 0, null);
        g.drawImage(ring.getImage(), ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth(), 0, null);
        return new ImageIcon(combined);
    }
}
