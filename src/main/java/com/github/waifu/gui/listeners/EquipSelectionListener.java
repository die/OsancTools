package com.github.waifu.gui.listeners;

import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.gui.ChooseItem;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * To be documented.
 */
public class EquipSelectionListener implements ListSelectionListener {

  /**
   * To be documented.
   */
  private final ChooseItem chooseItem;
  /**
   * To be documented.
   */
  private final JTable table;

  /**
   * To be documented.
   *
   * @param chooseItem To be documented.
   * @param table To be documented.
   */
  public EquipSelectionListener(final ChooseItem chooseItem, final JTable table) {
    this.chooseItem = chooseItem;
    this.table = table;
  }

  @Override
  public void valueChanged(final ListSelectionEvent e) {
    if (e.getValueIsAdjusting()) {
      if (table.getSelectedRow() != -1 && table.getSelectedColumn() != -1) {
        Object value = table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
        if (value instanceof EquipXMLObject) {
          EquipXMLObject equipXMLObject = (EquipXMLObject) value;
          final int choice = JOptionPane.showConfirmDialog(ChooseItem.getWindows()[0], "Do you want to select " + equipXMLObject + "?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, equipXMLObject.getImage());
          if (choice == 0) {
            chooseItem.dispose();
          } else {
            table.getSelectionModel().clearSelection();
          }
        } else if (value instanceof PlayerXmlObject) {
          PlayerXmlObject playerXMLObject = (PlayerXmlObject) value;
          final int choice = JOptionPane.showConfirmDialog(ChooseItem.getWindows()[0], "Do you want to select " + playerXMLObject + "?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, playerXMLObject.getImage());
          if (choice == 0) {
            chooseItem.dispose();
          } else {
            table.getSelectionModel().clearSelection();
          }
        }
      }
    }
  }
}
