package com.github.waifu.gui.models;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class SetTableModel extends DefaultTableModel {

  /**
   * To be documented.
   */
  public SetTableModel() {
    addColumn("Problem");
    addColumn("Username");
    addColumn("Inventory");
    addColumn("Mark");
  }

  /**
   * To be documented.
   *
   * @param column To be documented.
   * @return To be documented.
   */
  @Override
  public Class<?> getColumnClass(final int column) {
    if (column == 2) {
      return ImageIcon.class;
    }
    if (column == 3) {
      return Boolean.class;
    }
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
    return column == 3;
  }
}
