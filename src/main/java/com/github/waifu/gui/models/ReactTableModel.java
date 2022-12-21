package com.github.waifu.gui.models;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class ReactTableModel extends DefaultTableModel {

  /**
   * To be documented.
   */
  public ReactTableModel() {
    addColumn("React");
    addColumn("Username");
    addColumn("Inventory");
    addColumn("Message");
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
    if (column == 0) {
      return ImageIcon.class;
    }
    if (column == 2) {
      return ImageIcon.class;
    }
    if (column == 4) {
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
    return column == 4;
  }
}
