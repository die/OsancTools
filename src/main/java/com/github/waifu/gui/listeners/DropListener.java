package com.github.waifu.gui.listeners;

import com.github.waifu.gui.AcceptFilePanel;
import com.github.waifu.util.Utilities;
import java.awt.Color;
import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 */
public class DropListener implements DropTargetListener {

  private final AcceptFilePanel frame;

  /**
   * @param frame
   */
  public DropListener(AcceptFilePanel frame) {
    this.frame = frame;
  }

  /**
   * @param dtde
   */
  @Override
  public void dragEnter(DropTargetDragEvent dtde) {
    frame.getLabel().setForeground(Color.BLUE);
  }

  /**
   * @param dtde
   */
  @Override
  public void dragOver(DropTargetDragEvent dtde) {

  }

  /**
   * @param dtde
   */
  @Override
  public void dropActionChanged(DropTargetDragEvent dtde) {
  }

  /**
   * @param dte
   */
  @Override
  public void dragExit(DropTargetEvent dte) {
    frame.getLabel().setForeground(Color.BLACK);
  }

  /**
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
