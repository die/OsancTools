package com.github.waifu.gui.actions;

import com.github.waifu.enums.Stat;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.prefs.Preferences;

/**
 *
 */
public class StoreStatIndexAction implements ActionListener {

    private final Stat stat;

    /**
     *
     * @param stat
     */
    public StoreStatIndexAction(Stat stat) {
        this.stat = stat;
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Preferences userPrefs = Preferences.userRoot();
        userPrefs.putInt("stat", stat.getIndex());
    }
}
