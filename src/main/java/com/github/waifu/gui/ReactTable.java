package com.github.waifu.gui;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.React;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * ReactTable class to construct the UI for the table containing parsed reacts.
 */
public class ReactTable extends JFrame {

    private JTable reactTable;
    private JPanel ReactPanel;
    private JCheckBox removeGoodReactsCheckBox;
    private JCheckBox removeBadReactsCheckBox;
    private JCheckBox removeManualReactsCheckBox;
    private JCheckBox removeMarkedReactsCheckBox;
    private RowFilter<Object, Object> removeGoodReacts = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeBadReacts = RowFilter.regexFilter("");
    private RowFilter<Object, Object> privateProfile = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");
    private TableRowSorter<TableModel> sorter;

    /**
     * ReactTable method.
     *
     * Constructs a JFrame to display the react parse.
     *
     * @param reacts List containing React objects.
     */
    public ReactTable(List<React> reacts) {
        $$$setupUI$$$();
        createTable(reacts);
        addActionListeners();
        setAlwaysOnTop(true);
        setContentPane(ReactPanel);
        setResizable(false);
        setTitle("OsancTools");
        //setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/Gravestone.png"))).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        setVisible(true);
    }

    /**
     * createTable method.
     *
     * Creates the table model, adds rows to the model, and applies the model to the table.
     *
     * @param reacts List containing React objects.
     */
    private void createTable(List<React> reacts) {
        DefaultTableModel tableModel = new DefaultTableModel() {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) return ImageIcon.class;
                if (column == 2) return ImageIcon.class;
                if (column == 4) return Boolean.class;
                return Object.class;
            }
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return column != 0 && column != 2;
            }
        };
        tableModel.addColumn("React");
        tableModel.addColumn("Username");
        tableModel.addColumn("Inventory");
        tableModel.addColumn("Message");
        tableModel.addColumn("Mark");
        sorter = new TableRowSorter<>(tableModel);
        reactTable.setRowSorter(sorter);
        int width = 0;
        for (React react : reacts) {
            for (Account a : react.getRaiders()) {
                Object[] array = new Object[5];
                array[0] = new ImageIcon(react.getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                array[1] = a.getName();
                Inventory inventory = a.getCharacters().get(0).getInventory();
                ImageIcon skin = new ImageIcon(a.getCharacters().get(0).getSkinImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                ImageIcon weapon = new ImageIcon(inventory.getItems().get(0).getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                ImageIcon ability = new ImageIcon(inventory.getItems().get(1).getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                ImageIcon armor = new ImageIcon(inventory.getItems().get(2).getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                ImageIcon ring = new ImageIcon(inventory.getItems().get(3).getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                int w = skin.getIconWidth() + weapon.getIconWidth() + ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth();
                int h = weapon.getIconHeight();
                BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                Graphics g = combined.getGraphics();
                g.drawImage(skin.getImage(), 0, 0, null);
                g.drawImage(weapon.getImage(), weapon.getIconWidth(), 0, null);
                g.drawImage(ability.getImage(), weapon.getIconWidth() + ability.getIconWidth(), 0, null);
                g.drawImage(armor.getImage(), weapon.getIconWidth() + ability.getIconWidth() + armor.getIconWidth(), 0, null);
                g.drawImage(ring.getImage(), weapon.getIconWidth() + ability.getIconWidth() + armor.getIconWidth() + ring.getIconWidth(), 0, null);
                ImageIcon result = new ImageIcon(combined);
                width = result.getIconWidth() + skin.getIconWidth();
                array[2] = result;
                array[3] = a.getCharacters().get(0).getInventory().getIssue().getWhisper();
                array[4] = false;
                tableModel.addRow(array);
            }
        }
        reactTable.setModel(tableModel);
        reactTable.getColumnModel().getColumn(2).setMinWidth(width);
    }

    /**
     * addActionListeners method.
     *
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {

        reactTable.addPropertyChangeListener(evt -> updateFilters());

        removeGoodReactsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeGoodReactsCheckBox.isSelected()) {
                    removeGoodReacts = RowFilter.notFilter(RowFilter.regexFilter("None", 3));
                    updateFilters();
                } else {
                    removeGoodReacts = RowFilter.regexFilter("", 3);
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeBadReactsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeBadReactsCheckBox.isSelected()) {
                    removeBadReacts = RowFilter.regexFilter("None", 3);
                    updateFilters();
                } else {
                    removeBadReacts = RowFilter.regexFilter("", 3);
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeManualReactsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeManualReactsCheckBox.isSelected()) {
                    privateProfile = RowFilter.notFilter(RowFilter.regexFilter("/lock", 3));
                    updateFilters();
                } else {
                    privateProfile = RowFilter.regexFilter("", 3);
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeMarkedReactsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeMarkedReactsCheckBox.isSelected()) {
                    removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 4));
                    updateFilters();
                } else {
                    removeMarked = RowFilter.regexFilter("", 4);
                    updateFilters();
                }
                return null;
            }
        }.execute());
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        ReactPanel = new JPanel();
        ReactPanel.setLayout(new GridLayoutManager(3, 3, new Insets(5, 5, 5, 5), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        ReactPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Reacts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        reactTable = new JTable();
        scrollPane1.setViewportView(reactTable);
        removeGoodReactsCheckBox = new JCheckBox();
        removeGoodReactsCheckBox.setText("Remove Good Reacts");
        ReactPanel.add(removeGoodReactsCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeManualReactsCheckBox = new JCheckBox();
        removeManualReactsCheckBox.setText("Remove Manual Reacts");
        ReactPanel.add(removeManualReactsCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeBadReactsCheckBox = new JCheckBox();
        removeBadReactsCheckBox.setText("Remove Bad Reacts");
        ReactPanel.add(removeBadReactsCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMarkedReactsCheckBox = new JCheckBox();
        removeMarkedReactsCheckBox.setText("Remove Marked Reacts");
        ReactPanel.add(removeMarkedReactsCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return ReactPanel;
    }

    /**
     * updateFilters method.
     *
     * Updates the current filters to the table.
     * This allows real-time updates to filtered rows upon changes.
     */
    private void updateFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        filters.add(this.removeGoodReacts);
        filters.add(this.removeBadReacts);
        filters.add(this.privateProfile);
        filters.add(this.removeMarked);
        this.sorter.setRowFilter(RowFilter.andFilter(filters));
    }
}
