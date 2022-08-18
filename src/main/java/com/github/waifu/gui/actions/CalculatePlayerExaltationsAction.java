package com.github.waifu.gui.actions;

import com.github.waifu.gui.GUI;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.util.ExaltCalculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.prefs.Preferences;

public class CalculatePlayerExaltationsAction implements ActionListener {

    private JTextField exaltsInput;
    private JLabel exaltsResult;

    public CalculatePlayerExaltationsAction(JTextField exaltsInput, JLabel exaltsResult) {
        this.exaltsInput = exaltsInput;
        this.exaltsResult = exaltsResult;
    }

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
                int completes = calculator.calculateCompletions(collection);
                if (completes >= Preferences.userRoot().getInt("requirement", 100)) {
                    exaltsResult.setForeground(Color.green);
                } else {
                    exaltsResult.setForeground(Color.red);
                }
                exaltsResult.setText(username + " has " + calculator.calculateCompletions(collection) + " completes. (>= " + Preferences.userRoot().getInt("requirement", 100) + ")");
            }
        }
    }
}
