package com.github.waifu.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 */
public class ReactTableModel extends DefaultTableModel {

    /**
     *
     */
    public ReactTableModel() {
        addColumn("React");
        addColumn("Username");
        addColumn("Inventory");
        addColumn("Message");
        addColumn("Mark");
    }

    /**
     *
     * @param column
     * @return
     */
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 0) return ImageIcon.class;
        if (column == 2) return ImageIcon.class;
        if (column == 4) return Boolean.class;
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
        return column == 4;
    }
}
