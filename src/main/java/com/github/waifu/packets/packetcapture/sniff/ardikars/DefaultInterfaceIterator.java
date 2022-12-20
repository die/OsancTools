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

  private Interface next;

  DefaultInterfaceIterator(Interface next) {
    this.next = next;
  }

  @Override
  public boolean hasNext() {
    return next != null;
  }

  @Override
  public Interface next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    Interface previous = next;
    next = next.next();
    return previous;
  }

  @Override
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
