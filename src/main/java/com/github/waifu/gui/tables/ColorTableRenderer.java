package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raider;
import com.github.waifu.util.Utilities;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.*;
import java.util.List;

public class ColorTableRenderer extends DefaultTableCellRenderer {

    private final List<Integer> colorIndices;
    private final List<String> colors;
    private final List<Raider> raiders;

    public ColorTableRenderer(List<Raider> raiders) {
        this.colorIndices = new ArrayList<>();
        this.raiders = raiders;
        colors = Arrays.asList( "#2f4f4f", "#556b2f", "#a0522d", "#8b0000", "#808000", "#483d8b",
                                "#008000", "#3cb371", "#000080", "#9acd32", "#daa520", "#7f007f",
                                "#8fbc8f", "#b03060", "#d2b48c", "#ff0000", "#00ced1", "#ff8c00",
                                "#0000cd", "#00ff00", "#00ff7f", "#00bfff", "#f4a460", "#a020f0",
                                "#f08080", "#adff2f", "#ff6347", "#ff00ff", "#f0e68c", "#ffff54",
                                "#6495ed", "#dda0dd", "#b0e0e6", "#7b68ee", "#ee82ee", "#98fb98",
                                "#7fffd4", "#ff69b4", "#2c2440");

    }

    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {

        Component component = super.getTableCellRendererComponent(table, value,
                isSelected, hasFocus, row, column);

        if (column == 1 && !isSelected) {
            int count = 0;
            for (Raider r : raiders) {
                if (r.getAccounts().size() > 1) {
                    getRandomColor(39);
                    for (Account a : r.getAccounts()) {
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

    public void getRandomColor(int bound) {
        Random random = new Random();
        int n = random.nextInt(bound);
        if (!colorIndices.contains(n)) {
            colorIndices.add(n);
        }
    }
}
