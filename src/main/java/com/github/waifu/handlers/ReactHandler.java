package com.github.waifu.handlers;

import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Issue;
import com.github.waifu.entities.Item;
import com.github.waifu.entities.React;
import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import java.awt.Color;
import org.json.JSONArray;
import org.json.JSONObject;

public class ReactHandler {

  public static void parseReact(final String name, final React react, final Character character) {
    final JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    if (!requirementSheet.has("reacts")) return;

    final String reactName = react.getName();
    switch (reactName) {
      case "Trickster", "Mystic" -> parseClass(name, reactName, character.getType(), character.getInventory());
      case "Marble Seal", "Armor Break", "Fungal Tome" -> parseItem(name, react, character);
      case "2/4+ Ranged DPS" -> parseDps(name, 2, character.getSkin(), character);
      case "3/4+ Ranged DPS" -> parseDps(name, 3, character.getSkin(), character);
      default -> character.getInventory().getReactIssue().setWhisper("/lock " + name);
    }
  }


  /**
   * To be documented.
   *
   * @param skin To be documented.
   * @param dps To be documented.
   * @return To be documented.
   */
  private static void parseDps(final String name, final int dps, final int skin, final Character character) {
    JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    if (!requirementSheet.has("reacts")) return;
    final Inventory inventory = character.getInventory();
    final Issue reactIssue = inventory.getReactIssue();
    final JSONObject dpsItems = requirementSheet.getJSONObject("reacts").getJSONObject("dpsItems");
    int count = 0;
    for (final Item item : inventory.getItems()) {
      final JSONArray jsonArray = dpsItems.getJSONArray(item.getType());
      final String itemName;
      if (item.getName().charAt(item.getName().length() - 4) == ' ') {
        itemName = item.getName().substring(0, item.getName().length() - 4);
      } else {
        itemName = item.getName().substring(0, item.getName().length() - 3);
      }
      if (jsonArray.toList().contains(itemName)) {
        reactIssue.mark(InventorySlots.getEnumByType(item.getType()).getIndex(), Color.cyan);
        count++;
      }
    }
    final JSONArray exaltedSkins = (JSONArray) requirementSheet.get("exaltedSkins");
    if (exaltedSkins.toList().contains(skin)) {
      count++;
      reactIssue.mark(InventorySlots.SKIN.getIndex(), Color.cyan);
    }

    if (count < dps) {
      reactIssue.setProblem(Problem.MISSING_REACT_DPS);
      reactIssue.setWhisper("/t " + name + " Please equip your " + dps + "/4+ set so I can confirm it. You can swap it out after.");
    }
  }

  /**
   * parseClass method.
   *
   * <p>Checks if the inventory meets the required Class and T6/T7 Item.
   */
  private static void parseClass(final String name, final String reactName, final String type, final Inventory inventory) {
    final JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    final Issue reactIssue = inventory.getReactIssue();
    if (reactName.equals(type)) {
      reactIssue.mark(InventorySlots.SKIN.getIndex(), Problem.NONE.getColor());
      reactIssue.setProblem(Problem.NONE);
      reactIssue.setWhisper("None");
      //if (requirement.contains("T6")) {
        if (inventory.getAbility().getName().equals("Empty slot") || inventory.getItems().get(1).getName().endsWith(" ST")) {
          reactIssue.setWhisper("/t " + name + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor());
        } else if (inventory.getItems().get(1).getName().endsWith("UT")) {
          final JSONArray reskins = requirementSheet.getJSONObject("allowedSTSets").getJSONObject("Oryxmas").getJSONArray("items");
          final String ability = inventory.getAbility().getName();
          if (reskins.toList().contains(ability.substring(0, ability.length() - 3))) {
            reactIssue.setWhisper("None");
            reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.NONE.getColor());
          } else {
            reactIssue.setWhisper("/t " + name + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
            reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor());
          }
        } else {
          final String ability = inventory.getAbility().getName();
          if (Integer.parseInt(ability.substring(ability.length() - 1)) >= 6) {
            reactIssue.setWhisper("None");
            reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.NONE.getColor());
          } else {
            reactIssue.setWhisper("/t " + name + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
            reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor());
          }
        }
      //}
    } else {
      reactIssue.setWhisper("/t " + name + " Please switch to the class you reacted to (" + reactName + ")");
      reactIssue.mark(InventorySlots.SKIN.getIndex(), Problem.MISSING_REACT_CLASS.getColor());
    }
  }

  /**
   * parseItem method.
   *
   * <p>Checks if the inventory contains the proper item for the React.
   */
  private static void parseItem(final String name, final React react, final Character character) {
    final JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    final Inventory inventory = character.getInventory();
    final Issue reactIssue = inventory.getReactIssue();
    final String ability = inventory.getAbility().getName();
    switch (react.getName()) {
      case "Armor Break":
        if (ability.equals("Shield of Ogmur UT") || ability.equals("Crystallised Fang's Venom UT")) {
          reactIssue.setWhisper("None");
          reactIssue.setProblem(Problem.NONE);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.NONE.getColor());
        } else {
          reactIssue.setProblem(Problem.MISSING_REACT);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT.getColor());
          reactIssue.setWhisper("/t " + name + " Please equip your " + react.getName().toLowerCase() + " so I can confirm it. You can swap it out after.");
        }
        break;
      case "Slow":
        final JSONObject slowItems = requirementSheet.getJSONObject("reacts").getJSONObject("slowItems");
        boolean good = true;
        for (final String keys : slowItems.keySet()) {
          final JSONObject jsonObject = (JSONObject) slowItems.get(keys);
          final JSONArray items = jsonObject.getJSONArray("items");
          final String item = ability.substring(0, ability.length() - 3);
          if (items.toList().contains(item)) {
            final JSONArray slotArray = jsonObject.getJSONArray("slot");
            for (int j = 0; j < slotArray.length(); j++) {
              final String slot = slotArray.getString(j);
              final Item item1 = switch (slot) {
                case "weapon" -> inventory.getWeapon();
                case "ability" -> inventory.getAbility();
                case "armor" -> inventory.getArmor();
                case "ring" -> inventory.getRing();
                default -> null;
              };
              assert item1 != null;
              final String substring = item1.getName().substring(0, item1.getName().length() - 3);
              if (items.toList().contains(substring)) {
                reactIssue.mark(InventorySlots.getEnumByType(item1.getType()).getIndex(), Problem.NONE.getColor());
              } else {
                reactIssue.mark(InventorySlots.getEnumByType(item1.getType()).getIndex(), Problem.MISSING_REACT.getColor());
                good = false;
              }
            }
            if (good) {
              reactIssue.setWhisper("None");
              reactIssue.setProblem(Problem.NONE);
            } else {
              reactIssue.setWhisper("/t " + name + " Please equip your " + react.getName().toLowerCase() + " so I can confirm it. You can swap it out after.");
              reactIssue.setProblem(Problem.MISSING_REACT);
            }
            break;
          } else {
            reactIssue.setWhisper("/t " + name + " Please equip your " + react.getName().toLowerCase() + " so I can confirm it. You can swap it out after.");
            reactIssue.setProblem(Problem.MISSING_REACT);
            reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT.getColor());
          }
        }
        break;
      case "Fungal Tome":
        if (ability.equals("Tome of the Mushroom Tribes UT") || ability.equals("Non-Fungible Tome UT")) { // accounts for april fools name SMH
          reactIssue.setWhisper("None");
          reactIssue.setProblem(Problem.NONE);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.NONE.getColor());
        } else {
          reactIssue.setWhisper("/t " + name + " Please equip your " + react.getName().toLowerCase() + " so I can confirm it. You can swap it out after.");
          reactIssue.setProblem(Problem.MISSING_REACT);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT.getColor());
        }
        break;
      case "Marble Seal":
        if (ability.equals("Marble Seal UT")) {
          reactIssue.setWhisper("None");
          reactIssue.setProblem(Problem.NONE);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.NONE.getColor());
        } else {
          reactIssue.setWhisper("/t " + name + " Please equip your " + react.getName().toLowerCase() + " so I can confirm it. You can swap it out after.");
          reactIssue.setProblem(Problem.MISSING_REACT);
          reactIssue.mark(InventorySlots.ABILITY.getIndex(), Problem.MISSING_REACT.getColor());
        }
        break;
      default:
        break;
    }
  }
}
