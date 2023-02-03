package com.github.waifu.handlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Item;
import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

class RequirementSheetHandlerTest {

  @Test
  void parse() {
    System.out.println(WebAppHandler.getData(18853));
  }

  @Test
  @Order(0)
  void getRequirementSheet() {
    assertNotNull(RequirementSheetHandler.getRequirementSheet());
  }

  @Test
  @Order(1)
  void setRequirementSheet() {
    RequirementSheetHandler.setRequirementSheet("sheets/OryxSanctuary.json");
    assertNotNull(RequirementSheetHandler.getRequirementSheet());
  }

  @Test
  @Order(2)
  void parseBannedItem() {
    RequirementSheetHandler.setRequirementSheet("sheets/OryxSanctuary.json");
    final Inventory inventory = createSTSet();

    RequirementSheetHandler.parseBannedItem(inventory.getWeapon(), inventory.getItems(), inventory.getIssue());

    assertEquals(Problem.NONE, inventory.getIssue().getProblem());

    inventory.getItems().get(InventorySlots.WEAPON.getIndex()).setName("Useless Katana UT");

    RequirementSheetHandler.parseBannedItem(inventory.getWeapon(), inventory.getItems(), inventory.getIssue());

    assertEquals(Problem.BANNED_ITEM, inventory.getIssue().getProblem());
  }

  @Test
  @Order(3)
  void parseAllowedSTSet() {
    RequirementSheetHandler.setRequirementSheet("sheets/OryxSanctuary.json");
    final Inventory inventory = createSTSet();

    assertTrue(RequirementSheetHandler.parseAllowedSTSet(inventory.getWeapon(), inventory.getItems()));

    inventory.getItems().get(InventorySlots.RING.getIndex()).setName("The Forgotten Crown UT");

    assertFalse(RequirementSheetHandler.parseAllowedSTSet(inventory.getWeapon(), inventory.getItems()));
  }

  @Test
  @Order(4)
  void parseSwapout() {
    RequirementSheetHandler.setRequirementSheet("sheets/OryxSanctuary.json");
    final Inventory inventory = createSTSet();

    RequirementSheetHandler.parseSwapout(inventory.getWeapon(), inventory.getIssue());

    assertEquals(Problem.NONE, inventory.getIssue().getProblem());

    inventory.getItems().get(InventorySlots.WEAPON.getIndex()).setName("Legacy Pixie-Enchanted Sword ST");

    RequirementSheetHandler.parseSwapout(inventory.getWeapon(), inventory.getIssue());

    assertEquals(Problem.SWAPOUT_ITEM, inventory.getIssue().getProblem());
  }

  private Inventory createSTSet() {
    final Item weapon = new Item("Quartz Cutter ST", "weapon", "Ninja");
    final Item ability = new Item("Crystalline Kunai ST", "ability", "Ninja");
    final Item armor = new Item("Luminous Armor ST", "armor", "Ninja");
    final Item ring = new Item("Radiant Heart ST", "ring", "Ninja");

    final List<Item> items = new ArrayList<>();
    Collections.addAll(items, weapon, ability, armor, ring);
    return new Inventory(items);
  }
}
