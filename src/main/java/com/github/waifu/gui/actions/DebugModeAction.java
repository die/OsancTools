package com.github.waifu.gui.actions;

import com.github.waifu.debug.DebugMenu;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class DebugModeAction extends AbstractAction {

    private JFrame debugMenu;

    @Override
    public void actionPerformed(ActionEvent e) {
        if (debugMenu == null || !debugMenu.isDisplayable()) {
            debugMenu = new DebugMenu();
        }
    }
}
