package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Group;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.entities.ViBotRaider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.ExportWhoAction;
import com.github.waifu.gui.actions.ParseGuildLeakersAction;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.listeners.GroupListener;
import com.github.waifu.gui.listeners.ViBotListener;
import com.github.waifu.gui.models.SetTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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
  private ViBotListener viBotListener;
  /**
   * To be documented.
   */
  private GroupListener groupListener;
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
  private JCheckBox removeRuneReactsCheckbox;
  /**
   * To be documented.
   */
  private JCheckBox showRaidMembersCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeMarkedSetsCheckBox;
  /**
   * To be documented.
   */
  private JButton parseGuildLeaksButton;
  /**
   * To be documented.
   */
  private JButton exportWhoButton;
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
  private RowFilter<Object, Object> raidFilter = RowFilter.regexFilter("");
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
   */
  public SetTable() {
    $$$setupUI$$$();
    createUIComponents();
    createTable();
    addActionListeners();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    setVisible(true);
    new TableCopyAction(setsTable);

    pack();

    groupListener = (account, exists) -> {
      final DefaultTableModel tableModel = (DefaultTableModel) setsTable.getModel();
      account.getRecentCharacter().parseCharacterInventory(account.getName());
      final int width = account.getRecentCharacter().getCharacterImage(account.getRecentCharacter().getInventory().getIssue().getColors()).getIconWidth();
      if (exists) {
        final int nameColumn = 1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
          if (!tableModel.getValueAt(i, nameColumn).equals(account.getName())) continue;
          final Character recentChar = account.getRecentCharacter();
          tableModel.setValueAt(recentChar.getInventory().getIssue().getProblem().getProblem(), i, 0);
          tableModel.setValueAt(recentChar.getCharacterImage(recentChar.getInventory().getIssue().getColors()), i, 2);
          tableModel.setValueAt(recentChar.getMaxedStatsImage(), i, 3);
          tableModel.setValueAt(recentChar.getInventory().getIssue().getWhisper(), i, SetTableModel.Column.MESSAGE.ordinal());
        }
      } else {
        final Object[] row = createRow(account);
        tableModel.addRow(row);
      }
      setsTable.getColumnModel().getColumn(2).setMinWidth(width);
      setsTable.setModel(tableModel);
      updateFilters();
    };

    Gui.getRaid().getGroup().addListener(groupListener);
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   */
  private void createTable() {
    setsTable.setModel(new SetTableModel());

    final DefaultTableModel tableModel = (DefaultTableModel) setsTable.getModel();
    sorter = new TableRowSorter<>(tableModel);
    setsTable.setRowSorter(sorter);

    final Group group = Gui.getRaid().getGroup();
    if (group.accounts.isEmpty()) return;

    for (final Account account : group.accounts) {
      account.getRecentCharacter().parseCharacterInventory(account.getName());
      final Object[] row = createRow(account);
      tableModel.addRow(row);
    }
    setsTable.setModel(tableModel);
    setsTable.setRowSorter(sorter);
  }

  private Object[] createRow(final Account account) {
    final Object[] row = new Object[6];
    row[0] = account.getRecentCharacter().getInventory().getIssue().getProblem().getProblem();
    row[1] = account.getName();

    final ImageIcon characterImage = account.getRecentCharacter().getCharacterImage(account.getRecentCharacter().getInventory().getIssue().getColors());
    if (characterImage != null) {
      final int cWidth = characterImage.getIconWidth();
      setsTable.getColumnModel().getColumn(2).setMinWidth(cWidth);
    }
    row[2] = account.getRecentCharacter().getCharacterImage(account.getRecentCharacter().getInventory().getIssue().getColors());

    final ImageIcon statsImage = account.getRecentCharacter().getMaxedStatsImage();
    if (statsImage != null) {
      final int sWidth = statsImage.getIconWidth();
      setsTable.getColumnModel().getColumn(3).setMinWidth(sWidth);

    }
    row[3] = statsImage;

    row[4] = account.getRecentCharacter().getInventory().getIssue().getWhisper();
    row[5] = false;
    return row;
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

    removeRuneReactsCheckbox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeRuneReactsCheckbox.isSelected()) {
          removeBadSets = new RowFilter<Object, Object>() {
            @Override
            public boolean include(final Entry<?, ?> entry) {
              final String ign = entry.getStringValue(1);
              final ViBotRaider viBotRaider = Gui.getRaid().getViBotRaider(ign);
              if (viBotRaider == null) return true;
              final List<React> reacts = Gui.getRaid().getReacts();
              for (final React r : reacts) {
                if (!(r.getName().equals("Sword Rune") || r.getName().equals("Shield Rune") || r.getName().equals("Helmet Rune"))) continue;
                if (r.hasRaiderId(viBotRaider.getId())) return false;
              }
              return true;
            }
          };
          updateFilters();
        } else {
          removeBadSets = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    showRaidMembersCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (showRaidMembersCheckBox.isSelected()) {
          raidFilter = new RowFilter<Object, Object>() {

            @Override
            public boolean include(final Entry<?, ?> entry) {
              final String ign = entry.getStringValue(SetTableModel.Column.USERNAME.ordinal());
              if (Gui.getRaid() == null || Gui.getRaid().getViBotRaiders() == null) return false;
              final ViBotRaider viBotRaider = Gui.getRaid().getViBotRaider(ign);
              return viBotRaider != null;
            }
          };
          updateFilters();
        } else {
          raidFilter = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeMarkedSetsCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeMarkedSetsCheckBox.isSelected()) {
          removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 5));
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
    main.setMinimumSize(new Dimension(-1, 177));
    main.setPreferredSize(new Dimension(600, 700));
    final JScrollPane scrollPane1 = new JScrollPane();
    main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "Sets", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    setsTable = new JTable();
    setsTable.setCellSelectionEnabled(true);
    scrollPane1.setViewportView(setsTable);
    removeGoodSetsCheckBox = new JCheckBox();
    removeGoodSetsCheckBox.setText("Remove Good Sets");
    main.add(removeGoodSetsCheckBox, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    showRaidMembersCheckBox = new JCheckBox();
    showRaidMembersCheckBox.setText("Show Raid Members");
    main.add(showRaidMembersCheckBox, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeRuneReactsCheckbox = new JCheckBox();
    removeRuneReactsCheckbox.setText("Remove Rune Reacts");
    main.add(removeRuneReactsCheckbox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeMarkedSetsCheckBox = new JCheckBox();
    removeMarkedSetsCheckBox.setText("Remove Marked Sets");
    main.add(removeMarkedSetsCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseGuildLeaksButton = new JButton();
    parseGuildLeaksButton.setText("Parse Guild Leaks");
    main.add(parseGuildLeaksButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    exportWhoButton = new JButton();
    exportWhoButton.setText("Export /who");
    main.add(exportWhoButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
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
    filters.add(this.raidFilter);
    filters.add(this.removeMarked);
    this.sorter.setRowFilter(RowFilter.andFilter(filters));
  }

  /**
   * To be documented.
   */
  private void createUIComponents() {
    parseGuildLeaksButton.addActionListener(new ParseGuildLeakersAction(main));
    parseGuildLeaksButton.setEnabled(Gui.getRaid().getCrashers() != null);
    exportWhoButton.addActionListener(new ExportWhoAction(main));
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

  @Override
  public void dispose() {
    super.dispose();
    final Group group = Gui.getRaid().getGroup();
    if (group == null || groupListener == null) return;
    group.removeListener(groupListener);
  }
}