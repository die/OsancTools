package com.github.waifu.gui.panels;

import com.github.waifu.entities.Raid;
import com.github.waifu.entities.React;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.actions.SnifferAction;
import com.github.waifu.gui.tables.ReactTable;
import com.github.waifu.gui.tables.SetTable;
import com.github.waifu.gui.tables.VcParse;
import com.github.waifu.handlers.DatabaseHandler;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
  private final JButton startSnifferButton;
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
  private final JButton resetRaid;
  /**
   * To be documented.
   */
  private final JButton setRaidButton;
  /**
   * To be documented.
   */
  private final JProgressBar progressBar;
  /**
   * To be documented.
   */
  private final TitledBorder border;
  /**
   * Gui reference.
   */
  private final Gui gui;

  /**
   * To be documented.
   *
   * @param gui instance.
   */
  public HomePanel(final Gui gui) {
    this.gui = gui;
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
    buttonPanel.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 5), -1, -1));
    add(buttonPanel, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    resetRaid = new JButton();
    resetRaid.setHorizontalTextPosition(0);
    resetRaid.setText("Reset Raid");
    buttonPanel.add(resetRaid, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    setRaidButton = new JButton();
    setRaidButton.setHorizontalTextPosition(0);
    setRaidButton.setText("Set Raid");
    buttonPanel.add(setRaidButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    startSnifferButton = new JButton();
    startSnifferButton.setText("Start Sniffer");
    buttonPanel.add(startSnifferButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vcParseButton = new JButton();
    vcParseButton.setActionCommand("Parse VC");
    vcParseButton.setLabel("Parse VC");
    vcParseButton.setText("Parse VC");
    buttonPanel.add(vcParseButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseReactsButton = new JButton();
    parseReactsButton.setText("Parse Reacts");
    buttonPanel.add(parseReactsButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    parseSetsButton = new JButton();
    parseSetsButton.setText("Parse Sets");
    buttonPanel.add(parseSetsButton, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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
    startSnifferButton.addActionListener(new SnifferAction());

    vcParseButton.addActionListener(e -> {
      try {
        new VcParse();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    });

    parseReactsButton.addActionListener(e -> {
      new ReactTable();
    });

    parseSetsButton.addActionListener(e-> new SetTable());

    resetRaid.addActionListener(e -> {
      final int option = JOptionPane.showConfirmDialog(Gui.getFrames()[0], "This erases current raid data, including all players sniffed. Are you sure?");
      if (option == JOptionPane.YES_OPTION) Gui.setRaid(new Raid());
      updateHomePanel();
    });

    setRaidButton.addActionListener(e -> {
      final Map<String, JSONObject> map = DatabaseHandler.getAFKChecks();
      if (map == null) return;
      final Object selectionObject = JOptionPane.showInputDialog(Gui.getFrames()[0], "Choose", "Menu", JOptionPane.PLAIN_MESSAGE, null, map.keySet().toArray(), null);

      if (selectionObject == null) return;

      final String selection = (String) selectionObject;
      final JSONObject raid = map.get(selectionObject);

      // reacts
      System.out.println(raid.getJSONObject("reactables"));
      if (raid.has("reactables")) {
        final JSONObject reactables = raid.getJSONObject("reactables");
        for (final String key : reactables.keySet()) {
          if (reactables.getJSONObject(key).has("members")) {
            Gui.getRaid().addReact(new React(key, reactables.getJSONObject(key).getJSONArray("members")));
          }
        }
      }

      Gui.getRaid().setName(selection);
      if (raid.getJSONObject("raidStatusMessage").getJSONArray("embeds").getJSONObject(0).has("color")) {
        Gui.getRaid().setColor(Color.decode(String.valueOf(raid.getJSONObject("raidStatusMessage").getJSONArray("embeds").getJSONObject(0).getInt("color"))));
      } else {
        Gui.getRaid().setColor(Color.BLACK);
      }

      Gui.getRaid().setVcName(raid.isNull("channel") ? null : raid.getJSONObject("channel").getString("name"));
      final String guild = raid.getJSONObject("raidStatusMessage").getString("guildId");
      Gui.getRaid().setGuild(guild);

      if (raid.has("time")) {
        Gui.getRaid().setStartTime(new Date(raid.getLong("time")).toString());
      }
      System.out.println(raid.toString(4));

      final JSONArray members = raid.getJSONArray("members");
      final JSONArray earlySlotMembers = raid.getJSONArray("earlySlotMembers");
      final List<Object> memberIds = members.toList();
      for (int i = 0; i < earlySlotMembers.length(); i++) {
        final String id = earlySlotMembers.getString(i);
        if (memberIds.contains(id)) continue;
        members.put(id);
      }
      DatabaseHandler.getDiscordMembers(guild, members);
      updateHomePanel();
    });

  }

  /**
   * To be documented.
   */
  public void updateHomePanel() {
    try {
      final Raid raid = Gui.getRaid();
      if (raid.getName().equals("")) {
        border.setTitle("Not Connected");
        border.setTitleColor(Color.RED);
        connectionPanel.setBorder(border);
        SwingUtilities.updateComponentTreeUI(connectionPanel);
        raidPanel.setOpaque(true);
        raidPanel.setBackground(null);
        raidLabel.setForeground(null);
        raidLabel.setText("");
        metadataLabel.setText("");
        descriptionLabel.setText("");
        gui.pack();
      } else {
        border.setTitle("Connected");
        border.setTitleColor(Color.green);
        connectionPanel.setBorder(border);
        SwingUtilities.updateComponentTreeUI(connectionPanel);
        raidPanel.setOpaque(true);
        final Color color = raid.getColor();
        final Color invertedColor = Utilities.invertColor(color);
        raidPanel.setBackground(color);
        raidLabel.setForeground(invertedColor);
        final String name = Gui.getRaid().getName();
        raidLabel.setText(name);
        final String raidMetadata = "<html><center>" + "Members: " + raid.getViBotRaiders().size() + "<br>";
        metadataLabel.setText(raidMetadata);
        descriptionLabel.setText("<html><center>" + Gui.getRaid().getGuild() + " Discord <br> Started at: " + Gui.getRaid().getStartTime());
        gui.pack();
      }
    } catch (final JSONException exception) {
      exception.printStackTrace();
    }
  }
}
