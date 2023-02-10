package com.github.waifu.enums;

import java.awt.Color;

/**
 * Contains all types of problems that can occur.
 */
public enum Problem {

  /**
   * All items are empty slots, labeled as a Private Profile.
   */
  PRIVATE_PROFILE("Private Profile", 0, Color.WHITE),
  /**
   * When a character isn't maxed in stats.
   */
  NOT_MAXED("Not Maxed", 1, Color.RED),
  /**
   * The react was not fulfilled.
   */
  MISSING_REACT("Missing React", 1, Color.RED),
  /**
   * The class react was not fulfilled.
   */
  MISSING_REACT_CLASS("Missing React Class", 1, Color.RED),
  /**
   * The class react was not fulfilled because there was no T6 item equipped.
   */
  MISSING_REACT_CLASS_ABILITY_T6("Missing T6 Ability", 1, Color.RED),
  /**
   * The trade react was not fulfilled.
   */
  MISSING_REACT_TRADE("Missing React Trade", 1, Color.RED),
  /**
   * The dps react was not fulfilled.
   */
  MISSING_REACT_DPS("Missing React Dps", 1, Color.RED),
  /**
   * An empty slot was found.
   */
  EMPTY_SLOT("Empty Slot", 1, Color.RED),
  /**
   * An item that is under requirements was found.
   */
  UNDER_REQS("Under Reqs", 1, Color.RED),
  /**
   * An item that is banned was found.
   */
  BANNED_ITEM("Banned Item", 2, Color.RED),
  /**
   * An item that is a swap-out was found.
   */
  SWAPOUT_ITEM("Swapout Item", 4, Color.YELLOW),
  /**
   * An item didn't count as any points.
   */
  POINTS("Missing Points", 3, Color.ORANGE),
  /**
   * There was no problem.
   */
  NONE("None", 5, Color.GREEN),
  /**
   * An error occurred.
   */
  ERROR("Error", 0, Color.RED);

  /**
   * Name of the problem shown in the table.
   */
  private String problem;
  /**
   * The severity level.
   */
  private final int level;
  /**
   * The color to mark images with.
   */
  private final Color color;

  /**
   * Constructs a Probelm object.
   *
   * @param newProblem name of the problem.
   * @param newLevel level of the problem.
   * @param newColor marking color of the problem.
   */
  Problem(final String newProblem, final int newLevel, final Color newColor) {
    this.problem = newProblem;
    this.level = newLevel;
    this.color = newColor;
  }

  /**
   * Gets the problem shown in the problem column.
   *
   * @return gets the problem as a String.
   */
  public String getProblem() {
    return problem;
  }

  /**
   * Sets the problem.
   *
   * @param problem problem.
   */
  public void setProblem(final String problem) {
    this.problem = problem;
  }

  /**
   * Gets the level of the current problem.
   *
   * @return level as an int.
   */
  public int getLevel() {
    return level;
  }

  /**
   * Gets the marking color of the current problem.
   *
   * @return color as a Color object.
   */
  public Color getColor() {
    return color;
  }
}
