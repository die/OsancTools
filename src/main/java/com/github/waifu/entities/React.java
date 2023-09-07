package com.github.waifu.entities;

import com.github.waifu.enums.Problem;
import com.github.waifu.gui.Gui;
import com.github.waifu.handlers.RequirementSheetHandler;
import com.github.waifu.util.Utilities;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * React class to store react data.
 */
public class React {

  /**
   * To be documented.
   */
  private final String name;
  /**
   * To be documented.
   */
  private String requirement;
  /**
   * To be documented.
   */
  private String type;
  /**
   * To be documented.
   */
  private ImageIcon image;
  /**
   * To be documented.
   */
  private final JSONArray raiderIds;

  public React(final String name, final JSONArray raiderIds) {
    this.name = name;
    this.raiderIds = raiderIds;
  }

  public boolean hasRaiderId(final String raiderId) {
    for (int i = 0; i < raiderIds.length(); i++) {
      if (raiderIds.getString(i).equals(raiderId)) {
        return true;
      }
    }
    return false;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getName() {
    return this.name;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getType() {
    return type;
  }

  /**
   * To be documented.
   *
   * @param type To be documented.
   */
  public void setType(final String type) {
    this.type = type;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String getRequirement() {
    return this.requirement;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public ImageIcon getImage() {
    return this.image;
  }

  public JSONArray getRaiderIds() {
    return raiderIds;
  }

  /**
   * parseReact method.
   *
   * <p>Parsing the react given its type.
   *
   * @param type type of React in how it should be checked.
   */
  public void parseReact(final String type) {
    switch (type) {
      case "item":
        for (int i = 0; i < raiderIds.length(); i++) {
          final ViBotRaider viBotRaider = Gui.getRaid().getViBotRaiderById(raiderIds.getString(i));
          //parseItem(Gui.getRaid().getGroup().get);
        }
        break;
      case "class":
        // parseClass(a);
        break;
      case "dps":
        // parseDps(a);
        break;
      case "lock":
        //             a.getCharacters().get(0).getInventory().getIssue().setWhisper("/t " + a.getName() + " Please trade me so I can confirm your effusion.");
        break;
      default:
        //             a.getCharacters().get(0).getInventory().getIssue().setWhisper("/lock " + a.getName());
        break;
    }
  }

  /**
   * parseItem method.
   *
   * <p>Checks if the inventory contains the proper item for the React.
   *
   * @param account account of the raider.
   */
  private void parseItem(final Account account) {
    JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    final Inventory inventory = account.getRecentCharacter().getInventory();
    if (inventory != null) {
      final String ability = inventory.getAbility().getName();
      switch (name) {
        case "Ogmur/Pogmur":
          if (ability.equals("Shield of Ogmur UT") || ability.equals("Crystallised Fang's Venom UT")) {
            inventory.getIssue().setWhisper("None");
            inventory.getIssue().setProblem(Problem.NONE);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
          } else {
            inventory.getIssue().setProblem(Problem.MISSING_REACT);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
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
                  item1.setImage(Utilities.markImage(item1.getImage(), Problem.NONE.getColor()));
                } else {
                  item1.setImage(Utilities.markImage(item1.getImage(), Problem.MISSING_REACT.getColor()));
                  good = false;
                }
              }
              if (good) {
                inventory.getIssue().setWhisper("None");
                inventory.getIssue().setProblem(Problem.NONE);
              } else {
                inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                inventory.getIssue().setProblem(Problem.MISSING_REACT);
              }
              break;
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
              inventory.getIssue().setProblem(Problem.MISSING_REACT);
              inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
            }
          }
          break;
        case "Fungal Tome":
          if (ability.equals("Tome of the Mushroom Tribes UT") || ability.equals("Non-Fungible Tome UT")) { // accounts for april fools name SMH
            inventory.getIssue().setWhisper("None");
            inventory.getIssue().setProblem(Problem.NONE);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
          } else {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
            inventory.getIssue().setProblem(Problem.MISSING_REACT);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
          }
          break;
        case "Marble Seal":
          if (ability.equals("Marble Seal UT")) {
            inventory.getIssue().setWhisper("None");
            inventory.getIssue().setProblem(Problem.NONE);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
          } else {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
            inventory.getIssue().setProblem(Problem.MISSING_REACT);
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * parseDps method.
   *
   * <p>Checks if the inventory meets the required DPS and Class.
   *
   * @param account account of the raider.
   */
  public void parseDps(final Account account) {
    final Inventory inventory = account.getRecentCharacter().getInventory();
    final String type = account.getRecentCharacter().getType();
    if (inventory.getItems().stream().allMatch(item -> item.getName().equals("Empty slot"))) {
      inventory.getIssue().setWhisper("Private Profile");
    } else {
      final List<String> ranged = Arrays.asList("Wizard", "Mystic", "Necromancer", "Priest", "Summoner", "Sorcerer", "Bard", "Archer", "Huntress");
      switch (name) {
        case "Wizard/Summoner 2/4":
          if (!(type.equals("Wizard") || type.equals("Summoner"))) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard/Summoner that is 2/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        case "2/4+ Wizard":
          if (!account.getRecentCharacter().getType().equals("Wizard")) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard that is 2/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        case "2/4+ Ranged":
          if (!ranged.contains(account.getRecentCharacter().getType())) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a ranged class that is 2/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        case "Wizard/Summoner 3/4":
          if (!(account.getRecentCharacter().getType().equals("Wizard") || account.getRecentCharacter().getType().equals("Summoner"))) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard/Summoner that is 3/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        case "3/4+ Wizard":
          if (!account.getRecentCharacter().getType().equals("Wizard")) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard that is 3/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        case "3/4+ Ranged":
          if (!ranged.contains(account.getRecentCharacter().getType())) {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a ranged class that is 3/4 dps.");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
          } else {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
              inventory.getIssue().setWhisper("None");
            } else {
              inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
            }
          }
          break;
        default:
          break;
      }
    }
  }

  /**
   * parseClass method.
   *
   * <p>Checks if the inventory meets the required Class and T6/T7 Item.
   *
   * @param account account of the raider.
   */
  public void parseClass(final Account account) {
    final JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    final String type = account.getRecentCharacter().getType();
    final Inventory inventory = account.getRecentCharacter().getInventory();
    if (name.equals(type)) {
      account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
      inventory.getIssue().setProblem(Problem.NONE);
      inventory.getIssue().setWhisper("None");
      if (requirement.contains("T6")) {
        if (inventory.getAbility().getName().equals("Empty slot") || inventory.getItems().get(1).getName().endsWith("ST")) {
          inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
          inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
        } else if (inventory.getItems().get(1).getName().endsWith("UT")) {
          final JSONArray reskins = requirementSheet.getJSONObject("allowedSTSets").getJSONObject("Oryxmas").getJSONArray("items");
          final String ability = inventory.getAbility().getName();
          if (reskins.toList().contains(ability.substring(0, ability.length() - 3))) {
            inventory.getIssue().setWhisper("None");
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
          } else {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
          }
        } else {
          final String ability = inventory.getAbility().getName();
          if (Integer.parseInt(ability.substring(ability.length() - 1)) >= 6) {
            inventory.getIssue().setWhisper("None");
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
          } else {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
          }
        }
      }
    } else {
      inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to the class you reacted to (" + name + ")");
      account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT_CLASS.getColor()));
    }
  }
}
