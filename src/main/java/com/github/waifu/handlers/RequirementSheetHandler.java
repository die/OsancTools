package com.github.waifu.handlers;

import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Issue;
import com.github.waifu.entities.Item;
import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.gui.Gui;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * To be documented.
 */
public final class RequirementSheetHandler {

  /**
   * To be documented.
   */
  private static JSONObject requirementSheet;

  /**
   * To be documented.
   */
  private RequirementSheetHandler() {

  }

  /**
   * To be documented.
   *
   * @param inventory to be documented.
   */
  public static void parse(final Inventory inventory) {
    final List<Item> items = inventory.getItems();
    final Issue issue = inventory.getIssue();
    if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
      if (Gui.getRaid().isWebAppRaid()) {
        issue.setProblem(Problem.PRIVATE_PROFILE);
        issue.setMessage("");
        issue.setWhisper("");
      } else {
        issue.setProblem(Problem.EMPTY_SLOT);
        items.forEach(item -> item.setImage(Utilities.markImage(item.getImage(), Problem.EMPTY_SLOT.getColor())));
        issue.setMessage("");
        issue.setWhisper("");
      }
    } else {
      JSONObject requirementSheet = getRequirementSheet();

      final String metric = requirementSheet.getString("metric");

      switch (metric) {
        case "points" -> {
          baseParse(items, issue);
          int points = 0;
          for (final Item i : items) {
            final int calculatedPoints = calculatePoints(i, items);

            if (!i.getImage().getDescription().equals("marked")) {
              if (calculatedPoints > 0) {
                i.setImage(Utilities.markImage(i.getImage(), Color.CYAN));
                i.getImage().setDescription("marked");
              }
            }
            points += calculatedPoints;
          }

          final int required = requirementSheet.getJSONObject("required points").getInt(items.get(0).getItemClass());
          if (points < required) {
            issue.setProblem(Problem.POINTS);
          }
        }

        case "name" -> baseParse(items, issue);
        default -> throw new IllegalStateException("Unexpected value: " + metric);
      }
    }
  }

  /**
   * To be documented.
   *
   * @param items to be documented.
   * @param issue to be documented.
   */
  private static void baseParse(final List<Item> items, final Issue issue) {
    for (final Item i : items) {
      if (!RequirementSheetHandler.parseEmptySlot(i, issue)) {
        switch (i.getLabel()) {
          case "UT", "ST" -> {
            parseSwapout(i, issue);
            parseBannedItem(i, items, issue);
          }
          default -> RequirementSheetHandler.parseTier(i, issue);
        }
      }
    }
  }

  /**
   * To be documented.
   *
   * @param item To be documented.
   * @param items to be documented.
   * @return To be documented.
   */
  private static int calculatePoints(final Item item, final List<Item> items) {
    final JSONObject requirementSheet = getRequirementSheet();
    /*
      Excuse sets from calculations if the set is allowed.
     */
    if (item.getType().equals("weapon") && parseAllowedSTSet(item, items)) {
      return requirementSheet.getJSONObject("required points").getInt(item.getItemClass());
    } else {
      final JSONObject jsonObject = requirementSheet.getJSONObject("items");
      for (final String keys : jsonObject.keySet()) {
        final JSONObject pointObject = jsonObject.getJSONObject(keys);
        /*
          If an item name is found in the list, return the number of points of the JSONObject's name.
         */
        if (pointObject.has(item.getType()) && pointObject.getJSONArray(item.getType()).toList().contains(item.getNameWithoutLabel())) {
          return Integer.parseInt(keys);
        } else if (pointObject.has(item.getNameWithoutLabel())) {
          /*
            Objects with item names are for criteria where anything found with that item counts as a point.
           */
          return parseItemRequirements(pointObject.getJSONObject(item.getNameWithoutLabel()), item, items, Integer.parseInt(keys));
        } else {
          /*
            Handle objects that have specific combinations.
           */
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
                    case "weapon" -> items.get(InventorySlots.WEAPON.getIndex());
                    case "ability" -> items.get(InventorySlots.ABILITY.getIndex());
                    case "armor" -> items.get(InventorySlots.ARMOR.getIndex());
                    case "ring" -> items.get(InventorySlots.RING.getIndex());
                    default -> null;
                  };
                  if (item1 != null && !combination.getJSONArray("items").toList().contains(item1.getNameWithoutLabel())) {
                    return 0;
                  }
                }
                for (int i = 0; i < slots.length(); i++) {
                  final Item item1 = switch (slots.getString(i)) {
                    case "weapon" -> items.get(InventorySlots.WEAPON.getIndex());
                    case "ability" -> items.get(InventorySlots.ABILITY.getIndex());
                    case "armor" -> items.get(InventorySlots.ARMOR.getIndex());
                    case "ring" -> items.get(InventorySlots.RING.getIndex());
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
   * @param issue to be documented.
   * @return To be documented.
   */
  private static boolean parseEmptySlot(final Item item, final Issue issue) {
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
   * @param item to be documented.
   * @param issue to be documented.
   */
  private static void parseTier(final Item item, final Issue issue) {
    try {
      final String label = item.getLabel().substring(item.getLabel().length() - 2);
      final int tier;
      if (label.contains("T")) {
        tier = Integer.parseInt(label.substring(label.length() - 1));
      } else {
        tier = Integer.parseInt(label);
      }

      if (tier < getRequirementSheet().getJSONObject("tier").getInt(item.getType())) {
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
   * @param item To be documented.
   * @param issue to be documented.
   * @param items to be documented.
   * @return To be documented.
   */
  private static boolean parseBannedItem(final Item item, final List<Item> items, final Issue issue) {
    final JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet.has("bannedItems")) {
      final JSONObject bannedItems = requirementSheet.getJSONObject("bannedItems");
      final JSONArray itemList = bannedItems.getJSONArray(item.getType());

      boolean allowedStSet = true;
      if (itemList.toList().contains(item.getNameWithoutLabel())) {
        if (item.getLabel().equals("ST")) {
          allowedStSet = parseAllowedSTSet(item, items);
        } else {
          allowedStSet = false;
        }
      }

      if (!allowedStSet) {
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
   * @param items To be documented.
   * @return true if it is part of an allowed ST set that is equipped
   */
  private static boolean parseAllowedSTSet(final Item item, final List<Item> items) {
    JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet.has("allowedSTSets")) {
      final JSONObject allowedSts = requirementSheet.getJSONObject("allowedSTSets");

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
   * @param issue To be documented.
   * @return To be documented.
   */
  private static boolean parseSwapout(final Item item, final Issue issue) {
    final JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet.has("swapoutItems")) {
      final JSONArray swapouts = requirementSheet.getJSONArray("swapoutItems");
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

  private static int parseItemRequirements(final JSONObject itemRequirement, final Item item, final List<Item> items, final int points) {
    /*
      An item = points if any combination is found in any category.
      Used if an item = points if with a class(es) or item(s).
     */
    if (itemRequirement.has("class") && itemRequirement.getJSONArray("class").toList().contains(item.getItemClass())) {
      return points;
    }
    if (itemRequirement.has("weapon")) {
      if (itemRequirement.getJSONArray("weapon").toList().contains(items.get(InventorySlots.WEAPON.getIndex()))) {
        return points;
      }
    }
    if (itemRequirement.has("ability")) {
      if (itemRequirement.getJSONArray("ability").toList().contains(items.get(InventorySlots.ABILITY.getIndex()))) {
        return points;
      }
    }
    if (itemRequirement.has("armor")) {
      if (itemRequirement.getJSONArray("armor").toList().contains(items.get(InventorySlots.ARMOR.getIndex()))) {
        return points;
      }
    }
    if (itemRequirement.has("ring")) {
      if (itemRequirement.getJSONArray("ring").toList().contains(items.get(InventorySlots.RING.getIndex()))) {
        return points;
      }
    }
    return 0;
  }

  /**
   * Gets the requirement sheet.
   *
   * @return requirement sheet as a JSONObject.
   */
  public static JSONObject getRequirementSheet() {
    return requirementSheet;
  }

  /**
   * To be documented.
   *
   * @param sheet to be documented.
   */
  public static void setRequirementSheet(final JSONObject sheet) {
    requirementSheet = sheet;
  }
}
