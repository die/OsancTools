package com.github.waifu.gui.models;

import javax.swing.table.DefaultTableModel;

/**
 *
 */
public class VCTableModel extends DefaultTableModel {
    /**
     *
     */
    public VCTableModel() {
        addColumn("IGN");
        addColumn("In Group");
        addColumn("In VC");
        addColumn("Nickname");
        addColumn("Discord ID");
        addColumn("Role");
        addColumn("Mark");
    }

    /**
     *
     * @param column
     * @return
     */
    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 6) return Boolean.class;
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
        return column == 6;
    }
}
