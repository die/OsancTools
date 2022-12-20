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
  private Address next;

  DefaultAddressIterator(Address next) {
    this.next = next;
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  @Override
  public Address next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    Address previous = next;
    next = next.next();
    return previous;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
