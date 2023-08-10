package com.github.waifu.gui.actions;

import com.github.waifu.gui.tables.SetTable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * To be documented.
 */
public class ParseSetsAction implements ActionListener {
  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    new SetTable();
  }
}
