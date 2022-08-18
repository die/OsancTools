package com.github.waifu.gui.models;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class SetTableModel extends DefaultTableModel {

    private String[] columns = {"Problem", "Username", "Inventory", "Mark"};
    public SetTableModel() {
        this.addColumn("Problem");
        this.addColumn("Username");
        this.addColumn("Inventory");
        this.addColumn("Whisper");
        this.addColumn("Message");
        this.addColumn("Mark");
    }

    @Override
    public Class<?> getColumnClass(int column) {
        if (column == 2) return ImageIcon.class;
        if (column == 5) return Boolean.class;
        return Object.class;
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }
}
