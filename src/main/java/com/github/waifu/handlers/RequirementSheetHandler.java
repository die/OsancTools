package com.github.waifu.handlers;

import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Issue;
import com.github.waifu.entities.Item;
import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.enums.Stat;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.net.URL;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

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
  public static void parse(final Inventory inventory, final String name) {
    final List<Item> items = inventory.getItems();
    final Issue issue = inventory.getIssue();
    if (items.stream().allMatch(item -> item.getName().equals("Empty slot"))) {
      issue.setProblem(Problem.EMPTY_SLOT);
      items.forEach(item -> issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.EMPTY_SLOT.getColor()));
      issue.setMessage("");
      issue.setWhisper("");
    } else {
      final JSONObject requirementSheet = getRequirementSheet();
      if (requirementSheet == null) return;
      final String metric = requirementSheet.getString("metric");

      switch (metric) {
        case "points" -> {
          baseParse(items, issue, name);
          int points = 0;
          for (final Item i : items) {
            final int calculatedPoints = calculatePoints(issue, i, items);

            if (calculatedPoints > 0) {
              issue.mark(InventorySlots.getEnumByType(i.getType()).getIndex(), Color.CYAN);
              i.getImage().setDescription("marked");
            }
            points += calculatedPoints;
          }

          final int required = requirementSheet.getJSONObject("required points").getInt(items.get(0).getItemClass());
          if (points < required) {
            issue.setProblem(Problem.POINTS);
          }
        }

        case "name" -> baseParse(items, issue, name);
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
  private static void baseParse(final List<Item> items, final Issue issue, final String name) {
    for (final Item i : items) {
      if (!RequirementSheetHandler.parseEmptySlot(i, issue)) {
        switch (i.getLabel()) {
          case "UT", "ST" -> {
            parseSwapout(i, issue, name);
            parseBannedItem(i, items, issue, name);
          }
          default -> RequirementSheetHandler.parseTier(i, issue);
        }
      }
    }
  }

  /**
   * Parses maxed stats.
   *
   * @param character character object.
   */
  public static void parseMaxedStats(final Character character) {
    if (character.getCharacterStats() == null || requirementSheet == null) return;
    if (requirementSheet.has("maxedStats")) {
      final String metric = requirementSheet.getJSONObject("maxedStats").getString("metric");
      final JSONArray maxedStats = requirementSheet.getJSONObject("maxedStats").getJSONArray("stats");
      if (metric.equals("any")) {
        final Integer[] maxedStatIndices = character.getCharacterStats().getMaxedStatIndices(character.getType());

        boolean found = false;
        for (int i = 0; i < maxedStats.length(); i++) {
          final Stat stat = Stat.getStatByName(maxedStats.getString(i).toUpperCase());
          assert stat != null;
          if (maxedStatIndices[stat.getIndex()] == 1) {
            found = true;
          }

          if (!found) {
            character.getInventory().getIssue().setProblem(Problem.NOT_MAXED);
            character.getCharacterStats().addProblemStat(stat.getIndex());
          }
        }
      } else if (metric.equals("required")) {
        final Integer[] maxedStatIndices = character.getCharacterStats().getMaxedStatIndices(character.getType());

        for (int i = 0; i < maxedStats.length(); i++) {
          final Stat stat = Stat.getStatByName(maxedStats.getString(i).toUpperCase());
          assert stat != null;
          if (maxedStatIndices[stat.getIndex()] != 1) {
            character.getInventory().getIssue().setProblem(Problem.NOT_MAXED);
            character.getCharacterStats().addProblemStat(stat.getIndex());
          }
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
  private static int calculatePoints(final Issue issue, final Item item, final List<Item> items) {
    final JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet == null) return 0;
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
                  if (item1 != null) {
                    issue.mark(InventorySlots.getEnumByType(item1.getType()).getIndex(), Color.CYAN);
                    item1.getImage().setDescription("marked");
                  }
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
      issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.EMPTY_SLOT.getColor());
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
        issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.UNDER_REQS.getColor());
        item.getImage().setDescription("marked");
      }
    } catch (final Exception e) {
      e.printStackTrace();
      issue.setProblem(Problem.ERROR);
      issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.ERROR.getColor());
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
  public static boolean parseBannedItem(final Item item, final List<Item> items, final Issue issue, final String name) {
    final JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet == null) return false;
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
        issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.BANNED_ITEM.getColor());
        issue.setWhisper("/t " + name + " " + item.getNameWithoutLabel() + " is a banned item. Do you have another item to swap to?");
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
  public static boolean parseAllowedSTSet(final Item item, final List<Item> items) {
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
  public static boolean parseSwapout(final Item item, final Issue issue, final String name) {
    final JSONObject requirementSheet = getRequirementSheet();
    if (requirementSheet == null) return false;
    if (requirementSheet.has("swapoutItems")) {
      final JSONArray swapouts = requirementSheet.getJSONArray("swapoutItems");
      if (swapouts.toList().contains(item.getNameWithoutLabel())) {
        issue.setProblem(Problem.SWAPOUT_ITEM);
        issue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Problem.SWAPOUT_ITEM.getColor());
        item.getImage().setDescription("marked");
        issue.setWhisper("/t " + name + " " + item.getNameWithoutLabel() + " is a swapout item. Do you have another item that meets reqs? You can swap after I confirm.");
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
   * Sets the current requirement sheet.
   *
   * @param o can be of type String or JSONObject. If o is a path String, a JSONObject
   *          is constructed by loading a requirement sheet in the class path.
   */
  public static void setRequirementSheet(final Object o) {
    if (o instanceof String) {
      try {
        final String path = (String) o;
        final URL url = Utilities.getClassResource(path);
        final JSONTokener tokener = new JSONTokener(url.openStream());
        requirementSheet = new JSONObject(tokener);
      } catch (final Exception exception) {
        exception.printStackTrace();
      }
    } else if (o instanceof JSONObject) {
      requirementSheet = (JSONObject) o;
    }
  }
}
