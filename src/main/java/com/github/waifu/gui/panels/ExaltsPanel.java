package com.github.waifu.gui.panels;

import com.github.waifu.enums.Stat;
import com.github.waifu.gui.Main;
import com.github.waifu.gui.actions.CalculatePlayerExaltationsAction;
import com.github.waifu.gui.actions.StoreStatIndexAction;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

/**
 * To be documented.
 */
public class ExaltsPanel extends JPanel {

  /**
   * To be documented.
   */
  private final JLabel enterUsernameLabel;
  /**
   * To be documented.
   */
  private final JLabel setRequirementLabel;
  /**
   * To be documented.
   */
  private final JLabel lifePotionImage;
  /**
   * To be documented.
   */
  private final JLabel manaPotionImage;
  /**
   * To be documented.
   */
  private final JLabel attackPotionImage;
  /**
   * To be documented.
   */
  private final JLabel defensePotionImage;
  /**
   * To be documented.
   */
  private final JLabel speedPotionImage;
  /**
   * To be documented.
   */
  private final JLabel dexterityPotionImage;
  /**
   * To be documented.
   */
  private final JLabel vitalityPotionImage;
  /**
   * To be documented.
   */
  private final JLabel wisdomPotionImage;
  /**
   * To be documented.
   */
  private final JLabel resultLabel;
  /**
   * To be documented.
   */
  private final JLabel exaltsResultLabel;
  /**
   * To be documented.
   */
  private final JTextField usernameTextField;
  /**
   * To be documented.
   */
  private final JTextField requirementTextField;
  /**
   * To be documented.
   */
  private final JButton setRequirementButton;
  /**
   * To be documented.
   */
  private final JButton calculateExaltsButton;
  /**
   * To be documented.
   */
  private final JPanel statSelection;
  /**
   * To be documented.
   */
  private final JRadioButton lifeRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton manaRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton attackRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton defenseRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton speedRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton dexterityRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton vitalityRadioButton;
  /**
   * To be documented.
   */
  private final JRadioButton wisdomRadioButton;
  /**
   * To be documented.
   */
  private final ButtonGroup buttonGroup;

  /**
   * To be documented.
   */
  public ExaltsPanel() {
    setLayout(new GridLayoutManager(8, 10, new Insets(5, 5, 5, 5), -1, -1));
    enterUsernameLabel = new JLabel();
    enterUsernameLabel.setText("Enter username:");
    add(enterUsernameLabel, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    usernameTextField = new JTextField();
    usernameTextField.setMargin(new Insets(2, 6, 2, 6));
    add(usernameTextField, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    calculateExaltsButton = new JButton();
    calculateExaltsButton.setText("Enter");
    add(calculateExaltsButton, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    requirementTextField = new JTextField();
    requirementTextField.setMargin(new Insets(2, 6, 2, 6));
    add(requirementTextField, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(150, -1), null, 0, false));
    setRequirementLabel = new JLabel();
    setRequirementLabel.setText("Set requirement:");
    add(setRequirementLabel, new GridConstraints(4, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    setRequirementButton = new JButton();
    setRequirementButton.setText("Set");
    add(setRequirementButton, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    statSelection = new JPanel();
    statSelection.setLayout(new GridLayoutManager(4, 4, new Insets(0, 5, 0, 0), -1, -1));
    add(statSelection, new GridConstraints(0, 2, 8, 8, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    statSelection.setBorder(BorderFactory.createTitledBorder(null, "Stats", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
    manaRadioButton = new JRadioButton();
    manaRadioButton.setHorizontalTextPosition(4);
    manaRadioButton.setText("");
    statSelection.add(manaRadioButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    manaPotionImage = new JLabel();
    manaPotionImage.setHorizontalTextPosition(2);
    manaPotionImage.setText("");
    statSelection.add(manaPotionImage, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    defensePotionImage = new JLabel();
    defensePotionImage.setHorizontalTextPosition(2);
    defensePotionImage.setText("");
    statSelection.add(defensePotionImage, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    defenseRadioButton = new JRadioButton();
    defenseRadioButton.setHorizontalTextPosition(4);
    defenseRadioButton.setText("");
    statSelection.add(defenseRadioButton, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    lifeRadioButton = new JRadioButton();
    lifeRadioButton.setHorizontalTextPosition(4);
    lifeRadioButton.setText("");
    statSelection.add(lifeRadioButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    dexterityRadioButton = new JRadioButton();
    dexterityRadioButton.setHorizontalTextPosition(4);
    dexterityRadioButton.setText("");
    statSelection.add(dexterityRadioButton, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    dexterityPotionImage = new JLabel();
    dexterityPotionImage.setHorizontalTextPosition(2);
    dexterityPotionImage.setText("");
    statSelection.add(dexterityPotionImage, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    wisdomPotionImage = new JLabel();
    wisdomPotionImage.setHorizontalTextPosition(2);
    wisdomPotionImage.setText("");
    statSelection.add(wisdomPotionImage, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    wisdomRadioButton = new JRadioButton();
    wisdomRadioButton.setHorizontalTextPosition(4);
    wisdomRadioButton.setText("");
    statSelection.add(wisdomRadioButton, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    attackRadioButton = new JRadioButton();
    attackRadioButton.setHorizontalTextPosition(4);
    attackRadioButton.setText("");
    statSelection.add(attackRadioButton, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    speedRadioButton = new JRadioButton();
    speedRadioButton.setHorizontalTextPosition(4);
    speedRadioButton.setText("");
    statSelection.add(speedRadioButton, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vitalityRadioButton = new JRadioButton();
    vitalityRadioButton.setText("");
    statSelection.add(vitalityRadioButton, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    lifePotionImage = new JLabel();
    lifePotionImage.setText("");
    statSelection.add(lifePotionImage, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    attackPotionImage = new JLabel();
    attackPotionImage.setHorizontalTextPosition(2);
    attackPotionImage.setText("");
    statSelection.add(attackPotionImage, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    speedPotionImage = new JLabel();
    speedPotionImage.setText("");
    statSelection.add(speedPotionImage, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    vitalityPotionImage = new JLabel();
    vitalityPotionImage.setText("");
    statSelection.add(vitalityPotionImage, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    exaltsResultLabel = new JLabel();
    exaltsResultLabel.setHorizontalAlignment(11);
    exaltsResultLabel.setHorizontalTextPosition(10);
    exaltsResultLabel.setText("Calculate completions for each exalt.");
    add(exaltsResultLabel, new GridConstraints(1, 0, 3, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    resultLabel = new JLabel();
    resultLabel.setText("Result:");
    resultLabel.setVerticalAlignment(0);
    add(resultLabel, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    buttonGroup = new ButtonGroup();
    buttonGroup.add(lifeRadioButton);
    buttonGroup.add(manaRadioButton);
    buttonGroup.add(defenseRadioButton);
    buttonGroup.add(dexterityRadioButton);
    buttonGroup.add(wisdomRadioButton);
    buttonGroup.add(vitalityRadioButton);
    buttonGroup.add(attackRadioButton);
    buttonGroup.add(speedRadioButton);
    lifePotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/life.png")));
    manaPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/mana.png")));
    attackPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/attack.png")));
    defensePotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/defense.png")));
    speedPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/speed.png")));
    dexterityPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/dexterity.png")));
    vitalityPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/vitality.png")));
    wisdomPotionImage.setIcon(new ImageIcon(Utilities.getClassResource("images/potions/wisdom.png")));
    switch (Main.getSettings().getStat()) {
      case 1 -> manaRadioButton.setSelected(true);
      case 2 -> attackRadioButton.setSelected(true);
      case 3 -> defenseRadioButton.setSelected(true);
      case 4 -> speedRadioButton.setSelected(true);
      case 5 -> dexterityRadioButton.setSelected(true);
      case 6 -> vitalityRadioButton.setSelected(true);
      case 7 -> wisdomRadioButton.setSelected(true);
      default -> lifeRadioButton.setSelected(true);
    }
    requirementTextField.setText(String.valueOf(Main.getSettings().getRequirement()));
    addActionListeners();
  }

  private void addActionListeners() {
    /*
      To be documented.
     */
    setRequirementButton.addActionListener(e -> Main.getSettings().setRequirement(Integer.parseInt(requirementTextField.getText())));

    /*
      To be documented.
     */
    calculateExaltsButton.addActionListener(new CalculatePlayerExaltationsAction(usernameTextField, exaltsResultLabel));

    /*
      To be documented.
     */
    lifeRadioButton.addActionListener(new StoreStatIndexAction(Stat.LIFE));

    /*
      To be documented.
     */
    manaRadioButton.addActionListener(new StoreStatIndexAction(Stat.MANA));

    /*
      To be documented.
     */
    attackRadioButton.addActionListener(new StoreStatIndexAction(Stat.ATTACK));

    /*
      To be documented.
     */
    defenseRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEFENSE));

    /*
      To be documented.
     */
    speedRadioButton.addActionListener(new StoreStatIndexAction(Stat.SPEED));

    /*
      To be documented.
     */
    dexterityRadioButton.addActionListener(new StoreStatIndexAction(Stat.DEXTERITY));

    /*
      To be documented.
     */
    vitalityRadioButton.addActionListener(new StoreStatIndexAction(Stat.VITALITY));

    /*
      To be documented.
     */
    wisdomRadioButton.addActionListener(new StoreStatIndexAction(Stat.WISDOM));
  }
}
