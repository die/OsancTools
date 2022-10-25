package com.github.waifu.gui.listeners;

import com.github.waifu.gui.AcceptFilePanel;
import com.github.waifu.util.Utilities;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.File;
import java.io.IOException;

/**
 *
 */
public class DropListener implements DropTargetListener {

    private AcceptFilePanel frame;

    /**
     *
     * @param frame
     */
    public DropListener(AcceptFilePanel frame) {
        this.frame = frame;
    }

    /**
     *
     * @param dtde
     */
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        frame.getLabel().setForeground(Color.BLUE);
    }

    /**
     *
     * @param dtde
     */
    @Override
    public void dragOver(DropTargetDragEvent dtde) {

    }

    /**
     *
     * @param dtde
     */
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    /**
     *
     * @param dte
     */
    @Override
    public void dragExit(DropTargetEvent dte) {
        frame.getLabel().setForeground(Color.BLACK);
    }

    /**
     *
     * @param dtde
     */
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
            Transferable transferable = dtde.getTransferable();
            java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
            File file = files.get(0); // get first selected file if multiple
            if (Utilities.isImage(file)) {
                frame.getLabel().setText("");
                frame.setImage(ImageIO.read(files.get(0)));
                frame.getLabel().setIcon(new ImageIcon(frame.getImage().getScaledInstance(frame.getPanelWidth(), frame.getPanelHeight(), Image.SCALE_DEFAULT)));
            }
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }
}
