package com.github.waifu.gui;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.listeners.DropListener;
import com.github.waifu.gui.tables.VCParse;
import com.github.waifu.handlers.WebAppHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import net.sourceforge.tess4j.TesseractException;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;

/**
 *
 */
public class AcceptFilePanel extends JFrame {
    private JPanel main;
    private JButton openFromFileButton;
    private JButton chooseButton;
    private JPanel panel;
    private JLabel label;
    public Image image;
    public int width;
    public int height;

    /**
     *
     */
    AcceptFilePanel() {
        setContentPane(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setIconImage(new ImageIcon(Utilities.getImageResource("Gravestone.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        setVisible(true);
        pack();

        width = panel.getWidth();
        height = panel.getHeight();
        DropListener dropListener = new DropListener(this);
        panel.setDropTarget(new DropTarget(this, dropListener));
        panel.setBorder(BorderFactory.createDashedBorder(Color.gray, 7.0f, 2.0f));
        AbstractAction actionListener = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (Toolkit.getDefaultToolkit().getSystemClipboard().isDataFlavorAvailable(DataFlavor.imageFlavor)) {
                        image = (Image) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.imageFlavor);
                        label.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_SMOOTH)));
                        label.setText("");
                        pack();
                    }
                } catch (UnsupportedFlavorException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        };

        main.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_DOWN_MASK, true), "ctrl V");
        main.getRootPane().getActionMap().put("ctrl V", actionListener);

        openFromFileButton.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            FileFilter filter = new FileNameExtensionFilter("Images (*.jpg, *.jpeg, *.png)", "jpg", "jpeg", "png");

            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(main);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                try {
                    if (Utilities.isImage(fc.getSelectedFile())) {
                        image = ImageIO.read(fc.getSelectedFile());
                        label.setIcon(new ImageIcon(image.getScaledInstance(width, height, Image.SCALE_DEFAULT)));
                        label.setText("");
                        pack();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        chooseButton.addActionListener(e -> {
            JSONObject jsonObject = GUI.getJson();
            if (jsonObject == null) {
                Component rootPane = GUI.getFrames()[0].getComponents()[0];
                JOptionPane.showMessageDialog(rootPane, "Failed to get members", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
            JSONObject newJson = WebAppHandler.getData(jsonObject.getJSONObject("raid").getInt("id"));
            assert newJson != null;
            Raid raid = new Raid(newJson.getJSONObject("raid"));
            try {
                setVisible(false);
                JFrame frame = new VCParse(image, raid.getRaiders());
                frame.setLocationRelativeTo(this);
                dispose();
            } catch (IOException | TesseractException | InterruptedException ex) {
                ex.printStackTrace();
            }
        });
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
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
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    /**
     *
     * @return
     */
    public JLabel getLabel() {
        return label;
    }

    /**
     *
     * @return
     */
    public int getPanelWidth() {
        return width;
    }

    /**
     *
     * @return
     */
    public int getPanelHeight() {
        return height;
    }

    /**
     *
     * @param image
     */
    public void setImage(Image image) {
        this.image = image;
    }

    /**
     *
     * @return
     */
    public Image getImage() {
        return image;
    }
}
