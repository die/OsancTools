package com.github.waifu.gui.panels;

import com.github.waifu.entities.Raid;
import com.github.waifu.gui.AcceptFileFrame;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.GetWebAppDataAction;
import com.github.waifu.gui.actions.ParseWebAppReactsAction;
import com.github.waifu.gui.actions.ParseWebAppSetsAction;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.TitledBorder;
import org.json.JSONException;

/**
 * To be documented.
 */
public class HomePanel extends JPanel {

  /**
   * To be documented.
   */
  private final JPanel connectionPanel;
  /**
   * To be documented.
   */
  private final JPanel raidPanel;
  /**
   * To be documented.
   */
  private final JPanel descriptionPanel;
  /**
   * To be documented.
   */
  private final JPanel metadataPanel;
  /**
   * To be documented.
   */
  private final JPanel buttonPanel;
  /**
   * To be documented.
   */
  private final JPanel progressBarPanel;
  /**
   * To be documented.
   */
  private final JLabel raidLabel;
  /**
   * To be documented.
   */
  private final JLabel descriptionLabel;
  /**
   * To be documented.
   */
  private final JLabel metadataLabel;
  /**
   * To be documented.
   */
  private final JButton inputRaidButton;
  /**
   * To be documented.
   */
  private final JButton vcParseButton;
  /**
   * To be documented.
   */
  private final JButton parseReactsButton;
  /**
   * To be documented.
   */
  private final JButton parseSetsButton;
  /**
   * To be documented.
   */
  private final JButton stopButton;
  /**
   * To be documented.
   */
  private final JProgressBar progressBar;
  /**
   * To be documented.
   */
  private final TitledBorder border;

  /**
   * To be documented.
   */
  public HomePanel() {
    setLayout(new GridLayoutManager(2, 2, new Insets(5, 5, 5, 5), -1, -1));
    connectionPanel = new JPanel();
    connectionPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 5, 0, 0), -1, -1));
    add(connectionPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(300, 150), null, 0, false));
    connectionPanel.setBorder(BorderFactory.createTitledBorder(null, "Not Connected", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    raidPanel = new JPanel();
    raidPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connectionPanel.add(raidPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    raidLabel = new JLabel();
    raidLabel.setHorizontalAlignment(0);
    raidLabel.setHorizontalTextPosition(2);
    raidLabel.setText(" ");
    raidPanel.add(raidLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    descriptionPanel = new JPanel();
    descriptionPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connectionPanel.add(descriptionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    descriptionLabel = new JLabel();
    descriptionLabel.setHorizontalAlignment(0);
    descriptionLabel.setText(" ");
    descriptionPanel.add(descriptionLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
    metadataPanel = new JPanel();
    metadataPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    connectionPanel.add(metadataPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, 1, 1, null, null, null, 0, false));
    metadataLabel = new JLabel();
    metadataLabel.setHorizontalAlignment(0);
    metadataLabel.setText(" ");
    metadataPanel.add(metadataLabel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, 1, 1, null, null, null, 0, false));
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new GridLayoutManager(5, 1, new Insets(0, 0, 0, 5), -1, -1));
    add(buttonPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    inputRaidButton = new JButton();
    inputRaidButton.setText("Input Raid ID");
    buttonPanel.add(inputRaidButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vcParseButton = new JButton();
    vcParseButton.setActionCommand("Parse VC");
    vcParseButton.setLabel("Parse VC");
    vcParseButton.setText("Parse VC");
    buttonPanel.add(vcParseButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseReactsButton = new JButton();
    parseReactsButton.setText("Parse Reacts");
    buttonPanel.add(parseReactsButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseSetsButton = new JButton();
    parseSetsButton.setText("Parse Sets");
    buttonPanel.add(parseSetsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    stopButton = new JButton();
    stopButton.setHorizontalTextPosition(0);
    stopButton.setText("Stop Process");
    buttonPanel.add(stopButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    progressBarPanel = new JPanel();
    progressBarPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
    add(progressBarPanel, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    progressBar = new JProgressBar();
    progressBarPanel.add(progressBar, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    border = BorderFactory.createTitledBorder("None");
    border.setTitle("Not Connected");
    border.setTitleColor(Color.red);
    connectionPanel.setBorder(border);
    raidPanel.setOpaque(false);
    addActionListeners();
  }

  private void addActionListeners() {
    inputRaidButton.addActionListener(new GetWebAppDataAction(this));

    vcParseButton.addActionListener(e -> {
      final AcceptFileFrame acceptFileFrame = new AcceptFileFrame(this);
    });

    parseReactsButton.addActionListener(new ParseWebAppReactsAction(progressBar, stopButton, parseReactsButton));

    parseSetsButton.addActionListener(new ParseWebAppSetsAction(progressBar, stopButton, parseSetsButton));

    stopButton.addActionListener(e -> {
      final SwingWorker worker = Gui.getWorker();
      if (worker != null && !worker.isDone() && !worker.isCancelled()) {
        worker.cancel(true);
        stopButton.setText("Stopped");
      }
    });
  }

  /**
   * To be documented.
   */
  public void updateHomePanel() {
    try {
      final Raid raid = Gui.getRaid();
      if (raid != null) {
        border.setTitle("Connected");
        border.setTitleColor(Color.green);
        connectionPanel.setBorder(border);
        SwingUtilities.updateComponentTreeUI(connectionPanel);
        raidPanel.setOpaque(true);
        final Color color = new Color(raid.getJson().getInt("bg_color"));
        final Color invertedColor = Utilities.invertColor(color);
        raidPanel.setBackground(color);
        raidLabel.setForeground(invertedColor);
        raidLabel.setIcon(raid.getRaidLeader().getResizedAvatar(25, 25));
        raidLabel.setText(raid.getName() + " led by " + raid.getRaidLeader().getServerNickname());

        final String textId = "ID: " + raid.getId();
        final String textStatus = " Status: " + raid.getStatus();
        final String textLocation = " Location: " + raid.getLocation();
        final String raidMetadata = textId + textStatus + textLocation;
        metadataLabel.setText(raidMetadata);

        String raidDescription = raid.getDescription();
        if (raidDescription.length() > raidMetadata.length()) {
          raidDescription = raidDescription.substring(0, raidMetadata.length()) + "...";
        }
        descriptionLabel.setText(raidDescription);
        // todo: pack GUI if necessary.
      }
    } catch (final JSONException exception) {
      System.out.println("Cannot find assets from JSON, did you select the right file?");
    }
  }
}
