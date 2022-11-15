package com.github.waifu.debug;

import com.github.waifu.gui.GUI;
import com.github.waifu.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyListener implements java.awt.event.KeyListener {

    List<Character> gerpep = Arrays.asList('g', 'e', 'r', 'p', 'e', 'p');
    List<Character> keys = new ArrayList<>();

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        int index = keys.size();

        keys.add(e.getKeyChar());

        if (keys.equals(gerpep)) {
            ImageIcon icon = new ImageIcon(new ImageIcon(Utilities.getImageResource("bluecat.png")).getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
            JOptionPane.showMessageDialog(GUI.getFrames()[0], new JLabel("how did you get here?", icon, JLabel.TRAILING));
            keys.clear();
        } else if (keys.size() == gerpep.size() || !gerpep.contains(e.getKeyChar()) || !gerpep.get(index).equals(e.getKeyChar())) {
            keys.clear();
        }
    }
}
