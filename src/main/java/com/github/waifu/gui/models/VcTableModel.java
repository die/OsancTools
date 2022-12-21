package com.github.waifu.gui.models;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class VcTableModel extends DefaultTableModel {

  /**
   * To be documented.
   */
  public VcTableModel() {
    addColumn("Avatar");
    addColumn("Nickname");
    addColumn("IGN");
    addColumn("In Group");
    addColumn("In VC");
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
    if (column == 5) {
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
    return column == 5;
  }
}
