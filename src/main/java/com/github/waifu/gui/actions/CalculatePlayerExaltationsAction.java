package com.github.waifu.gui.actions;

import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.ExaltCalculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

/**
 *
 */
public class CalculatePlayerExaltationsAction implements ActionListener {

    private JTextField exaltsInput;
    private JLabel exaltsResult;

    /**
     *
     * @param exaltsInput
     * @param exaltsResult
     */
    public CalculatePlayerExaltationsAction(JTextField exaltsInput, JLabel exaltsResult) {
        this.exaltsInput = exaltsInput;
        this.exaltsResult = exaltsResult;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (exaltsInput.getText() != null) {
            String username = exaltsInput.getText();
            List<String[]> collection = null;
            try {
                if (username != null && !username.equals("") && username.chars().allMatch((Character::isAlphabetic)) && username.length() <= 10) {
                    collection = RealmeyeRequestHandler.GETExalts(username);
                    TimeUnit.SECONDS.sleep(1);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            if (collection != null) {
                ExaltCalculator calculator = new ExaltCalculator();
                int stat = calculator.getStat();
                String statName = switch (stat) {
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
                int completes = calculator.calculateCompletions(collection);
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
