package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.models.ReactTableModel;
import com.github.waifu.util.Utilities;
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
    private RowFilter<Object, Object> removeManualReacts = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");
    private TableRowSorter<TableModel> sorter;

    /**
     * ReactTable method.
     * <p>
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
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
        setVisible(true);
        pack();
        new TableCopyAction(reactTable);
    }

    /**
     * createTable method.
     * <p>
     * Creates the table model, adds rows to the model, and applies the model to the table.
     *
     * @param reacts List containing React objects.
     */
    private void createTable(List<React> reacts) {
        DefaultTableModel tableModel = new ReactTableModel();
        sorter = new TableRowSorter<>(tableModel);
        reactTable.setRowSorter(sorter);
        int width = 0;
        for (React react : reacts) {
            for (Raider r  : react.getRaiders()) {
                for (Account a : r.getAccounts()) {
                    Object[] array = new Object[5];
                    ImageIcon reactImage = new ImageIcon(react.getImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                    reactImage.setDescription(react.getName());
                    array[0] = reactImage;
                    array[1] = a.getName();
                    Inventory inventory = a.getCharacters().get(0).getInventory();
                    ImageIcon result = new ImageIcon(inventory.createImage(reactTable.getRowHeight(), reactTable.getRowHeight()).getImage());
                    ImageIcon skin = new ImageIcon(a.getCharacters().get(0).getSkinImage().getImage().getScaledInstance(reactTable.getRowHeight(), reactTable.getRowHeight(), Image.SCALE_SMOOTH));
                    width = result.getIconWidth() + skin.getIconWidth();
                    BufferedImage bufferedImage = new BufferedImage(width, reactTable.getRowHeight(), BufferedImage.TYPE_INT_ARGB);
                    Graphics g = bufferedImage.getGraphics();
                    g.drawImage(skin.getImage(), 0, 0, null);
                    g.drawImage(result.getImage(), skin.getIconWidth(), 0, null);
                    g.dispose();
                    ImageIcon imageIcon = new ImageIcon(bufferedImage);
                    imageIcon.setDescription(inventory.printInventory());
                    array[2] = imageIcon;
                    String whisper = a.getCharacters().get(0).getInventory().getIssue().getWhisper();
                    array[3] = whisper;
                    array[4] = false;
                    tableModel.addRow(array);
                }
            }
        }
        List<Raider> raiders = new ArrayList<>();
        for (React react : reacts) {
            raiders.addAll(react.getRaiders());
        }
        reactTable.setDefaultRenderer(Object.class, new ColorTableRenderer(raiders));
        reactTable.setModel(tableModel);
        reactTable.getColumnModel().getColumn(2).setMinWidth(width);
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        reactTable.addPropertyChangeListener(evt -> {
            JTable editedTable = (JTable) evt.getSource();
            int row = editedTable.getEditingRow();
            int column = editedTable.getEditingColumn();

            if (column == 4) {
                String username = (String) editedTable.getValueAt(row, 1);
                Raider r  = GUI.raid.findRaiderByUsername(username);
                boolean newValue = (boolean) editedTable.getValueAt(row, column);
                for (Account a : r.getAccounts()) {
                    int n = findRowValue(a.getName());
                    if (n != -1) {
                        reactTable.setValueAt(newValue, n, column);
                    }
                }
            }
            updateFilters();
        });

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
                    removeManualReacts = RowFilter.notFilter(RowFilter.regexFilter("/lock", 3));
                    updateFilters();
                } else {
                    removeManualReacts = RowFilter.regexFilter("", 3);
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
        reactTable.setCellSelectionEnabled(true);
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
     * <p>
     * Updates the current filters to the table.
     * This allows real-time updates to filtered rows upon changes.
     */
    private void updateFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        filters.add(this.removeGoodReacts);
        filters.add(this.removeBadReacts);
        filters.add(this.removeManualReacts);
        filters.add(this.removeMarked);
        this.sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    private int findRowValue(String value) {
        for (int i = 0; i < reactTable.getRowCount(); i++) {
            if (reactTable.getValueAt(i, 1).equals(value)) {
                return i;
            }
        }
        return -1;
    }
}
