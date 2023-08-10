package com.github.waifu.gui.panels;

import com.github.waifu.gui.tables.EquipListRenderer;
import com.github.waifu.gui.models.KeyPopModel;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 * To be documented.
 */
public class PopsPanel extends JPanel {

  /**
   * To be documented.
   */
  private static JTable table;

  /**
   * To be documented.
   *
   * @param preferredSize To be documented.
   */
  public PopsPanel(final Dimension preferredSize) {
    setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JScrollPane scrollPane = new JScrollPane();
    add(scrollPane, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK, GridConstraints.SIZEPOLICY_CAN_SHRINK, null, null, null, 0, false));
    table = new JTable();
    table.setCellSelectionEnabled(true);
    table.setModel(new KeyPopModel());
    table.getColumnModel().getColumn(0).setCellRenderer(new EquipListRenderer());
    scrollPane.setViewportView(table);
    setPreferredSize(preferredSize);
  }

  /**
   * To be documented.
   *
   * @param object To be documented.
   * @param popper To be documented.
   */
  public static void addPop(final Object object, final String popper) {
    final DefaultTableModel model = (DefaultTableModel) table.getModel();
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
    final Date date = Calendar.getInstance().getTime();
    final Object[] row = new Object[3];
    row[0] = object;
    row[1] = popper;
    row[2] = dateFormat.format(date);
    model.addRow(row);
    table.setModel(model);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public JTable getTable() {
    return table;
  }
}
