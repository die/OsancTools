package com.github.waifu.entities;

import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.enums.InventorySlots;
import com.github.waifu.enums.Problem;
import com.github.waifu.handlers.RequirementSheetHandler;
import com.github.waifu.util.Utilities;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.ImageIcon;
import org.json.JSONArray;
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
  private Issue issue;
  /**
   * To be documented.
   */
  private Issue reactIssue;
  /**
   * Stores if the inventory was parsed already.
   */
  private boolean parsed;

  /**
   * Inventory method.
   *
   * <p>Constructs a default Inventory with only empty slots.
   */
  public Inventory() {
    final List<Item> empty = new ArrayList<>();
    empty.add(new Item(-1, "Empty slot", "weapon", "Wizard"));
    empty.add(new Item(-1, "Empty slot", "ability", "Wizard"));
    empty.add(new Item(-1, "Empty slot", "armor", "Wizard"));
    empty.add(new Item(-1, "Empty slot", "ring", "Wizard"));

    issue = new Issue(Problem.NONE);
    reactIssue = new Issue(Problem.NONE);
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
    this.reactIssue = new Issue(Problem.NONE);
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
    return this.items.get(InventorySlots.WEAPON.getIndex());
  }

  /**
   * To be documented.
   *
   * @param item to be documented.
   */
  public void setWeapon(final Item item) {
    this.items.set(InventorySlots.WEAPON.getIndex(), item);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getAbility() {
    return this.items.get(InventorySlots.ABILITY.getIndex());
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getArmor() {
    return this.items.get(InventorySlots.ARMOR.getIndex());
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Item getRing() {
    return this.items.get(InventorySlots.RING.getIndex());
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Issue getIssue() {
    return this.issue;
  }

  public void resetIssue() {
    this.issue = new Issue(Problem.NONE);
  }

  public Issue getReactIssue() {
    return reactIssue;
  }

  public void resetReactIssue() {
    this.reactIssue = new Issue(Problem.NONE);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public String printInventory() {
    final StringJoiner stringJoiner = new StringJoiner(", ");
    for (final Item item : items) {
      stringJoiner.add(item.getName());
    }
    return "Inventory: [" + stringJoiner.toString() + "]";
  }

  /**
   * parseInventory method.
   */
  public void parseInventory(final String name) {
    for (final Item i : items) {
      if (i.getId() == -1) continue;
      i.setImage(RotmgAssets.equipXMLObjectList.get(i.getId()).getImage());
    }
    RequirementSheetHandler.parse(this, name);
  }

  /**
   * To be documented.
   *
   * @param skin To be documented.
   * @param dps To be documented.
   * @return To be documented.
   */
  public boolean calculateDps(final int skin, final int dps) {
    JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
    final JSONObject dpsItems = requirementSheet.getJSONObject("reacts").getJSONObject("dpsItems");
    int count = 0;
    for (final Item s : items) {
      final JSONArray jsonArray = dpsItems.getJSONArray(s.getType());
      final String name;
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
    final JSONArray exaltedSkins = (JSONArray) requirementSheet.get("exaltedSkins");
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
    final ImageIcon weapon = new ImageIcon(getItems().get(InventorySlots.WEAPON.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
    final ImageIcon ability = new ImageIcon(getItems().get(InventorySlots.ABILITY.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
    final ImageIcon armor = new ImageIcon(getItems().get(InventorySlots.ARMOR.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
    final ImageIcon ring = new ImageIcon(getItems().get(InventorySlots.RING.getIndex()).getImage().getImage().getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING));
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

  public boolean isParsed() {
    return parsed;
  }

}
