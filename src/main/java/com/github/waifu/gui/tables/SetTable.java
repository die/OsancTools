package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.models.SetTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * SetTable class to construct the UI for the table containing parsed sets.
 */
public class SetTable extends JFrame {

  /**
   * To be documented.
   */
  private JTable setsTable;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JCheckBox removeGoodSetsCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeBadSetsCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removePrivateProfilesCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeMarkedSetsCheckBox;
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeGoodSets = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeBadSets = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> privateProfile = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private TableRowSorter<TableModel> sorter;

  /**
   * SetTable method.
   *
   * <p>Constructs a JFrame to display the set parse.
   *
   * @param accountInventoryMap Map containing an Account object as the key
   *                            and its Inventory as the value.
   */
  public SetTable(final List<Raider> accountInventoryMap) {
    $$$setupUI$$$();
    createTable(accountInventoryMap);
    addActionListeners();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
    setVisible(true);
    new TableCopyAction(setsTable);

    pack();
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   *
   * @param accountInventoryMap Map containing an Account object as the key
   *                            and its Inventory as the value.
   */
  private void createTableSniffer(final List<Raider> accountInventoryMap) {
    final DefaultTableModel tableModel = new SetTableModel();
    int width = 0;
    for (final Raider raider : accountInventoryMap) {
      final Account account = raider.getAccounts().get(0);
      final Inventory inventory = account.getCharacters().get(0).getInventory();
      final Object[] array = new Object[6];
      final ImageIcon result = new ImageIcon(inventory.createImage(setsTable.getRowHeight(), setsTable.getRowHeight()).getImage());
      result.setDescription(inventory.printInventory());
      width = result.getIconWidth();
      // array[0] = inventory.getIssue().getWhisper();
      array[0] = inventory.getIssue().getProblem().getProblem();
      array[1] = account.getName();
      array[2] = result;
      array[3] = false;
      tableModel.addRow(array);
    }
    setsTable.setDefaultRenderer(Object.class, new ColorTableRenderer(accountInventoryMap));
    sorter = new TableRowSorter<>(tableModel);
    setsTable.setRowSorter(sorter);
    setsTable.setModel(tableModel);
    setsTable.getColumnModel().getColumn(2).setMinWidth(width);
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   *
   * @param accountInventoryMap Map containing an Account object as the key
   *                            and its Inventory as the value.
   */
  private void createTable(final List<Raider> accountInventoryMap) {
    final DefaultTableModel tableModel = new SetTableModel();
    int width = 0;
    for (final Raider raider : accountInventoryMap) {
      for (int j = 0; j < raider.getAccounts().size(); j++) {
        final Account account = raider.getAccounts().get(j);
        final Inventory inventory = account.getCharacters().get(0).getInventory();
        final Object[] array = new Object[6];
        final ImageIcon result = new ImageIcon(inventory.createImage(setsTable.getRowHeight(), setsTable.getRowHeight()).getImage());
        result.setDescription(inventory.printInventory());
        width = result.getIconWidth();
        array[0] = inventory.getIssue().getProblem().getProblem();
        array[1] = account.getName();
        array[2] = result;
        array[3] = false;
        tableModel.addRow(array);
      }
    }
    setsTable.setDefaultRenderer(Object.class, new ColorTableRenderer(accountInventoryMap));
    sorter = new TableRowSorter<>(tableModel);
    setsTable.setRowSorter(sorter);
    setsTable.setModel(tableModel);
    setsTable.getColumnModel().getColumn(2).setMinWidth(width);
  }

  /**
   * addActionListeners method.
   *
   * <p>Constructs all listeners for the JFrame.
   */
  private void addActionListeners() {
    setsTable.addPropertyChangeListener(evt -> {
      final JTable editedTable = (JTable) evt.getSource();
      final int row = editedTable.getEditingRow();
      final int column = editedTable.getEditingColumn();

      if (column == 3) {
        final String username = (String) editedTable.getValueAt(row, 1);
        final Raider r = Gui.getRaid().findRaiderByUsername(username);
        final boolean newValue = (boolean) editedTable.getValueAt(row, column);
        for (final Account a : r.getAccounts()) {
          final int n = findRowValue(a.getName());
          if (n != -1) {
            setsTable.setValueAt(newValue, n, column);
          }
        }
      }
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
   * Method generated by IntelliJ IDEA GUI Designer.
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(3, 3, new Insets(5, 5, 5, 5), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Sets", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    setsTable = new JTable();
    setsTable.setCellSelectionEnabled(true);
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
  }

  /**
   * To be documented.
   *
   * @noinspection ALL.
   * @return To be documented.
   */
  public JComponent $$$getRootComponent$$$() {
    return main;
  }

  /**
   * updateFilters method.
   *
   * <p>Updates the current filters to the table.
   * This allows real-time updates to filtered rows upon changes.
   */
  private void updateFilters() {
    final List<RowFilter<Object, Object>> filters = new ArrayList<>();
    filters.add(this.removeGoodSets);
    filters.add(this.removeBadSets);
    filters.add(this.privateProfile);
    filters.add(this.removeMarked);
    this.sorter.setRowFilter(RowFilter.andFilter(filters));
  }

  /**
   * To be documented.
   */
  private void createUIComponents() {
    // todo: place custom component creation code here
  }

  /**
   * To be documented.
   *
   * @param value To be documented.
   * @return To be documented.
   */
  private int findRowValue(final String value) {
    for (int i = 0; i < setsTable.getRowCount(); i++) {
      if (setsTable.getValueAt(i, 1).equals(value)) {
        return i;
      }
    }
    return -1;
  }
}
