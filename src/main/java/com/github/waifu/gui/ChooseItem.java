package com.github.waifu.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.github.waifu.assets.RotmgAssets;
import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.gui.listeners.EquipSelectionListener;
import com.github.waifu.gui.tables.EquipListRenderer;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**
 * To be documented.
 */
public class ChooseItem extends JDialog {

  /**
   * To be documented.
   */
  private TableRowSorter<TableModel> sorter = new TableRowSorter<>();
  /**
   * To be documented.
   */
  private JTabbedPane tabbedPane1;
  /**
   * To be documented.
   */
  private JPanel panel1;
  /**
   * To be documented.
   */
  private JTextField textField1;
  /**
   * To be documented.
   */
  private JTable table1;
  /**
   * To be documented.
   */
  private JTable table2;
  /**
   * To be documented.
   */
  private JTable table3;
  /**
   * To be documented.
   */
  private JTable table4;
  /**
   * To be documented.
   */
  private JTable table5;

  public ChooseItem() {
    $$$setupUI$$$();
    createUIComponents();

    try {
      final String theme = Main.getSettings().getTheme();
      final LookAndFeel lookAndFeel = theme.equals("dark") ? new FlatDarkLaf() : new FlatLightLaf();
      UIManager.setLookAndFeel(lookAndFeel);
      FlatLaf.updateUI();
    } catch (final Exception e) {
      e.printStackTrace();
    }
    SwingUtilities.updateComponentTreeUI(getRootPane());
    add(panel1);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    pack();
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setModalityType(ModalityType.APPLICATION_MODAL);
    setLocationRelativeTo(null);
    setVisible(true);
    setFocusable(false);
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(2, 1, new Insets(5, 5, 5, 5), -1, -1));
    tabbedPane1 = new JTabbedPane();
    tabbedPane1.setTabLayoutPolicy(1);
    panel1.add(tabbedPane1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, 300), null, 0, false));
    final JPanel panel2 = new JPanel();
    panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Weapon", panel2);
    final JScrollPane scrollPane1 = new JScrollPane();
    panel2.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    table1 = new JTable();
    scrollPane1.setViewportView(table1);
    final JPanel panel3 = new JPanel();
    panel3.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Ability", panel3);
    final JScrollPane scrollPane2 = new JScrollPane();
    panel3.add(scrollPane2, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    table2 = new JTable();
    scrollPane2.setViewportView(table2);
    final JPanel panel4 = new JPanel();
    panel4.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Armor", panel4);
    final JScrollPane scrollPane3 = new JScrollPane();
    panel4.add(scrollPane3, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    table3 = new JTable();
    scrollPane3.setViewportView(table3);
    final JPanel panel5 = new JPanel();
    panel5.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Ring", panel5);
    final JScrollPane scrollPane4 = new JScrollPane();
    panel5.add(scrollPane4, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    table4 = new JTable();
    scrollPane4.setViewportView(table4);
    final JPanel panel6 = new JPanel();
    panel6.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    tabbedPane1.addTab("Class", panel6);
    final JScrollPane scrollPane5 = new JScrollPane();
    panel6.add(scrollPane5, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    table5 = new JTable();
    scrollPane5.setViewportView(table5);
    textField1 = new JTextField();
    panel1.add(textField1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return panel1;
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here

    table1.setTableHeader(null);
    table2.setTableHeader(null);
    table3.setTableHeader(null);
    table4.setTableHeader(null);
    table5.setTableHeader(null);
    DefaultTableModel weaponList = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
    weaponList.addColumn("Name");
    DefaultTableModel abilityList = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
    abilityList.addColumn("Name");
    DefaultTableModel armorList = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
    armorList.addColumn("Name");
    DefaultTableModel ringList = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
    ringList.addColumn("Name");
    DefaultTableModel classList = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int column) {
        //all cells false
        return false;
      }
    };
    classList.addColumn("Name");
    for (Map.Entry<Integer, EquipXMLObject> entry : RotmgAssets.equipXMLObjectList.entrySet()) {
      EquipXMLObject equipXMLObject = entry.getValue();
      if (equipXMLObject.isEquipment()) {
        switch (equipXMLObject.getEquipmentType()) {
          case 0 -> weaponList.addRow(new Object[]{equipXMLObject});
          case 1 -> abilityList.addRow(new Object[]{equipXMLObject});
          case 2 -> armorList.addRow(new Object[]{equipXMLObject});
          case 3 -> ringList.addRow(new Object[]{equipXMLObject});
        }

      }
    }

    for (PlayerXmlObject playerXMLObject : RotmgAssets.playerXmlObjectList) {
      classList.addRow(new Object[]{playerXMLObject});
    }

    table1.setModel(weaponList);
    table2.setModel(abilityList);
    table3.setModel(armorList);
    table4.setModel(ringList);
    table5.setModel(classList);
    table1.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());
    table2.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());
    table3.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());
    table4.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());
    table5.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());

    table1.getSelectionModel().addListSelectionListener(new EquipSelectionListener(this, table1));
    table2.getSelectionModel().addListSelectionListener(new EquipSelectionListener(this, table2));
    table3.getSelectionModel().addListSelectionListener(new EquipSelectionListener(this, table3));
    table4.getSelectionModel().addListSelectionListener(new EquipSelectionListener(this, table4));
    table5.getSelectionModel().addListSelectionListener(new EquipSelectionListener(this, table5));

    textField1.getDocument().addDocumentListener(new DocumentListener() {

      @Override
      public void insertUpdate(DocumentEvent e) {
        // put your filter code here upon data insertion
        if (textField1.getText().equals("")) {
          switch (tabbedPane1.getSelectedIndex()) {
            case 0 -> table1.setRowSorter(null);
            case 1 -> table2.setRowSorter(null);
            case 2 -> table3.setRowSorter(null);
            case 3 -> table4.setRowSorter(null);
          }
        } else {
          sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textField1.getText(), 0));
          //sorter.setSortsOnUpdates(true);
          switch (tabbedPane1.getSelectedIndex()) {
            case 0 -> {
              sorter.setModel(table1.getModel());
              table1.setRowSorter(sorter);
            }
            case 1 -> {
              sorter.setModel(table2.getModel());
              table2.setRowSorter(sorter);
            }
            case 2 -> {
              sorter.setModel(table3.getModel());
              table3.setRowSorter(sorter);
            }
            case 3 -> {
              sorter.setModel(table4.getModel());
              table4.setRowSorter(sorter);
            }
          }
        }
      }

      @Override
      public void removeUpdate(DocumentEvent e) {
        //put your filter code here upon data removal
        if (textField1.getText().equals("")) {
          switch (tabbedPane1.getSelectedIndex()) {
            case 0 -> table1.setRowSorter(null);
            case 1 -> table2.setRowSorter(null);
            case 2 -> table3.setRowSorter(null);
            case 3 -> table4.setRowSorter(null);
          }
        } else {
          sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textField1.getText(), 0));
          //sorter.setSortsOnUpdates(true);
          switch (tabbedPane1.getSelectedIndex()) {
            case 0 -> {
              sorter.setModel(table1.getModel());
              table1.setRowSorter(sorter);
            }
            case 1 -> {
              sorter.setModel(table2.getModel());
              table2.setRowSorter(sorter);
            }
            case 2 -> {
              sorter.setModel(table3.getModel());
              table3.setRowSorter(sorter);
            }
            case 3 -> {
              sorter.setModel(table4.getModel());
              table4.setRowSorter(sorter);
            }
          }
        }
      }

      @Override
      public void changedUpdate(DocumentEvent e) {
      }
    });
  }
}