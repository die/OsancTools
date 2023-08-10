package com.github.waifu.gui.tables;

import com.github.waifu.assets.objects.EquipXMLObject;
import com.github.waifu.assets.objects.PlayerXmlObject;
import com.github.waifu.assets.objects.PortalXmlObject;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * To be documented.
 */
public class EquipListRenderer extends DefaultTableCellRenderer {

  @Override
  public Component getTableCellRendererComponent(
          final JTable table, final Object value,
          final boolean isSelected, final boolean hasFocus, final int row, final int column) {

    super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

    if (value instanceof final EquipXMLObject equipXmlObject) {
      setText(equipXmlObject.toString());
      setIcon(equipXmlObject.getImage());
      return this;
    } else if (value instanceof final PlayerXmlObject playerXmlObject) {
      setText(playerXmlObject.toString());
      setIcon(playerXmlObject.getImage());
      return this;
    } else if (value instanceof final PortalXmlObject portalXmlObject) {
      setText(portalXmlObject.toString());
      setIcon(portalXmlObject.getImage());
      return this;
    }
    return this;
  }
}
