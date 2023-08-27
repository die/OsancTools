package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.ViBotRaider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.listeners.GroupListener;
import com.github.waifu.gui.listeners.RaidListener;
import com.github.waifu.gui.models.VcTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * To be documented.
 */
public class VcParse extends JFrame {

  private RaidListener raidListener;

  private GroupListener groupListener;

  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JTable vcParseTable;
  /**
   * To be documented.
   */
  private JCheckBox showCrashers;
  /**
   * To be documented.
   */
  private JCheckBox removeCelestial;
  /**
   * To be documented.
   */
  private JCheckBox showDeafened;
  /**
   * To be documented.
   */
  private JCheckBox removeMarkedRaidersCheckBox;
  private JButton copyCrashers;
  private JCheckBox showRaidMembersCheckBox;
  /**
   * To be documented.
   */
  private TableRowSorter<TableModel> sorter;
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> celestial = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> raidingVC = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> raidMember = RowFilter.regexFilter("");
  ;
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> deafened = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");

  /**
   * To be documented.
   */
  public VcParse() {
    $$$setupUI$$$();
    createTable();
    addActionListeners();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setVisible(true);
    pack();
    new TableCopyAction(vcParseTable);

    groupListener = (account, exists) -> {
      final DefaultTableModel tableModel = (DefaultTableModel) vcParseTable.getModel();

      if (exists && Gui.getRaid().getViBotRaider(account.getName()) != null) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
          if (tableModel.getValueAt(i, 1).equals(account.getName())) {
            ViBotRaider raider = Gui.getRaid().getViBotRaider(account.getName());
            tableModel.setValueAt(raider.getNickname(), i, 0);
            tableModel.setValueAt(raider.inVC() ? raider.getVoiceChannel().getName() : null, i, 2);
            tableModel.setValueAt(raider.isDeaf(), i, 3);
          }
        }
      } else if (!exists) {
        ViBotRaider viBotRaider = Gui.getRaid().getViBotRaider(account.getName());
        if (viBotRaider == null) {
          Object[] row = new Object[6];
          row[1] = account.getName();
          tableModel.addRow(row);
        } else {
          Object[] row = new Object[6];
          row[0] = viBotRaider.getNickname();
          row[1] = account.getName();
          row[2] = viBotRaider.inVC() ? viBotRaider.getVoiceChannel().getName() : null;
          row[3] = viBotRaider.isDeaf();
          row[4] = "None";
          row[5] = false;
          tableModel.addRow(row);
        }
      }
      vcParseTable.setModel(tableModel);
      updateFilters();
    };

    raidListener = (vibotRaider) -> {
      final DefaultTableModel tableModel = (DefaultTableModel) vcParseTable.getModel();
      for (int i = 0; i < tableModel.getRowCount(); i++) {
        if (vibotRaider.hasIGN(String.valueOf(tableModel.getValueAt(i, 1)))) {
          tableModel.setValueAt(vibotRaider.getNickname(), i, 0);
          tableModel.setValueAt(vibotRaider.inVC() ? vibotRaider.getVoiceChannel().getName() : null, i, 2);
          tableModel.setValueAt(vibotRaider.isDeaf(), i, 3);
        }
      }
      vcParseTable.setModel(tableModel);
      updateFilters();
    };

    Gui.getRaid().addListener(raidListener);
    Gui.getRaid().getGroup().addListener(groupListener);
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   */
  private void createTable() {
    final DefaultTableModel tableModel = new VcTableModel();
    for (final Account a : Gui.getRaid().getGroup().getAccounts()) {
      final ViBotRaider viBotRaider = Gui.getRaid().getViBotRaider(a.getName());
      if (viBotRaider == null) {
        Object[] row = new Object[6];
        row[1] = a.getName();
        tableModel.addRow(row);
      } else {
        Object[] row = new Object[6];
        row[0] = viBotRaider.getNickname();
        row[1] = a.getName();
        row[2] = viBotRaider.inVC() ? viBotRaider.getVoiceChannel().getName() : null;
        row[3] = viBotRaider.isDeaf();
        row[4] = "None";
        row[5] = false;
        tableModel.addRow(row);
      }

    }

    vcParseTable.setDefaultRenderer(Object.class, new VcTableRenderer());
    sorter = new TableRowSorter<>(tableModel);
    vcParseTable.setRowSorter(sorter);
    vcParseTable.setModel(tableModel);
  }

  /**
   * addActionListeners method.
   *
   * <p>Constructs all listeners for the JFrame.
   */
  private void addActionListeners() {
    vcParseTable.addPropertyChangeListener(evt -> updateFilters());

    showCrashers.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (showCrashers.isSelected()) {
          raidingVC = RowFilter.notFilter(RowFilter.regexFilter(Gui.getRaid().getVcName(), 2));
          updateFilters();
        } else {
          raidingVC = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    showDeafened.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (showDeafened.isSelected()) {
          deafened = RowFilter.regexFilter("true", 3);
          updateFilters();
        } else {
          deafened = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeCelestial.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeCelestial.isSelected()) {
          celestial = new RowFilter<>() {
            @Override
            public boolean include(final Entry entry) {
              final String ign = (String) entry.getValue(1);
              final Raid raid = Gui.getRaid();
              final ViBotRaider viBotRaider = raid.getViBotRaider(ign);
              if (viBotRaider == null) return true;
              return !viBotRaider.isCelestial();
            }
          };
          ;
          updateFilters();
        } else {
          celestial = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeMarkedRaidersCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeMarkedRaidersCheckBox.isSelected()) {
          removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 5));
          updateFilters();
        } else {
          removeMarked = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    showRaidMembersCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (showRaidMembersCheckBox.isSelected()) {
          raidMember = new RowFilter<Object, Object>() {
            @Override
            public boolean include(final Entry<?, ?> entry) {
              return entry.getValue(0) != null;
            }
          };
          updateFilters();
        } else {
          raidMember = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    copyCrashers.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        final DefaultTableModel tableModel = (DefaultTableModel) vcParseTable.getModel();

        StringJoiner names = new StringJoiner(" ");
        for (int i = 0; i < tableModel.getRowCount(); i++) {
          if (tableModel.getValueAt(i, 2) == null) {
            names.add(String.valueOf(tableModel.getValueAt(i, 1)));
          }
        }
        System.out.println(names.toString());
        final StringSelection stringSelection = new StringSelection(names.toString());
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
        return null;
      }
    }.execute());
  }

  /**
   * updateFilters method.
   *
   * <p>Updates the current filters to the table.
   * This allows real-time updates to filtered rows upon changes.
   */
  private void updateFilters() {
    final List<RowFilter<Object, Object>> filters = new ArrayList<>();
    filters.add(this.celestial);
    filters.add(this.raidingVC);
    filters.add(this.raidMember);
    filters.add(this.deafened);
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
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(4, 3, new Insets(5, 5, 5, 5), -1, -1));
    main.setPreferredSize(new Dimension(600, 700));
    final JScrollPane scrollPane1 = new JScrollPane();
    main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    scrollPane1.setBorder(BorderFactory.createTitledBorder(null, "VC", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    vcParseTable = new JTable();
    vcParseTable.setCellSelectionEnabled(true);
    scrollPane1.setViewportView(vcParseTable);
    showCrashers = new JCheckBox();
    showCrashers.setText("Show Crashers");
    main.add(showCrashers, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeCelestial = new JCheckBox();
    removeCelestial.setText("Remove Celestial");
    main.add(removeCelestial, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    showDeafened = new JCheckBox();
    showDeafened.setText("Show Deafened");
    main.add(showDeafened, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeMarkedRaidersCheckBox = new JCheckBox();
    removeMarkedRaidersCheckBox.setText("Remove Marked Raiders");
    main.add(removeMarkedRaidersCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    copyCrashers = new JButton();
    copyCrashers.setText("Copy Crasher Names");
    main.add(copyCrashers, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    showRaidMembersCheckBox = new JCheckBox();
    showRaidMembersCheckBox.setText("Show Raid Members");
    main.add(showRaidMembersCheckBox, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return main;
  }

}
