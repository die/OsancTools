package com.github.waifu.gui.models;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class ReactTableModel extends DefaultTableModel {

  public enum Column {
    REACT,
    RAIDER,
    USERNAME,
    INVENTORY,
    MESSAGE,
    MARK
  }

  /**
   * To be documented.
   */
  public ReactTableModel() {
    for (final ReactTableModel.Column column : ReactTableModel.Column.values()) {
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
    /*if (column == 0) {
      return ImageIcon.class;
    }*/
    if (column == Column.INVENTORY.ordinal()) {
      return ImageIcon.class;
    }
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
