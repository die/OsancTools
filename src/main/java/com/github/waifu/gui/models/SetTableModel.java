package com.github.waifu.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 */
public class SetTableModel extends DefaultTableModel {
    /**
     *
     */
    public SetTableModel() {
        addColumn("Problem");
        addColumn("Username");
        addColumn("Inventory");
        addColumn("Mark");
    }

    /**
     *
     * @param column
     * @return
     */
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 2) return ImageIcon.class;
        if (column == 3) return Boolean.class;
        return Object.class;
    }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 3;
    }
}
