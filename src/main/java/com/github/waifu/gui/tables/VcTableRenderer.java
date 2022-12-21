package com.github.waifu.gui.tables;

import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * To be documented.
 */
public class VcTableRenderer extends DefaultTableCellRenderer {

  /**
   * To be documented.
   */
  public VcTableRenderer() {

  }

  /**
   * To be documented.
   *
   * @param table To be documented.
   * @param value To be documented.
   * @param isSelected To be documented.
   * @param hasFocus To be documented.
   * @param row To be documented.
   * @param column To be documented.
   * @return To be documented.
   */
  public Component getTableCellRendererComponent(final JTable table, final Object value,
                                                 final boolean isSelected, final boolean hasFocus, final int row, final int column) {

    final Component component = super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);

    if (column == 1) {
      final Raider r = Gui.getRaid().findRaiderByServerNickname(String.valueOf(value));
      if (r != null && r.isCelestial()) {
        component.setForeground(Color.YELLOW);
      } else {
        component.setForeground(Utilities.getTextColorFromTheme());
      }
    } else {
      component.setForeground(Utilities.getTextColorFromTheme());
    }
    return component;
  }
}
