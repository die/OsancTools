package com.github.waifu.debug;

import com.github.waifu.gui.GUI;
import com.github.waifu.util.Utilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DebugMenu extends JFrame implements KeyListener {

    List<Character> gerpep = Arrays.asList('g', 'e', 'r', 'p', 'e', 'p');
    List<Character> keys = new ArrayList<>();

    public DebugMenu() {
       // setContentPane();
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setIconImage(new ImageIcon(Utilities.getImageResource("Gravestone.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setMinimumSize(new Dimension(GUI.WIDTH, GUI.HEIGHT));
        setVisible(true);
        pack();
        addKeyListener(this);
    }

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
            System.out.println("bluecat");
            keys.clear();
        } else if (keys.size() == gerpep.size() || !gerpep.contains(e.getKeyChar()) || !gerpep.get(index).equals(e.getKeyChar())) {
            keys.clear();
        }
    }
}
