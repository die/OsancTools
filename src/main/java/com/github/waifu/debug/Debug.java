package com.github.waifu.debug;

import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Debug extends JFrame {
  private static JTextArea textArea;
  private JPanel main;

  public Debug() {
    if (textArea == null) {
      create();
    }
    setAlwaysOnTop(true);
    setPreferredSize(new Dimension(200, 200));
    setResizable(false);
    setTitle("OsancTools");
    setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    setIconImage(new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")).getImage());
    pack();
    setVisible(true);
  }

  private void create() {
    final JPanel panel1 = new JPanel();
    panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    final JScrollPane scrollPane1 = new JScrollPane();
    panel1.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    textArea = new JTextArea();
    textArea.setEditable(false);
    textArea.setLineWrap(true);
    textArea.setRows(10);
    scrollPane1.setViewportView(textArea);
    add(panel1);
  }

  /**
   * To be documented.
   *
   * @param stacktrace to be documented.
   */
  public static void printStacktrace(final String stacktrace) {
    if (textArea == null) return;

    textArea.append(stacktrace);
  }
}
