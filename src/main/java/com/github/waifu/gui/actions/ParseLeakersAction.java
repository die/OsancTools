package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Parse leakers.
 */
public class ParseLeakersAction implements ActionListener {
  /**
   * Panel for the SetTable.
   */
  private final JPanel setTablePanel;

  /**
   * Construct action.
   *
   * @param setTablePanel table panel.
   */
  public ParseLeakersAction(final JPanel setTablePanel) {
    this.setTablePanel = setTablePanel;
  }

  /**
   * Create action.
   *
   * @param e ignored event.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (Gui.getRaid() != null) {
      final Raid raid = Gui.getRaid();
      final StringBuilder names = new StringBuilder();
      StringBuilder line = new StringBuilder();
      int numberOfPlayers = 0;

      if (raid.getRaiders() != null && !raid.getRaiders().isEmpty()) {
        return;
      }
    }
  }
}
