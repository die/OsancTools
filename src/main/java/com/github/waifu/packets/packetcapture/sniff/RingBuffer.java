/**
 * Class made by PrgmTrouble. Big thanks to him for implementing it.
 */

package com.github.waifu.packets.packetcapture.sniff;

/**
 * This is a simple Ring buffer made to store buffered packets as captured
 * by the sniffer. It stores elements in an array with two indesies traversing
 * the array. The indexes wrap arround to zero when hitting the limit of the
 * array. The first index is the write index and the second the read index.
 * Write adds elements and increments the counter while the read does the same
 * when reading the elements. The buffer also doubles in size if the ring buffer
 * is full and copies the elements into the newly created buffer of double the
 * size in the corresponding locations in the new array.
 *
 * @param <T> Generic type, in Sniffer.java its used to store TcpPackets
 */
public class RingBuffer<T> {

  /**
   * To be documented.
   */
  private static final byte EMPTY = 0;
  /**
   * To be documented.
   */
  private static final byte NORMAL = 1;
  /**
   * To be documented.
   */
  private static final byte FULL = 2;
  /**
   * To be documented.
   */
  private T[] buffer;
  /**
   * To be documented.
   */
  private int readPointer = 0;
  /**
   * To be documented.
   */
  private int writePointer = 0;
  /**
   * To be documented.
   */
  private byte state = EMPTY;

  /**
   * Constructor with initial capacity.
   *
   * @param capacity Initial capacity of buffer size.
   */
  public RingBuffer(final int capacity) {
    buffer = (T[]) new Object[capacity];
  }

  /**
   * Is empty check.
   *
   * @return True if buffer is empty.
   */
  public synchronized boolean isEmpty() {
    return EMPTY == state;
  }

  /**
   * Returns the number of elements currently in the buffer.
   *
   * @return The number of buffered elements in the buffer.
   */
  public synchronized int size() {
    if (readPointer > writePointer) {
      return writePointer + (buffer.length - readPointer);
    } else {
      return writePointer - readPointer;
    }
  }

  /**
   * Puts objects into the ring buffer.
   * If the ring buffer fills up to the max,
   * doubles the size of the buffer.
   *
   * @param item Items to be inserted into the buffer.
   */
  public synchronized void push(final T item) {
    if ((writePointer + 1) % buffer.length == readPointer) {
      state = FULL;
    } else {
      if (state == FULL) {
        final T[] next = (T[]) new Object[buffer.length << 1];
        /*
            [-----[writePointer,readPointer]-------]
            start from zero to writePointer or readPointer given they
            are the same point and write it to the new array.
         */
        System.arraycopy(buffer, 0, next, 0, writePointer);
        /*
            Write also from writePointer to the end of the old array.
            into new
         */
        final int destPos = buffer.length + writePointer;
        final int length = buffer.length - writePointer;
        System.arraycopy(buffer, writePointer, next, destPos, length);
        readPointer += buffer.length;
        buffer = next;
      }
      state = NORMAL;
    }
    writePointer = writePointer % buffer.length;
    buffer[writePointer++] = item;
  }

  /**
   * Removes and returns the oldest entry into the buffer.
   * The elements are extracted as per FIFO
   *
   * @return Returns the oldest element in the buffer and removes it.
   */
  public synchronized T pop() {
    if (readPointer + 1 == writePointer) {
      state = EMPTY;
    } else if (state == EMPTY) {
      return null;
    } else {
      state = NORMAL;
    }
    final T buf = buffer[readPointer];
    readPointer = (readPointer + 1) % buffer.length;
    return buf;
  }
}
