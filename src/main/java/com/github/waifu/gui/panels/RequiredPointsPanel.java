package com.github.waifu.gui.panels;

import com.github.waifu.handlers.RequirementSheetHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class RequiredPointsPanel extends JPanel {

  /**
   * Constructs a map filled with the classes and default points in a requirement sheet.
   */
  private final Map<String, Integer> map = new HashMap<>() {
    {
      final JSONObject jsonObject = RequirementSheetHandler.getRequirementSheet().getJSONObject("required points");
      for (final String keys : jsonObject.keySet()) {
        put(keys, jsonObject.getInt(keys));
      }
    }
  };

  /**
   * Constructs the GUI of the panel to be used in a JOptionPane.
   */
  public RequiredPointsPanel() {
    final int rows = 6;
    final int columns = 3;
    final JPanel grid = new JPanel();
    grid.setLayout(new GridLayout(rows, columns, 10, 10));
    int index = 0;
    final List<String> classes = map.keySet().stream().toList();
    for (int i = 0; i < rows; i++) {
      for (int j = 0; j < columns; j++) {
        final String classType = classes.get(index);
        final JLabel jLabel = new ClassLabel(classType);
        final int points = RequirementSheetHandler.getRequirementSheet().getJSONObject("required points").getInt(classType);
        jLabel.setText(String.valueOf(points));
        final ImageIcon icon = new ImageIcon(new ImageIcon(Utilities.getClassResource("images/skins/" + classType + ".png")).getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        jLabel.setIcon(icon);
        final JPanel panel = this;
        jLabel.addMouseListener(new MouseAdapter() {
          public void mouseClicked(final MouseEvent e) {
            try {
              final int number = Integer.parseInt(JOptionPane.showInputDialog(panel, "Input a number", points));
              final ClassLabel label = (ClassLabel) e.getSource();
              label.setText(String.valueOf(number));
              map.put(label.getClassType(), number);
            } catch (final NumberFormatException exception) {
              // ignored exception to catch invalid input when parsing int
            }
          }
        });
        grid.add(jLabel, new GridConstraints(i, j, i, j, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null));
        index++;
      }
    }
    add(grid);
    final JButton setAll = new JButton("Set All");
    setAll.addActionListener(e -> {
      try {
        final int number = Integer.parseInt(JOptionPane.showInputDialog(this, "Input a number", 4));
        for (Component c : grid.getComponents()) {
          if (c instanceof ClassLabel) {
            ((ClassLabel) c).setText(String.valueOf(number));
            map.put(((ClassLabel) c).classType, number);
          }
        }
      } catch (final Exception ignored) {
        // ignored
      }
    });
    add(setAll);
  }

  /**
   * To be documented.
   *
   * @return map containing class values from user input.
   */
  public Map<String, Integer> getMap() {
    return this.map;
  }

  /**
   * Custom JLabel to store RoTMG class associated with it.
   */
  private static class ClassLabel extends JLabel {
    /**
     * RotMG Class.
     */
    private final String classType;

    ClassLabel(final String classType) {
      this.classType = classType;
    }

    private String getClassType() {
      return this.classType;
    }
  }
}
