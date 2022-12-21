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

  /**
   * To be documented.
   */
  private final List<Item> items;
  /**
   * To be documented.
   */
  private final Issue issue;

  /**
   * Inventory method.
   *
   * <p>Constructs a default Inventory with only empty slots.
   */
  public Inventory() {
    final List<Item> empty = new ArrayList<>();
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
  public Inventory(final List<Item> items) {
    this.items = items;
    this.issue = new Issue(Problem.NONE);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<Item> getItems() {
    return this.items;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getWeapon() {
    return this.items.get(0);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getAbility() {
    return this.items.get(1);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getArmor() {
    return this.items.get(2);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getRing() {
    return this.items.get(3);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Issue getIssue() {
    return this.issue;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String printInventory() {
    final StringJoiner stringJoiner = new StringJoiner(" | ");
    for (final Item item : items) {
      stringJoiner.add(item.getName());
    }
    return stringJoiner.toString();
  }

  /**
   * parseInventory method.
   *
   * <p>Returns the Inventory after parsing each item.
   *
   * @return To be documented.
   */
  public Inventory parseInventory() {
    if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
      issue.setProblem(Problem.PRIVATE_PROFILE);
      issue.setMessage("");
      issue.setWhisper("");
    } else {
      final String metric = Utilities.getJson().getString("metric");

      switch (metric) {
        case "points" -> {
          int points = 0;

          for (final Item i : items) {
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
            final int calculatedPoints = calculatePoints(i);

            if (!i.getImage().getDescription().equals("marked")) {
              if (calculatedPoints > 0) {
                i.setImage(Utilities.markImage(i.getImage(), Color.CYAN));
                i.getImage().setDescription("marked");
              }
            }
            points += calculatedPoints;
          }

          final int required = Utilities.getJson().getJSONObject("required points").getInt(items.get(0).getItemClass());
          if (points < required) {
            issue.setProblem(Problem.POINTS);
          }
        }

        case "name" -> {
          for (final Item i : items) {
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
        default -> {
          return this;
        }
      }
    }
    return this;
  }

  /**
   * To be documented.
   *
   * @param item To be documented.
   * @return To be documented.
   */
  private boolean parseEmptySlot(final Item item) {
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
   * To be documented.
   *
   * @param item To be documented.
   * @return To be documented.
   */
  private int calculatePoints(final Item item) {

    final int tier = item.getTier();
    if (tier == 11) {
      return -1;
    }

    if (item.getType().equals("weapon") && checkAllowedSTSet(item)) {
      return Utilities.getJson().getJSONObject("required points").getInt(item.getItemClass());
    } else {
      final JSONObject jsonObject = Utilities.getJson().getJSONObject("items");

      for (final String keys : jsonObject.keySet()) {
        final JSONObject pointObject = jsonObject.getJSONObject(keys);
        if (pointObject.has(item.getType()) && pointObject.getJSONArray(item.getType()).toList().contains(item.getNameWithoutLabel())) {
          return Integer.parseInt(keys);
        } else {
          for (final String k : pointObject.keySet()) {
            try {
              final JSONObject combination = pointObject.getJSONObject(k);
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
                final JSONArray slots = combination.getJSONArray("slot");
                for (int i = 0; i < slots.length(); i++) {
                  final Item item1 = switch (slots.getString(i)) {
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
                  final Item item1 = switch (slots.getString(i)) {
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
            } catch (final JSONException e) {
              //e.printStackTrace();
            }
          }
        }
      }
    }
    return 0;
  }

  /**
   * To be documented.
   *
   * @param item To be documented.
   * @return To be documented.
   */
  private boolean checkBannedItem(final Item item) {
    if (Utilities.getJson().has("bannedItems")) {
      final JSONObject bannedItems = Utilities.getJson().getJSONObject("bannedItems");
      final JSONArray itemList = bannedItems.getJSONArray(item.getType());

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
   * @param item To be documented.
   * @return true if it is part of an allowed ST set that is equipped
   */
  public boolean checkAllowedSTSet(final Item item) {
    if (Utilities.getJson().has("allowedSTSets")) {
      final JSONObject allowedSts = Utilities.getJson().getJSONObject("allowedSTSets");

      for (final String keys : allowedSts.keySet()) {
        final JSONObject set = (JSONObject) allowedSts.get(keys);
        final JSONArray stItems = set.getJSONArray("items");
        if (stItems.toList().contains(item.getNameWithoutLabel())) {
          final int total = set.getInt("total");
          int count = 0;
          for (final Item i : items) {
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

  /**
   * To be documented.
   *
   * @param item To be documented.
   * @return To be documented.
   */
  private boolean checkSwapout(final Item item) {
    if (Utilities.getJson().has("swapoutItems")) {
      final JSONArray swapouts = Utilities.getJson().getJSONArray("swapoutItems");
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

  /**
   * To be documented.
   *
   * @param item To be documented.
   */
  private void checkTier(final Item item) {
    try {
      final String label = item.getLabel().substring(item.getLabel().length() - 2);
      final int tier;
      if (label.contains("T")) {
        tier = Integer.parseInt(label.substring(label.length() - 1));
      } else {
        tier = Integer.parseInt(label);
      }

      if (tier < Utilities.getJson().getJSONObject("tier").getInt(item.getType())) {
        issue.setProblem(Problem.UNDER_REQS);
        item.setImage(Utilities.markImage(item.getImage(), Problem.UNDER_REQS.getColor()));
        item.getImage().setDescription("marked");
      }
    } catch (final Exception e) {
      e.printStackTrace();
      issue.setProblem(Problem.ERROR);
      item.setImage(Utilities.markImage(item.getImage(), Problem.ERROR.getColor()));
      item.getImage().setDescription("marked");
    }
  }

  /**
   * To be documented.
   *
   * @param skin To be documented.
   * @param dps To be documented.
   * @return To be documented.
   */
  public boolean calculateDps(final String skin, final int dps) {
    final JSONObject dpsItems = Utilities.getJson().getJSONObject("reacts").getJSONObject("dpsItems");
    int count = 0;
    for (final Item s : items) {
      final JSONArray jsonArray = dpsItems.getJSONArray(s.getType());
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
    final JSONArray exaltedSkins = (JSONArray) Utilities.getJson().get("exaltedSkins");
    if (exaltedSkins.toList().contains(skin)) {
      count++;
    }
    return count >= dps;
  }

  /**
   * To be documented.
   *
   * @param w To be documented.
   * @param h To be documented.
   * @return To be documented.
   */
  public ImageIcon createImage(final int w, final int h) {
    final ImageIcon weapon = new ImageIcon(getItems().get(InventorySlots.WEAPON.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    final ImageIcon ability = new ImageIcon(getItems().get(InventorySlots.ABILITY.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    final ImageIcon armor = new ImageIcon(getItems().get(InventorySlots.ARMOR.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    final ImageIcon ring = new ImageIcon(getItems().get(InventorySlots.RING.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH));
    final int inventoryWidth = weapon.getIconWidth() + ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth();
    final int inventoryHeight = weapon.getIconHeight();
    final BufferedImage combined = new BufferedImage(inventoryWidth, inventoryHeight, BufferedImage.TYPE_INT_ARGB);
    final Graphics g = combined.getGraphics();
    g.drawImage(weapon.getImage(), 0, 0, null);
    g.drawImage(ability.getImage(), ability.getIconWidth(), 0, null);
    g.drawImage(armor.getImage(), ability.getIconWidth() + armor.getIconWidth(), 0, null);
    g.drawImage(ring.getImage(), ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth(), 0, null);
    return new ImageIcon(combined);
  }
}
