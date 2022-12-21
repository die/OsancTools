/*
 * Copyright (c) 2020-2021 Pcap Project
 * SPDX-License-Identifier: MIT OR Apache-2.0
 */

package com.github.waifu.packets.packetcapture.sniff.ardikars;

import java.util.Iterator;
import java.util.NoSuchElementException;
import pcap.spi.Address;

/**
 * Directly extracted out of ardikars library to make edits possible.
 * https://github.com/ardikars/pcap
 */
public class DefaultAddressIterator implements Iterator<Address> {

  /**
   * To be documented.
   */
  private Address next;

  /**
   * To be documented.
   *
   * @param next To be documented.
   */
  DefaultAddressIterator(final Address next) {
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
  public Address next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    final Address previous = next;
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
