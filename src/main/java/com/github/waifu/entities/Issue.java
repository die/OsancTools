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
    switch (this.problem) {
      case UNDER_REQS -> this.message = "Under reqs, brought " + string;
      case EMPTY_SLOT -> this.message = "Under reqs, brought an " + string;
      case BANNED_ITEM -> this.message = "Under reqs, brought banned item " + string;
      case MISSING_REACT, MISSING_REACT_DPS -> this.message = "Fake reacting " + string;
      case SWAPOUT_ITEM -> this.message = "Under reqs, brought swapout item " + string + " without an item that meets reqs";
      case PRIVATE_PROFILE -> this.message = "Hiding character information";
      case MISSING_REACT_TRADE -> this.message = "Fake reacting " + string + ", didn't bring to the run";
      case NONE -> this.message = "None";
      default -> this.message = "";
    }
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
