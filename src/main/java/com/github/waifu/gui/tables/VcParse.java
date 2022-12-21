package com.github.waifu.gui.tables;

import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.models.VcTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Component;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoadLibs;

/**
 * To be documented.
 */
public class VcParse extends JFrame {

  /**
   * To be documented.
   */
  private JPanel main;
  /**
   * To be documented.
   */
  private JTable vcParseTable;
  /**
   * To be documented.
   */
  private JCheckBox showInGroup;
  /**
   * To be documented.
   */
  private JCheckBox removeCelestial;
  /**
   * To be documented.
   */
  private JCheckBox removeInVcCheckBox;
  /**
   * To be documented.
   */
  private JCheckBox removeMarkedRaidersCheckBox;
  /**
   * To be documented.
   */
  private boolean destroy = false;
  /**
   * To be documented.
   */
  private TableRowSorter<TableModel> sorter;
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> celestial = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> ingroup = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> invc = RowFilter.regexFilter("");
  /**
   * To be documented.
   */
  private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");

  /**
   * To be documented.
   *
   * @param image   To be documented.
   * @param raiders To be documented.
   * @throws TesseractException   To be documented.
   * @throws InterruptedException To be documented.
   */
  public VcParse(final Image image, final List<Raider> raiders) throws TesseractException {
    $$$setupUI$$$();
    createTable(image, raiders);
    if (destroy) {
      return;
    }
    addActionListeners();
    setContentPane(main);
    setAlwaysOnTop(true);
    setResizable(false);
    setTitle("OsancTools");
    setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setVisible(true);
    pack();
    new TableCopyAction(vcParseTable);
  }

  /**
   * createTable method.
   *
   * <p>Creates the table model, adds rows to the model, and applies the model to the table.
   *
   * @param image   image of the /who to parse
   * @param raiders list of members in the webapp
   */
  private void createTable(final Image image, final List<Raider> raiders) throws TesseractException {
    final BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
    bufferedImage.getGraphics().drawImage(image, 0, 0, null);
    final Tesseract instance = new Tesseract();
    instance.setLanguage("eng");
    instance.setDatapath(LoadLibs.extractTessResources("tessdata").getPath());
    instance.setOcrEngineMode(1);
    final String result = instance.doOCR(ImageHelper.convertImageToGrayscale(bufferedImage));
    if (!result.contains("Players online")) {
      final Component rootPane = Gui.getFrames()[0].getComponents()[0];
      if (raiders == null) {
        JOptionPane.showMessageDialog(rootPane, "Failed get members", "Error", JOptionPane.WARNING_MESSAGE);
        destroy = true;
      }
      JOptionPane.showMessageDialog(rootPane, "Failed to parse image", "Error", JOptionPane.WARNING_MESSAGE);
      destroy = true;
      this.dispose();
      return;
    }
    final List<String> temp = Arrays.asList(Arrays.asList(result.split(": ")).get(1).replace(",", "").replace("\n", " ").split(" "));
    final List<String> names = new ArrayList<>(temp);
    final DefaultTableModel tableModel = new VcTableModel();
    for (final Raider raider : raiders) {
      final String username = raider.getServerNickname();
      final List<String> usernames = Utilities.parseUsernamesFromNickname(raider.getServerNickname());
      String inGroup = "Not In Group";
      String inGroupUsername = "";
      String inVc = "Not In VC";
      for (final String s : usernames) {
        for (final String n : names) {
          if (n.equalsIgnoreCase(s)) {
            names.remove(n);
            inGroup = "In Group";
            inGroupUsername = s;
            break;
          }
        }
      }
      if (raider.isInVc()) {
        inVc = "In VC";
      }

      final Object[] array = new Object[6];
      array[0] = raider.getResizedAvatar(vcParseTable.getRowHeight(), vcParseTable.getRowHeight());
      array[1] = username;
      array[2] = inGroupUsername;
      array[3] = inGroup;
      array[4] = inVc;
      array[5] = false;
      tableModel.addRow(array);
    }
    vcParseTable.setDefaultRenderer(Object.class, new VcTableRenderer());
    sorter = new TableRowSorter<>(tableModel);
    vcParseTable.setRowSorter(sorter);
    vcParseTable.setModel(tableModel);
  }

  /**
   * addActionListeners method.
   *
   * <p>Constructs all listeners for the JFrame.
   */
  private void addActionListeners() {
    vcParseTable.addPropertyChangeListener(evt -> updateFilters());

    showInGroup.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (showInGroup.isSelected()) {
          ingroup = RowFilter.notFilter(RowFilter.regexFilter("Not In Group", 3));
          updateFilters();
        } else {
          ingroup = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeInVcCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeInVcCheckBox.isSelected()) {
          invc = RowFilter.regexFilter("Not In VC", 4);
          updateFilters();
        } else {
          invc = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeCelestial.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        final RowFilter<Object, Object> filter = new RowFilter<>() {
          @Override
          public boolean include(final Entry entry) {
            final String serverNickname = (String) entry.getValue(1);
            return !Gui.getRaid().findRaiderByServerNickname(serverNickname).isCelestial();
          }
        };
        if (removeCelestial.isSelected()) {
          celestial = filter;
          updateFilters();
        } else {
          celestial = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());

    removeMarkedRaidersCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        if (removeMarkedRaidersCheckBox.isSelected()) {
          removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 5));
          updateFilters();
        } else {
          removeMarked = RowFilter.regexFilter("");
          updateFilters();
        }
        return null;
      }
    }.execute());
  }

  /**
   * updateFilters method.
   *
   * <p>Updates the current filters to the table.
   * This allows real-time updates to filtered rows upon changes.
   */
  private void updateFilters() {
    final List<RowFilter<Object, Object>> filters = new ArrayList<>();
    filters.add(this.celestial);
    filters.add(this.ingroup);
    filters.add(this.invc);
    filters.add(this.removeMarked);
    this.sorter.setRowFilter(RowFilter.andFilter(filters));
  }

  /**
   * To be documented.
   */
  private void createUIComponents() {
    // todo: place custom component creation code here

  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer.
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    main = new JPanel();
    main.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    vcParseTable = new JTable();
    vcParseTable.setCellSelectionEnabled(true);
    scrollPane1.setViewportView(vcParseTable);
    showInGroup = new JCheckBox();
    showInGroup.setText("Show In Group");
    main.add(showInGroup, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeCelestial = new JCheckBox();
    removeCelestial.setText("Remove Celestial");
    main.add(removeCelestial, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeInVcCheckBox = new JCheckBox();
    removeInVcCheckBox.setText("Remove In VC");
    main.add(removeInVcCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    removeMarkedRaidersCheckBox = new JCheckBox();
    removeMarkedRaidersCheckBox.setText("Remove Marked Raiders");
    main.add(removeMarkedRaidersCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
  }

  /**
   * To be documented.
   *
   * @noinspection ALL
   * @return To be documented.
   */
  public JComponent $$$getRootComponent$$$() {
    return main;
  }

}
