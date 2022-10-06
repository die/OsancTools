package com.github.waifu.gui.actions;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 *
 */
public class TableCopyAction extends AbstractAction {

    private final JTable jTable;

    /**
     *
     * @param jTable
     */
    public TableCopyAction(JTable jTable) {
        this.jTable = jTable;
        assignAction();
    }

    /**
     *
     */
    private void assignAction() {
        jTable.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_DOWN_MASK, true), "ctrl C");
        jTable.getRootPane().getActionMap().put("ctrl C", this);
    }

    /**
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (jTable.getSelectedRow() >= 0) {
            Object object = jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
            if (object instanceof String) {
                String string = (String) jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
                if (!string.isEmpty()) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(string), null);
                }
            } /*else if (object instanceof ImageIcon) {
                ImageIcon imageIcon = (ImageIcon) jTable.getValueAt(jTable.getSelectedRow(), jTable.getSelectedColumn());
                if (imageIcon != null) {
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new ImageSelection(imageIcon.getImage()), null);
                }
            }*/
        }
    }

}
