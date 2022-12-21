package com.github.waifu.util;

/**
 * Basic pair class.
 *
 * @param <A> Type of objects to the left.
 * @param <B> Type of objects to the right.
 */
public class Pair<A, B> {

  /**
   * To be documented.
   */
  private final A fst;
  /**
   * To be documented.
   */
  private final B snd;

  /**
   * Constructor initilizing the pair.
   *
   * @param f Left object.
   * @param s Right object.
   */
  public Pair(final A f, final B s) {
    this.fst = f;
    this.snd = s;
  }

  /**
   * Returns the left object.
   *
   * @return The left object.
   */
  public A left() {
    return fst;
  }

  /**
   * Returns the right object.
   *
   * @return The right object.
   */
  public B right() {
    return snd;
  }

  /**
   * To be documented.
   *
   * @return to be documented.
   */
  public String toString() {
    return "Pair["
            + fst
            + ","
            + snd
            + "]";
  }
}
