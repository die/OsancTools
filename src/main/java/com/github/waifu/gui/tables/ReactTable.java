package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Inventory;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.entities.ViBotRaider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.listeners.GroupListener;
import com.github.waifu.gui.models.ReactTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
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
import org.json.JSONArray;

/**
 * ReactTable class to construct the UI for the table containing parsed reacts.
 */
public class ReactTable extends JFrame {

  /**
   * To be documented.
   */
  private GroupListener groupListener;
  /**
   * To be documented.
   */
  private JTable reactTable;
  /**
   * To be documented.
   */
  private JPanel reactPanel;
  /**
   * To be documented.
   */
  private JCheckBox removeGoodReactsCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeBadReactsCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeManualReactsCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeMarkedReactsCheckBox;
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeGoodReacts = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeBadReacts = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeManualReacts = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private TableRowSorter<TableModel> sorter;

  /**
   * ReactTable method.
   *
   * <p>Constructs a JFrame to display the react parse.
   */
  public ReactTable() {
    $$$setupUI$$$();
    createTable();
    addActionListeners();
    setAlwaysOnTop(true);
    setContentPane(reactPanel);
    setResizable(false);
    setTitle("OsancTools");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    setVisible(true);
    pack();
    new TableCopyAction(reactTable);

    groupListener = (account, exists) -> {
      final DefaultTableModel tableModel = (DefaultTableModel) reactTable.getModel();

    };

    Gui.getRaid().getGroup().addListener(groupListener);
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   */
  private void createTable() {

    final DefaultTableModel tableModel = new ReactTableModel();
    final List<React> reacts = Gui.getRaid().getReacts();
    for (final React react : reacts) {
      final JSONArray ids = react.getRaiderIds();
      for (int i = 0; i < ids.length(); i++) {
        final String id = ids.getString(i);
        final ViBotRaider viBotRaider = Gui.getRaid().getViBotRaiderById(id);
        if (viBotRaider == null) {
          System.out.println(id);
        }
        final Account account = Gui.getRaid().getGroup().getAccountByNickname(Utilities.parseUsernamesFromNickname(viBotRaider.getNickname()));
        tableModel.addRow(createRow(react, viBotRaider, account));
      }
    }
    sorter = new TableRowSorter<>(tableModel);
    reactTable.setRowSorter(sorter);
    reactTable.setModel(tableModel);
  }

  private Object[] createRow(final React react, final ViBotRaider viBotRaider, final Account account) {
    final Object[] row = new Object[6];
    row[0] = react.getName();
    row[1] = viBotRaider.getNickname();

    if (account != null) {
      row[2] = account.getName();
      final ImageIcon characterImage = account.getRecentCharacter().getCharacterImage();
      if (characterImage != null) {
        final int cWidth = characterImage.getIconWidth();
        reactTable.getColumnModel().getColumn(ReactTableModel.Column.INVENTORY.ordinal()).setMinWidth(cWidth);
      }
      row[3] = characterImage;
      row[4] = account.getRecentCharacter().getInventory().getIssue().getMessage();
    }
    row[5] = false;

    return row;
  }

  /**
   * addActionListeners method.
   *
   * <p>Constructs all listeners for the JFrame.
   */
  private void addActionListeners() {
    reactTable.addPropertyChangeListener(evt -> {
      final JTable editedTable = (JTable) evt.getSource();
      final int row = editedTable.getEditingRow();
      final int column = editedTable.getEditingColumn();

      if (column == 4) {
        final String username = (String) editedTable.getValueAt(row, 1);
        final Raider r = Gui.getRaid().findRaiderByUsername(username);
        final boolean newValue = (boolean) editedTable.getValueAt(row, column);
        for (final Account a : r.getAccounts()) {
          final int n = findRowValue(a.getName());
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
   * Method generated by IntelliJ IDEA Gui Designer.
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    reactPanel = new JPanel();
    reactPanel.setLayout(new GridLayoutManager(3, 3, new Insets(5, 5, 5, 5), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    reactPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Reacts", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    reactTable = new JTable();
    reactTable.setCellSelectionEnabled(true);
    scrollPane1.setViewportView(reactTable);
    removeGoodReactsCheckBox = new JCheckBox();
    removeGoodReactsCheckBox.setText("Remove Good Reacts");
    reactPanel.add(removeGoodReactsCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeManualReactsCheckBox = new JCheckBox();
    removeManualReactsCheckBox.setText("Remove Manual Reacts");
    reactPanel.add(removeManualReactsCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeBadReactsCheckBox = new JCheckBox();
    removeBadReactsCheckBox.setText("Remove Bad Reacts");
    reactPanel.add(removeBadReactsCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeMarkedReactsCheckBox = new JCheckBox();
    removeMarkedReactsCheckBox.setText("Remove Marked Reacts");
    reactPanel.add(removeMarkedReactsCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * To be documented.
   *
   * @noinspection ALL
   * @return To be documented.
   */
  public JComponent $$$getRootComponent$$$() {
    return reactPanel;
  }

  /**
   * updateFilters method.
   *
   * <p>Updates the current filters to the table.
   * This allows real-time updates to filtered rows upon changes.
   */
  private void updateFilters() {
    final List<RowFilter<Object, Object>> filters = new ArrayList<>();
    filters.add(this.removeGoodReacts);
    filters.add(this.removeBadReacts);
    filters.add(this.removeManualReacts);
    filters.add(this.removeMarked);
    this.sorter.setRowFilter(RowFilter.andFilter(filters));
  }

  private int findRowValue(final String value) {
    for (int i = 0; i < reactTable.getRowCount(); i++) {
      if (reactTable.getValueAt(i, 1).equals(value)) {
        return i;
      }
    }
    return -1;
  }
}
