package com.github.waifu.gui.actions;

import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.ExaltCalculator;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * To be documented.
 */
public class CalculatePlayerExaltationsAction implements ActionListener {

  /**
   * To be documented.
   */
  private final JTextField exaltsInput;
  /**
   * To be documented.
   */
  private final JLabel exaltsResult;

  /**
   * To be documented.
   *
   * @param exaltsInput To be documented.
   * @param exaltsResult To be documented.
   */
  public CalculatePlayerExaltationsAction(final JTextField exaltsInput, final JLabel exaltsResult) {
    this.exaltsInput = exaltsInput;
    this.exaltsResult = exaltsResult;
  }

  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (exaltsInput.getText() != null) {
      final String username = exaltsInput.getText();
      List<String[]> collection = null;
      try {
        if (username != null && !username.equals("") && username.chars().allMatch((Character::isAlphabetic)) && username.length() <= 15) {
          collection = RealmeyeRequestHandler.GETExalts(username);
          TimeUnit.SECONDS.sleep(1);
        } else {
          exaltsResult.setForeground(Color.yellow);
          exaltsResult.setText("Invalid IGN (above 15 characters)");
        }
      } catch (final Exception ex) {
        ex.printStackTrace();
      }
      if (collection != null) {
        final ExaltCalculator calculator = new ExaltCalculator();
        final int stat = calculator.getStat();
        final String statName = switch (stat) {
          case 0 -> "Oryx 3";
          case 1 -> "Void";
          case 2 -> "Shatters";
          case 3 -> "MBC";
          case 4 -> "Cult";
          case 5 -> "Nest";
          case 6 -> "Cavern";
          case 7 -> "Fungal";
          default -> "";
        };
        final int completes = calculator.calculateCompletions(collection);
        if (completes >= Preferences.userRoot().getInt("requirement", 100)) {
          exaltsResult.setForeground(Color.green);
        } else {
          exaltsResult.setForeground(Color.red);
        }
        exaltsResult.setText(username + " has " + calculator.calculateCompletions(collection) + " " + statName + " completes. (>= " + Preferences.userRoot().getInt("requirement", 100) + ")");
      }
    }
  }
}
