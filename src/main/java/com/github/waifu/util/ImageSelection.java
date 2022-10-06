package com.github.waifu.util;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

/**
 *
 */
public class ImageSelection implements Transferable {

    private Image image;

    /**
     *
     */
    public ImageSelection() {

    }

    /**
     *
     * @return
     */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.imageFlavor };
    }

    /**
     *
     * @param flavor
     * @return
     */
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    /**
     *
     * @param flavor
     * @return
     */
    @Override
    public Object getTransferData(DataFlavor flavor) {
        return image;
    }
}
