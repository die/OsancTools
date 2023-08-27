package com.github.waifu.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class VcTableModel extends DefaultTableModel {

  public enum Column {
    RAIDER,
    IGN,
    VC,
    DEAFENED,
    MESSAGE,
    MARK
  }

  /**
   * To be documented.
   */
  public VcTableModel() {
    for (final Column column : Column.values()) {
      final String name = column.name();
      addColumn(name.charAt(0) + name.substring(1).toLowerCase());
    }
  }

  /**
   * To be documented.
   *
   * @param column To be documented.
   * @return To be documented.
   */
  @Override
  public Class<?> getColumnClass(final int column) {
    if (column == Column.MARK.ordinal()) {
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
    return column == Column.MARK.ordinal();
  }
}
