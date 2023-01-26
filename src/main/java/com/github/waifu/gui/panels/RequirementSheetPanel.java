package com.github.waifu.gui.panels;

import com.github.waifu.gui.Main;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Insets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * To be documented.
 */
public class RequirementSheetPanel extends JPanel {

  /**
   * To be documented.
   */
  private final JComboBox requirementSheetComboBox;
  /**
   * To be documented.
   */
  private final JScrollPane scrollPane;
  /**
   * To be documented.
   */
  private final JTextArea requirementSheetTextArea;
  /**
   * To be documented.
   */
  private final JButton changeClassPointsButton;

  /**
   * To be documented.
   */
  public RequirementSheetPanel() {
    setLayout(new GridLayoutManager(5, 2, new Insets(5, 5, 5, 5), -1, -1));
    requirementSheetComboBox = new JComboBox();
    add(requirementSheetComboBox, new GridConstraints(1, 1, 4, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    scrollPane = new JScrollPane();
    add(scrollPane, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    requirementSheetTextArea = new JTextArea();
    requirementSheetTextArea.setEditable(false);
    requirementSheetTextArea.setLineWrap(true);
    requirementSheetTextArea.setRows(10);
    scrollPane.setViewportView(requirementSheetTextArea);
    changeClassPointsButton = new JButton();
    changeClassPointsButton.setText("Modify Class Points");
    add(changeClassPointsButton, new GridConstraints(1, 0, 4, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final List<String> requirementSheets = Main.getSettings().getRequirementSheets();
    final String selectedSheet = Main.getSettings().getRequirementSheetName();
    for (final String s : requirementSheets) {
      requirementSheetComboBox.addItem(s);
      if (selectedSheet.equals(s)) {
        requirementSheetComboBox.setSelectedIndex(requirementSheets.indexOf(s));
      }
    }
    requirementSheetTextArea.setEnabled(false);
    requirementSheetTextArea.setText(Utilities.getJson().toString(4));
    changeClassPointsButton.setEnabled(Utilities.getJson().has("required points"));
    addActionListeners();
  }

  private void addActionListeners() {
    /*
      To be documented.
     */
    changeClassPointsButton.addActionListener(e -> {
      RequiredPointsPanel requiredPointsPanel = new RequiredPointsPanel();
      int confirm = JOptionPane.showConfirmDialog(this, requiredPointsPanel, "<html><b>Click on a class to change points</b>", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

      if (confirm == JOptionPane.OK_OPTION) {
        Map<String, Integer> map = requiredPointsPanel.getMap();

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
          String key = entry.getKey();
          Object value = entry.getValue();
          Utilities.getJson().getJSONObject("required points").put(key, value);
          requirementSheetTextArea.setText(Utilities.getJson().toString(4));
        }
      }
    });

    /*
      To be documented.
     */
    requirementSheetComboBox.addActionListener(e -> {
      final JComboBox comboBox = (JComboBox) e.getSource();
      Main.getSettings().setRequirementSheetName(String.valueOf(comboBox.getSelectedItem()));
      try {
        Main.loadRequirementSheet();
        TimeUnit.SECONDS.sleep(1);
        requirementSheetTextArea.setText(Utilities.getJson().toString(4));
        changeClassPointsButton.setEnabled(Utilities.getJson().has("required points"));
      } catch (final Exception ex) {
        ex.printStackTrace();
      }
    });
  }
}
