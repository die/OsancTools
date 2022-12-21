package com.github.waifu.gui.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;

/**
 * To be documented.
 */
public class TableCopyAction extends AbstractAction {

  /**
   * To be documented.
   */
  private final JTable jTable;

  /**
   * To be documented.
   *
   * @param table To be documented.
   */
  public TableCopyAction(final JTable table) {
    this.jTable = table;
    assignAction();
  }

  /**
   * To be documented.
   */
  private void assignAction() {
    jTable.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, true), "ctrl C");
    jTable.getRootPane().getActionMap().put("ctrl C", this);
  }

  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (jTable.getSelectedRow() >= 0) {
      final Object object = jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
      if (object instanceof String) {
        final String string = (String) jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
        if (!string.isEmpty()) {
          Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
        }
      }
    }
  }

}
