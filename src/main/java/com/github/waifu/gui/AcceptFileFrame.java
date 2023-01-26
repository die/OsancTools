package com.github.waifu.gui;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.listeners.DropListener;
import com.github.waifu.gui.panels.HomePanel;
import com.github.waifu.gui.tables.VcParse;
import com.github.waifu.handlers.WebAppHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class AcceptFileFrame extends JDialog {
  /**
   * To be documented.
   */
  private Image image;
  /**
   * To be documented.
   */
  private final int width;
  /**
   * To be documented.
   */
  private final int height;
  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JButton openFromFileButton;
  /**
   * To be documented.
   */
  private JButton chooseButton;
  /**
   * To be documented.
   */
  private JPanel panel;
  /**
   * To be documented.
   */
  private JLabel label;

  /**
   * Constructs the JFrame for getting a /who to parse the voice channel.
   *
   * @param homePanel HomePanel object to set this frame's location.
   */
  public AcceptFileFrame(final HomePanel homePanel) {
    setLocationRelativeTo(homePanel);
    setModal(true);
    createUIComponents();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
    setVisible(true);
    pack();

    width = panel.getWidth();
    height = panel.getHeight();
    final DropListener dropListener = new DropListener(this);
    panel.setDropTarget(new DropTarget(this, dropListener));
    panel.setBorder(BorderFactory.createDashedBorder(Color.gray, 7.0f, 2.0f));
    final AbstractAction actionListener = new AbstractAction() {
      @Override
      public void actionPerformed(final ActionEvent e) {
        try {
          if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.imageFlavor)) {
            image = (Image) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
            label.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            label.setText("");
            pack();
          }
        } catch (final UnsupportedFlavorException | IOException ex) {
          ex.printStackTrace();
        }
      }
    };

    main.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, true), "ctrl V");
    main.getRootPane().getActionMap().put("ctrl V", actionListener);

    openFromFileButton.addActionListener(e -> {
      final JFileChooser fc = new JFileChooser();
      final FileFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");

      fc.setFileFilter(filter);
      final int returnVal = fc.showOpenDialog(main);

      if (returnVal == JFileChooser.APPROVE_OPTION) {
        try {
          if (Utilities.isImage(fc.getSelectedFile())) {
            image = ImageIO.read(fc.getSelectedFile());
            label.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_DEFAULT)));
            label.setText("");
            pack();
          }
        } catch (final IOException ex) {
          ex.printStackTrace();
        }
      }
    });

    chooseButton.addActionListener(e -> {
      if (image == null) {
        return;
      }
      final JSONObject jsonObject = Gui.getJson();
      if (jsonObject == null) {
        final Component rootPane = Gui.getFrames()[0].getComponents()[0];
        JOptionPane.showMessageDialog(rootPane, "Failed to get members", "Error", JOptionPane.WARNING_MESSAGE);
        return;
      }
      final JSONObject newJson = WebAppHandler.getData(jsonObject.getJSONObject("raid").getInt("id"));
      assert newJson != null;
      final Raid raid = new Raid(newJson.getJSONObject("raid"));
      try {
        setVisible(false);
        final JFrame frame = new VcParse(image, raid.getRaiders());
        frame.setLocationRelativeTo(this);
        dispose();
      } catch (final TesseractException ex) {
        ex.printStackTrace();
      }
    });
  }

  /**
   * createUIComponents method.
   *
   * <p>Modifies custom components on launch.
   */
  private void createUIComponents() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
    panel = new JPanel();
    panel.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
    main.add(panel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(-4473925)), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    label = new JLabel();
    label.setHorizontalTextPosition(0);
    label.setText("Drag or paste /who here");
    panel.add(label, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    openFromFileButton = new JButton();
    openFromFileButton.setText("Open From File");
    main.add(openFromFileButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    chooseButton = new JButton();
    chooseButton.setText("Choose");
    main.add(chooseButton, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public JLabel getLabel() {
    return label;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getPanelWidth() {
    return width;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public int getPanelHeight() {
    return height;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public Image getImage() {
    return image;
  }

  /**
   * To be documented.
   *
   * @param image To be documented.
   */
  public void setImage(final Image image) {
    this.image = image;
  }
}
