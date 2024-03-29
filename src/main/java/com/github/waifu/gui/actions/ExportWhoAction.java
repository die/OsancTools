package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Group;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.handlers.DatabaseHandler;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Class for exporting names in a /who format.
 */
public class ExportWhoAction implements ActionListener {

  /**
   * Class for copying images into clipboard.
   */
  private static class ImageTransferable implements Transferable {

    /**
     * Image to copy to the clipboard.
     */
    private final Image image;

    /**
     * Constructor.
     *
     * @param image store image.
     */
    ImageTransferable(final Image image) {
      this.image = image;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[]{DataFlavor.imageFlavor};
    }

    @Override
    public boolean isDataFlavorSupported(final DataFlavor flavor) {
      return flavor == DataFlavor.imageFlavor;
    }

    @Override
    public Object getTransferData(final DataFlavor flavor) {
      if (isDataFlavorSupported(flavor)) {
        return image;
      }
      return null;
    }
  }

  /**
   * Panel for the SetTable.
   */
  private final JPanel setTablePanel;

  /**
   * Construct action.
   *
   * @param setTablePanel table panel.
   */
  public ExportWhoAction(final JPanel setTablePanel) {
    this.setTablePanel = setTablePanel;
  }

  /**
   * Create action.
   *
   * @param e ignored event.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    final Raid raid = Gui.getRaid();
    if (raid == null) return;

    final Group group = Gui.getRaid().getGroup();
    if (group == null) return;

    final StringBuilder names = new StringBuilder();
    StringBuilder line = new StringBuilder();
    int numberOfPlayers = 0;

    if (group.getAccounts() != null && !group.getAccounts().isEmpty()) {
      final List<Account> raiders = group.getAccounts();

      for (final Account account : raiders) {
        // names who are not picked up by the sniffer (possibly not in loc), using the webapp, will show as a private profile, so we must filter them out.
        if (account.getCharacters() != null && !account.getCharacters().isEmpty() && !account.getCharacters().get(0).isPrivate()) {
          if (line.length() == 0) {
            line.append(account.getName());
          } else {
            line.append(", ").append(account.getName());
          }

          if (line.length() > 70) {
            line.append("\n");
            names.append(line);
            line = new StringBuilder();
          }
          numberOfPlayers++;
        }
      }

      if (!names.toString().contains(line)) {
        names.append(line);
      }
    }

    final StringBuilder full = new StringBuilder("Players online (" + numberOfPlayers + "): ").append(names);

    BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    Graphics graphics = image.createGraphics();
    int y = 0;
    for (final String s : full.toString().split("\n")) {
      graphics.drawString(s, 0, y += graphics.getFontMetrics().getHeight());
    }
    final FontMetrics metrics = graphics.getFontMetrics();
    final int margin = 10;
    final int height = metrics.getHeight() * full.toString().split("\n").length + margin;
    final int width = metrics.stringWidth(full.toString().split("\n")[0]) + margin;
    graphics.dispose();
    image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    graphics  = image.getGraphics();
    graphics.setColor(Color.decode("#CCCC00"));
    y = 0;
    for (final String s : full.toString().split("\n")) {
      graphics.drawString(s, margin / 2, y += graphics.getFontMetrics().getHeight());
    }
    graphics.dispose();
    final ImageTransferable imageTransferable = new ImageTransferable(image);
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(imageTransferable, null);
    JOptionPane.showMessageDialog(setTablePanel, "Exported /who image into the clipboard.", "", JOptionPane.PLAIN_MESSAGE);
  }
}
