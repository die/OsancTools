package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import org.json.simple.JSONArray;
import java.util.ArrayList;
import java.util.List;

/**
 * Inventory class to store inventory data.
 */
public class Inventory {

    private final List<Item> items;
    private String problem;
    private String message;
    private int level;

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

        this.items = empty;
        this.problem = "None";
        this.level = 0;
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
        this.problem = "None";
        this.level = 0;
    }

    public String getProblem() {
        return this.problem;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public Item getAbility() { return this.items.get(1); }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    /**
     * parseInventory method.
     *
     * Returns the Inventory after parsing each item.
     */
    public Inventory parseInventory() {

        if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
            setProblem("Private Profile", 3);
        } else {
            for (Item i : items) {
                String name = i.getName();
                String type = i.getType();
                if (name.equals("Empty slot")) {
                    setProblem("Empty slot", 1);
                } else if (name.endsWith("UT") || name.endsWith("ST")) {
                    JSONArray banned = (JSONArray) Utilities.json.get("banned");
                    JSONArray swapouts = (JSONArray) Utilities.json.get("swapouts");
                    if (banned.contains(name)) {
                        setProblem("Banned item", 2);
                        i.setParseSetImage("issue");
                    } else if (swapouts.contains(name)) {
                        setProblem("Swapout Item", 1);
                        i.setParseSetImage("warning");
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
                            setProblem("Under Reqs", 3);
                            i.setParseSetImage("issue");
                        }
                    } else if (type.equals("weapon") || type.equals("armor")) {
                        if (item < 12) {
                            setProblem("Under Reqs", 3);
                            i.setParseSetImage("issue");
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
    public boolean calculateDps(Long skin, int dps) {
        JSONArray dpsItems = (JSONArray) Utilities.json.get("dps");
        int count = 0;
        for (Item s : items) {
            if (dpsItems.contains(s.getName())) {
                s.setImage(Utilities.markImage(s.getImage(), "good"));
                count++;
            }
        }
        JSONArray exaltedSkins = (JSONArray) Utilities.json.get("exaltedSkins");
        if (exaltedSkins.contains(skin)) {
            count++;
        }
        return count >= dps;
    }

    /**
     * setProblem method.
     *
     * Sets a problem if the level is higher.
     * If multiple problems exist, the one with the highest level is reported.
     *
     * @param problem contains the type of problem (Banned item/Under reqs/Swapout/etc)
     * @param level contains the level of the problem.
     */
    public void setProblem(String problem, int level) {
        if (this.problem != null) {
            if (this.level <= level) {
                this.problem = problem;
                this.level = level;
            }
        }
    }
}
