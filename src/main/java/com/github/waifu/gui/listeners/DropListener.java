package com.github.waifu.gui.listeners;

import com.github.waifu.gui.AcceptFileFrame;
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
 * To be documented.
 */
public class DropListener implements DropTargetListener {

  /**
   * To be documented.
   */
  private final AcceptFileFrame frame;

  /**
   * To be documented.
   *
   * @param frame To be documented.
   */
  public DropListener(final AcceptFileFrame frame) {
    this.frame = frame;
  }

  /**
   * To be documented.
   *
   * @param dtde To be documented.
   */
  @Override
  public void dragEnter(final DropTargetDragEvent dtde) {
    frame.getLabel().setForeground(Color.BLUE);
  }

  /**
   * To be documented.
   *
   * @param dtde To be documented.
   */
  @Override
  public void dragOver(final DropTargetDragEvent dtde) {

  }

  /**
   * To be documented.
   *
   * @param dtde To be documented.
   */
  @Override
  public void dropActionChanged(final DropTargetDragEvent dtde) {
  }

  /**
   * To be documented.
   *
   * @param dte To be documented.
   */
  @Override
  public void dragExit(final DropTargetEvent dte) {
    frame.getLabel().setForeground(Color.BLACK);
  }

  /**
   * To be documented.
   *
   * @param dtde To be documented.
   */
  @Override
  public void drop(final DropTargetDropEvent dtde) {
    try {
      dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
      final Transferable transferable = dtde.getTransferable();
      final java.util.List<File> files = (java.util.List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
      final File file = files.get(0); // get first selected file if multiple
      if (Utilities.isImage(file)) {
        frame.getLabel().setText("");
        frame.setImage(ImageIO.read(files.get(0)));
        frame.getLabel().setIcon(new ImageIcon(frame.getImage().getScaledInstance(frame.getPanelWidth(), frame.getPanelHeight(), Image.SCALE_DEFAULT)));
      }
    } catch (final UnsupportedFlavorException | IOException e) {
      e.printStackTrace();
    }
  }
}
