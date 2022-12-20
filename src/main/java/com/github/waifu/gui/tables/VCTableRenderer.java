package com.github.waifu.gui.tables;

import com.github.waifu.entities.Raider;
import com.github.waifu.gui.GUI;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class VCTableRenderer extends DefaultTableCellRenderer {

  public VCTableRenderer() {

  }

  public Component getTableCellRendererComponent(JTable table, Object value,
                                                 boolean isSelected, boolean hasFocus, int row, int column) {

    Component component = super.getTableCellRendererComponent(table, value,
            isSelected, hasFocus, row, column);

    if (column == 1) {
      Raider r = GUI.raid.findRaiderByServerNickname(String.valueOf(value));
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
