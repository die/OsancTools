package com.github.waifu.entities;

import com.github.waifu.enums.Problem;

/**
 * Stores issue of an Inventory.
 */
public class Issue {

  /**
   * Stores the problem.
   */
  private Problem problem;
  /**
   * Stores in-game whisper.
   */
  private String whisper;
  /**
   * Stores message to prompt in the table.
   */
  private String message;

  /**
   * Creates an Issue with no whisper or message specified.
   *
   * @param problem problem as a Problem object.
   */
  public Issue(final Problem problem) {
    this.problem = problem;
    this.whisper = "None";
    this.message = "";
  }

  /**
   * Gets the whisper.
   *
   * @return whisper as a String.
   */
  public String getWhisper() {
    return this.whisper;
  }

  /**
   * Sets the whisper.
   *
   * @param string whisper as a String
   */
  public void setWhisper(final String string) {
    this.whisper = string;
  }

  /**
   * Gets the message.
   *
   * @return message as a String.
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Sets the message.
   *
   * @param string message as a String.
   */
  public void setMessage(final String string) {
    this.message = string;
  }

  /**
   * Gets the problem.
   *
   * @return problem as a Problem object.
   */
  public Problem getProblem() {
    return this.problem;
  }

  /**
   * setProblem method.
   *
   * <p>Sets a problem if the level is higher.
   * If multiple problems exist, the one with the highest level is reported.
   *
   * @param problem contains the type of problem (Banned item/Under reqs/Swapout/etc)
   */
  public void setProblem(final Problem problem) {
    if (this.problem != null) {
      if (this.problem.getLevel() >= problem.getLevel()) {
        this.problem = problem;
      }
    } else {
      this.problem = problem;
    }
  }
}
