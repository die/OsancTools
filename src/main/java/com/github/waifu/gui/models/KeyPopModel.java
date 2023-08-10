package com.github.waifu.gui.models;

import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class KeyPopModel extends DefaultTableModel {

  /**
   * To be documented.
   */
  public KeyPopModel() {
    addColumn("Dungeon / Key");
    addColumn("Popper");
    addColumn("Timestamp");
  }

  /**
   * To be documented.
   *
   * @param column To be documented.
   * @return To be documented.
   */
  @Override
  public Class<?> getColumnClass(final int column) {
    return Object.class;
  }

  /**
   * To be documented.
   *
   * @param row To be documented.
   * @param column To be documented.
   * @return To be documented.
   */
  @Override
  public boolean isCellEditable(final int row, final int column) {
    return false;
  }
}
