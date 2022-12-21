/*
 * Copyright (c) 2020-2021 Pcap Project
 * SPDX-License-Identifier: MIT OR Apache-2.0
 */

package com.github.waifu.packets.packetcapture.sniff.ardikars;

import java.util.Iterator;
import java.util.NoSuchElementException;
import pcap.spi.Interface;

/**
 * Directly extracted out of ardikars library to make edits possible.
 * https://github.com/ardikars/pcap
 */
public class DefaultInterfaceIterator implements Iterator<Interface> {

  /**
   * To be documented.
   */
  private Interface next;

  /**
   * To be documented.
   *
   * @param next To be documented.
   */
  DefaultInterfaceIterator(final Interface next) {
    this.next = next;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public boolean hasNext() {
    return next != null;
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  @Override
  public Interface next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    final Interface previous = next;
    next = next.next();
    return previous;
  }

  /**
   * To be documented.
   */
  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
