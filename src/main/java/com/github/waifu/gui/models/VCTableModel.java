package com.github.waifu.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 *
 */
public class VCTableModel extends DefaultTableModel {

    /**
     *
     */
    public VCTableModel() {
        addColumn("Avatar");
        addColumn("Nickname");
        addColumn("IGN");
        addColumn("In Group");
        addColumn("In VC");
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
        if (column == 5) return Boolean.class;
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
        return column == 5;
    }
}
