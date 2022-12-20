package com.github.waifu.entities;

import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Inventory class to store inventory data.
 */
public class Inventory {

  private final List<Item> items;
  private final Issue issue;

  /**
   * Inventory method.
   *
   * <p>Constructs a default Inventory with only empty slots.
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
   * <p>Constructs an Inventory with all information.
   *
   * @param items list of items the Inventory has.
   */
  public Inventory(List<Item> items) {
    this.items = items;
    this.issue = new Issue(Problem.NONE);
  }

  /**
   * @return
   */
  public List<Item> getItems() {
    return this.items;
  }

  /**
   * @return
   */
  public Item getWeapon() {
    return this.items.get(0);
  }

  /**
   * @return
   */
  public Item getAbility() {
    return this.items.get(1);
  }

  /**
   * @return
   */
  public Item getArmor() {
    return this.items.get(2);
  }

  /**
   * @return
   */
  public Item getRing() {
    return this.items.get(3);
  }

  /**
   * @return
   */
  public Issue getIssue() {
    return this.issue;
  }

  /**
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
   * <p>Returns the Inventory after parsing each item.
   */
  public Inventory parseInventory() {
    if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
      issue.setProblem(Problem.PRIVATE_PROFILE);
      issue.setMessage("");
      issue.setWhisper("");
    } else {
      String metric = Utilities.json.getString("metric");

      switch (metric) {
        case "points" -> {
          int points = 0;

          for (Item i : items) {
            if (!parseEmptySlot(i)) {
              switch (i.getLabel()) {
                case "UT", "ST" -> {
                  checkSwapout(i);
                  checkBannedItem(i);
                }
                default -> {
                  checkTier(i);
                }
              }
            }
            int calculatedPoints = calculatePoints(i);

            if (!i.getImage().getDescription().equals("marked")) {
              if (calculatedPoints > 0) {
                i.setImage(Utilities.markImage(i.getImage(), Color.CYAN));
                i.getImage().setDescription("marked");
              }
            }
            points += calculatedPoints;
          }

          int required = Utilities.json.getJSONObject("required points").getInt(items.get(0).getItemClass());
          if (points < required) {
            issue.setProblem(Problem.POINTS);
          }
        }

        case "name" -> {
          for (Item i : items) {
            if (!parseEmptySlot(i)) {
              switch (i.getLabel()) {
                case "UT", "ST" -> {
                  checkSwapout(i);
                  checkBannedItem(i);
                }
                default -> {
                  checkTier(i);
                }
              }
            }
          }
        }
      }
    }
    return this;
  }

  /**
   * @param item
   * @return
   */
  private boolean parseEmptySlot(Item item) {
    if (item.getName().equals("Empty slot")) {
      issue.setProblem(Problem.EMPTY_SLOT);
      item.setImage(Utilities.markImage(item.getImage(), Problem.EMPTY_SLOT.getColor()));
      item.getImage().setDescription("marked");
      return true;
    } else {
      return false;
    }
  }

  /**
   * @param item
   * @return
   */
  private int calculatePoints(Item item) {

    int tier = item.getTier();
    if (tier == 11) {
      return -1;
    }

    if (item.getType().equals("weapon") && checkAllowedSTSet(item)) {
      return Utilities.json.getJSONObject("required points").getInt(item.getItemClass());
    } else {
      JSONObject jsonObject = Utilities.json.getJSONObject("items");

      for (String keys : jsonObject.keySet()) {
        JSONObject pointObject = jsonObject.getJSONObject(keys);
        if (pointObject.has(item.getType()) && pointObject.getJSONArray(item.getType()).toList().contains(item.getNameWithoutLabel())) {
          return Integer.parseInt(keys);
        } else {
          for (String k : pointObject.keySet()) {
            try {
              JSONObject combination = pointObject.getJSONObject(k);
              if (combination.getJSONArray("items").toList().contains(item.getNameWithoutLabel())) {
                for (int i = 0; i < items.indexOf(item); i++) {
                  if (combination.getJSONArray("items").toList().contains(items.get(i).getNameWithoutLabel())) {
                    return 0;
                  }
                }
                if (combination.has("class")) {
                  if (!combination.getJSONArray("class").toList().contains(item.getItemClass())) {
                    return 0;
                  }
                }
                JSONArray slots = combination.getJSONArray("slot");
                for (int i = 0; i < slots.length(); i++) {
                  Item item1 = switch (slots.getString(i)) {
                    case "weapon" -> getWeapon();
                    case "ability" -> getAbility();
                    case "armor" -> getArmor();
                    case "ring" -> getRing();
                    default -> null;
                  };
                  if (item1 != null && !combination.getJSONArray("items").toList().contains(item1.getNameWithoutLabel())) {
                    return 0;
                  }
                }
                for (int i = 0; i < slots.length(); i++) {
                  Item item1 = switch (slots.getString(i)) {
                    case "weapon" -> getWeapon();
                    case "ability" -> getAbility();
                    case "armor" -> getArmor();
                    case "ring" -> getRing();
                    default -> null;
                  };
                  item1.setImage(Utilities.markImage(item1.getImage(), Color.CYAN));
                  item1.getImage().setDescription("marked");
                }
                return Integer.parseInt(keys);
              }
            } catch (JSONException e) {
              //e.printStackTrace();
            }
          }
        }
      }
    }
    return 0;
  }

  /**
   * @return
   */
  private boolean checkBannedItem(Item item) {
    if (Utilities.json.has("bannedItems")) {
      JSONObject bannedItems = Utilities.json.getJSONObject("bannedItems");
      JSONArray itemList = bannedItems.getJSONArray(item.getType());

      boolean mark = false;
      if (itemList.toList().contains(item.getNameWithoutLabel())) {
        if (item.getLabel().equals("ST")) {
          mark = !checkAllowedSTSet(item);
        } else {
          mark = true;
        }
      }

      if (mark) {
        issue.setProblem(Problem.BANNED_ITEM);
        item.setImage(Utilities.markImage(item.getImage(), Problem.BANNED_ITEM.getColor()));
        item.getImage().setDescription("marked");
      }
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if a banned ST item is part of the allowed set.
   *
   * @param item
   * @return true if it is part of an allowed ST set that is equipped
   */
  public boolean checkAllowedSTSet(Item item) {
    if (Utilities.json.has("allowedSTSets")) {
      JSONObject allowedSts = Utilities.json.getJSONObject("allowedSTSets");

      for (String keys : allowedSts.keySet()) {
        JSONObject set = (JSONObject) allowedSts.get(keys);
        JSONArray stItems = set.getJSONArray("items");
        if (stItems.toList().contains(item.getNameWithoutLabel())) {
          int total = set.getInt("total");
          int count = 0;
          for (Item i : items) {
            if (stItems.toList().contains(i.getNameWithoutLabel())) {
              count++;
            }
          }
          if (count >= total) {
            return true;
          }
          break;
        }
      }
      return false;
    } else {
      return false;
    }
  }

  private boolean checkSwapout(Item item) {
    if (Utilities.json.has("swapoutItems")) {
      JSONArray swapouts = Utilities.json.getJSONArray("swapoutItems");
      if (swapouts.toList().contains(item.getNameWithoutLabel())) {
        issue.setProblem(Problem.SWAPOUT_ITEM);
        item.setImage(Utilities.markImage(item.getImage(), Problem.SWAPOUT_ITEM.getColor()));
        item.getImage().setDescription("marked");
        return true;
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  private void checkTier(Item item) {
    try {
      String label = item.getLabel().substring(item.getLabel().length() - 2);
      int tier;
      if (label.contains("T")) {
        tier = Integer.parseInt(label.substring(label.length() - 1));
      } else {
        tier = Integer.parseInt(label);
      }

      if (tier < Utilities.json.getJSONObject("tier").getInt(item.getType())) {
        issue.setProblem(Problem.UNDER_REQS);
        item.setImage(Utilities.markImage(item.getImage(), Problem.UNDER_REQS.getColor()));
        item.getImage().setDescription("marked");
      }
    } catch (Exception e) {
      e.printStackTrace();
      issue.setProblem(Problem.ERROR);
      item.setImage(Utilities.markImage(item.getImage(), Problem.ERROR.getColor()));
      item.getImage().setDescription("marked");
    }
  }

  /**
   * calculateDps method.
   *
   * <p>Returns a boolean if the Inventory meets a certain DPS requirement.
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
