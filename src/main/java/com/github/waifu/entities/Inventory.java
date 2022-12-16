package com.github.waifu.entities;

import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/**
 * Inventory class to store inventory data.
 */
public class Inventory {

    private final List<Item> items;
    private final Issue issue;

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

    /**
     *
     * @return
     */
    public List<Item> getItems() {
        return this.items;
    }

    /**
     *
     * @return
     */
    public Item getWeapon() {
        return this.items.get(0);
    }

    /**
     *
     * @return
     */
    public Item getAbility() {
        return this.items.get(1);
    }

    /**
     *
     * @return
     */
    public Item getArmor() {
        return this.items.get(2);
    }

    /**
     *
     * @return
     */
    public Item getRing() {
        return this.items.get(3);
    }

    /**
     *
     * @return
     */
    public Issue getIssue() {
        return this.issue;
    }

    /**
     *
     * @return
     */
    public String printInventory() {
        StringJoiner stringJoiner = new StringJoiner(" | ");
        for (Item item : items) {
            stringJoiner.add(item.getName());
        }
        return stringJoiner.toString();
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
                    i.setImage(Utilities.markImage(i.getImage(), Problem.EMPTY_SLOT.getColor()));
                } else if (name.endsWith("UT") || name.endsWith("ST")) {
                    JSONObject bannedItems = Utilities.json.getJSONObject("bannedItems");
                    JSONArray jsonArray = bannedItems.getJSONArray(i.getType());
                    JSONArray swapouts = Utilities.json.getJSONArray("swapoutItems");
                    String substring = name.substring(0, name.length() - 3);
                    boolean foundSTSet = false;
                    if (jsonArray.toList().contains(substring)) {
                        JSONObject allowedSts = Utilities.json.getJSONObject("allowedSTSets");
                        for (String keys : allowedSts.keySet()) {
                            JSONObject set = (JSONObject) allowedSts.get(keys);
                            JSONArray stItems = set.getJSONArray("items");
                            if (stItems.toList().contains(substring)) {
                                foundSTSet = true;
                                int total = set.getInt("total");
                                int count = 0;
                                for (Item item : items) {
                                    String substring1 = item.getName().substring(0, item.getName().length() - 3);
                                    if (stItems.toList().contains(substring1)) {
                                        count++;
                                    }
                                }
                                if (count < total) {
                                    issue.setProblem(Problem.BANNED_ITEM);
                                    i.setImage(Utilities.markImage(i.getImage(), Problem.BANNED_ITEM.getColor()));
                                }
                                break;
                            }
                        }
                        if (foundSTSet) {
                            break;
                        } else {
                            issue.setProblem(Problem.BANNED_ITEM);
                            issue.setMessage(name);
                            issue.setWhisper(name);
                            i.setImage(Utilities.markImage(i.getImage(), Problem.BANNED_ITEM.getColor()));
                        }
                    } else if (swapouts.toList().contains(substring)) {
                        issue.setProblem(Problem.SWAPOUT_ITEM);
                        issue.setMessage(name);
                        issue.setWhisper(name);
                        i.setImage(Utilities.markImage(i.getImage(), Problem.SWAPOUT_ITEM.getColor()));
                    }
                } else {
                    try {
                        int item;
                        String tier = name.substring(name.length() - 2);
                        if (tier.contains("T")) {
                            item = Integer.parseInt(name.substring(name.length() - 1));
                        } else {
                            item = Integer.parseInt(tier);
                        }

                        if (item < Utilities.json.getInt(type)) {
                            issue.setProblem(Problem.UNDER_REQS);
                            issue.setMessage(name);
                            issue.setWhisper(name);
                            i.setImage(Utilities.markImage(i.getImage(), Problem.UNDER_REQS.getColor()));
                        }
                    } catch (Exception e) {
                        issue.setProblem(Problem.ERROR);
                        issue.setMessage(name);
                        issue.setWhisper(name);
                        i.setImage(Utilities.markImage(i.getImage(), Problem.ERROR.getColor()));
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
        JSONObject dpsItems = Utilities.json.getJSONObject("reacts").getJSONObject("dpsItems");
        int count = 0;
        for (Item s : items) {
            JSONArray jsonArray = dpsItems.getJSONArray(s.getType());
            String name = "";
            if (s.getName().charAt(s.getName().length() - 4) == ' ') {
                name = s.getName().substring(0, s.getName().length() - 4);
            } else {
                name = s.getName().substring(0, s.getName().length() - 3);
            }
            if (jsonArray.toList().contains(name)) {
                s.setImage(Utilities.markImage(s.getImage(), Problem.NONE.getColor()));
                count++;
            } else {
                s.setImage(Utilities.markImage(s.getImage(), Problem.MISSING_REACT_DPS.getColor()));
            }
        }
        JSONArray exaltedSkins = (JSONArray) Utilities.json.get("exaltedSkins");
        if (exaltedSkins.toList().contains(skin)) {
            count++;
        }
        return count >= dps;
    }

    /**
     *
     * @param w
     * @param h
     * @return
     */
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
