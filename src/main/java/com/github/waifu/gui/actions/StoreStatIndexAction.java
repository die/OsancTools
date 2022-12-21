package com.github.waifu.gui.actions;

import com.github.waifu.enums.Stat;
import com.github.waifu.gui.Main;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Action to set the stat index on a button press.
 */
public class StoreStatIndexAction implements ActionListener {

  /**
   * Stat enum that stores indices.
   */
  private final Stat stat;

  /**
   * Construct action with stat enum.
   *
   * @param newStat Stat enum.
   */
  public StoreStatIndexAction(final Stat newStat) {
    this.stat = newStat;
  }

  /**
   * Set the stat index when button is pressed.
   *
   * @param e ActionEvent object.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    Main.getSettings().setStat(stat.getIndex());
  }
}
