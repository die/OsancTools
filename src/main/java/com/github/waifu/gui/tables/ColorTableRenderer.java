package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raider;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * To be documented.
 */
public class ColorTableRenderer extends DefaultTableCellRenderer {

  /**
   * To be documented.
   */
  private final List<Integer> colorIndices;
  /**
   * To be documented.
   */
  private final List<String> colors;
  /**
   * To be documented.
   */
  private final List<Raider> raiders;

  /**
   * To be documented.
   *
   * @param raiders To be documented.
   */
  public ColorTableRenderer(final List<Raider> raiders) {
    this.colorIndices = new ArrayList<>();
    this.raiders = raiders;
    colors = Arrays.asList("#2f4f4f", "#556b2f", "#a0522d", "#8b0000", "#808000", "#483d8b",
            "#008000", "#3cb371", "#000080", "#9acd32", "#daa520", "#7f007f",
            "#8fbc8f", "#b03060", "#d2b48c", "#ff0000", "#00ced1", "#ff8c00",
            "#0000cd", "#00ff00", "#00ff7f", "#00bfff", "#f4a460", "#a020f0",
            "#f08080", "#adff2f", "#ff6347", "#ff00ff", "#f0e68c", "#ffff54",
            "#6495ed", "#dda0dd", "#b0e0e6", "#7b68ee", "#ee82ee", "#98fb98",
            "#7fffd4", "#ff69b4", "#2c2440");

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

    if (column == 1 && !isSelected) {
      int count = 0;
      for (final Raider r : raiders) {
        if (r.getAccounts().size() > 1) {
          getRandomColor(39);
          for (final Account a : r.getAccounts()) {
            if (value.equals(a.getName())) {
              component.setForeground(Color.decode(colors.get(colorIndices.get(count))));
            }
          }
          count++;
        }
      }
    } else {
      component.setForeground(Utilities.getTextColorFromTheme());
    }
    return component;
  }

  /**
   * To be documented.
   *
   * @param bound To be documented.
   */
  public void getRandomColor(final int bound) {
    final Random random = new Random();
    final int n = random.nextInt(bound);
    if (!colorIndices.contains(n)) {
      colorIndices.add(n);
    }
  }
}
