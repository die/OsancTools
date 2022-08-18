package com.github.waifu.gui;

import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Raider;
import com.github.waifu.enums.Problem;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.models.SetTableModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SetTable class to construct the UI for the table containing parsed sets.
 */
public class SetTable extends JFrame {

    private JTable setsTable;
    private JPanel main;
    private JCheckBox removeGoodSetsCheckBox;
    private JCheckBox removeBadSetsCheckBox;
    private JCheckBox removePrivateProfilesCheckBox;
    private JCheckBox removeMarkedSetsCheckBox;
    private JButton viewProfileButton;
    private RowFilter<Object, Object> removeGoodSets = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeBadSets = RowFilter.regexFilter("");
    private RowFilter<Object, Object> privateProfile = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");
    private TableRowSorter<TableModel> sorter;
    private Map<Raider, Inventory> accountInventoryMap;

    /**
     * SetTable method.
     * <p>
     * Constructs a JFrame to display the set parse.
     *
     * @param accountInventoryMap Map containing an Account object as the key
     *                            and its Inventory as the value.
     */
    public SetTable(Map<Raider, Inventory> accountInventoryMap) throws MalformedURLException {
        this.accountInventoryMap = accountInventoryMap;
        $$$setupUI$$$();
        createTable(accountInventoryMap);
        addActionListeners();
        setContentPane(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        // setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("resources/Gravestone.png"))).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        setVisible(true);
        viewProfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = (String) setsTable.getValueAt(setsTable.getSelectedRow(), 1);
                for (Map.Entry<Raider, Inventory> m : accountInventoryMap.entrySet()) {
                    if (m.getKey().getAccounts().get(0).getName().equalsIgnoreCase(username)) {
                        Raider r = m.getKey();
                        r.setAvatarSize(128, 128);
                        new AccountView(r);
                    }
                }
            }
        });
        TableCopyAction tableCopyAction = new TableCopyAction(setsTable);
        pack();
    }

    /**
     * createTable method.
     * <p>
     * Creates the table model, adds rows to the model, and applies the model to the table.
     *
     * @param accountInventoryMap Map containing an Account object as the key
     *                            and its Inventory as the value.
     */
    private void createTable(Map<Raider, Inventory> accountInventoryMap) {
        DefaultTableModel tableModel = new SetTableModel();
        int width = 0;
        for (Map.Entry<Raider, Inventory> m : accountInventoryMap.entrySet()) {
            if (m.getValue() != null) {
                Object[] array = new Object[6];
                ImageIcon result = new ImageIcon(m.getValue().createImage(setsTable.getRowHeight(), setsTable.getRowHeight()).getImage());
                width = result.getIconWidth();
                array[0] = m.getValue().getIssue().getProblem().getProblem();
                array[1] = m.getKey().getAccounts().get(0).getName();
                array[2] = result;
                if (!m.getValue().getIssue().getProblem().equals(Problem.NONE)) {
                    array[3] = "/t " + m.getKey().getAccounts().get(0).getName() + " " + m.getValue().getIssue().getWhisper();
                    array[4] = ";warn " + m.getKey().getAccounts().get(0).getName() + " " + m.getValue().getIssue().getMessage();
                } else {
                    array[3] = "";
                    array[4] = "";
                }
                array[5] = false;
                tableModel.addRow(array);
            }
        }
        sorter = new TableRowSorter<>(tableModel);
        setsTable.setRowSorter(sorter);
        setsTable.setModel(tableModel);
        setsTable.getColumnModel().getColumn(2).setMinWidth(width);
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        setsTable.addPropertyChangeListener(evt -> {
            updateFilters();
        });

        removeGoodSetsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeGoodSetsCheckBox.isSelected()) {
                    removeGoodSets = RowFilter.notFilter(RowFilter.regexFilter("None", 0));
                    updateFilters();
                } else {
                    removeGoodSets = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeBadSetsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeBadSetsCheckBox.isSelected()) {
                    removeBadSets = RowFilter.regexFilter("None", 0);
                    updateFilters();
                } else {
                    removeBadSets = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removePrivateProfilesCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removePrivateProfilesCheckBox.isSelected()) {
                    privateProfile = RowFilter.notFilter(RowFilter.regexFilter("Private Profile", 0));
                    updateFilters();
                } else {
                    privateProfile = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeMarkedSetsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeMarkedSetsCheckBox.isSelected()) {
                    removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 3));
                    updateFilters();
                } else {
                    removeMarked = RowFilter.regexFilter("");
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
        main = new JPanel();
        main.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Sets", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        setsTable = new JTable();
        scrollPane1.setViewportView(setsTable);
        removeGoodSetsCheckBox = new JCheckBox();
        removeGoodSetsCheckBox.setText("Remove Good Sets");
        main.add(removeGoodSetsCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removePrivateProfilesCheckBox = new JCheckBox();
        removePrivateProfilesCheckBox.setText("Remove Private Profiles");
        main.add(removePrivateProfilesCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeBadSetsCheckBox = new JCheckBox();
        removeBadSetsCheckBox.setText("Remove Bad Sets");
        main.add(removeBadSetsCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMarkedSetsCheckBox = new JCheckBox();
        removeMarkedSetsCheckBox.setText("Remove Marked Sets");
        main.add(removeMarkedSetsCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        viewProfileButton = new JButton();
        viewProfileButton.setText("View Profile");
        main.add(viewProfileButton, new GridConstraints(3, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    /**
     * updateFilters method.
     * <p>
     * Updates the current filters to the table.
     * This allows real-time updates to filtered rows upon changes.
     */
    private void updateFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        filters.add(this.removeGoodSets);
        filters.add(this.removeBadSets);
        filters.add(this.privateProfile);
        filters.add(this.removeMarked);
        this.sorter.setRowFilter(RowFilter.andFilter(filters));
    }
}
