package com.github.waifu.packets.packetcapture.encryption;

import com.github.waifu.packets.Util;
import java.nio.ByteBuffer;

/**
 * Rc4 cipher used to decrypt packets.
 */
public class Rc4 {

  /**
   * Size of the int array.
   */
  private final int size = 256;
  /**
   * To be documented.
   */
  private int[] state = new int[size];
  /**
   * To be documented.
   */
  private int[] initState = new int[size];
  /**
   * To be documented.
   */
  private int indexI;
  /**
   * To be documented.
   */
  private int indexJ;

  /**
   * Constructor of Rc4 needing a string key.
   *
   * @param key A key in the form of a string.
   */
  public Rc4(final String key) {
    this(Util.hexStringToByteArray(key));
  }

  /**
   * A Rc4 object with specific internal states.
   *
   * @param s     The current state of the key.
   * @param init The initial state of the key.
   * @param i         The i index of the Rc4 state.
   * @param j         The j index of the Rc4 state.
   */
  private Rc4(final int[] s, final int[] init, final int i, final int j) {
    this.state = s;
    this.initState = init;
    this.indexI = i;
    this.indexJ = j;
  }

  /**
   * Constructor of Rc4 class needing a hex-number-key in a byte array.
   *
   * @param key Key in the form of hex numbers in a byte array.
   */
  public Rc4(final byte[] key) {
    indexI = 0;
    indexJ = 0;

    for (int i = 0; i < size; i++) {
      state[i] = i;
    }

    int j = 0;
    for (int i = 0; i < size; i++) {
      j = (j + state[i] + Byte.toUnsignedInt(key[i % key.length])) % size;
      final int tmp = state[i];
      state[i] = state[j];
      state[j] = tmp;
      // state[i] ^= state[j] ^= state[i] ^= state[j];
    }
    System.arraycopy(state, 0, initState, 0, state.length);
  }

  /**
   * To be documented.
   *
   * @param bytes byte array.
   * @return int
   */
  public static int convertByteArrayToInt(final byte[] bytes) {
    return ByteBuffer.wrap(bytes).getInt();
  }

  /**
   * Returning the result of Xor:ing the byte with the Rc4 cipher.
   * This also results in incrementing the internal state by 1.
   *
   * @return The resulting byte after Xor:ing with the cipher.
   */
  public synchronized byte getXor() {
    indexI = (indexI + 1) % size;
    indexJ = (indexJ + state[indexI]) % size;
    final int tmp = state[indexI];
    state[indexI] = state[indexJ];
    state[indexJ] = tmp;
    return (byte) state[(state[indexI] + state[indexJ]) % size];
  }

  /**
   * A crude brute force skip method to increment
   * the ciphers internal state by an amount.
   *
   * @param amount The amount needed to increment the cipher.
   * @return Returning this object for inlining.
   */
  public Rc4 skip(final int amount) {
    for (int k = 0; k < amount; k++) {
      indexI = (indexI + 1) % size;
      indexJ = (indexJ + state[indexI]) % size;
      final int tmp = state[indexI];
      state[indexI] = state[indexJ];
      state[indexJ] = tmp;
    }
    return this;
  }

  /**
   * Decrypting the bytes in an array with the cipher.
   * Then directly inserting the decrypted
   * bytes back into the same array with same index.
   *
   * @param offset Offset from the start of the array needing to be decrypted.
   * @param array  Array with bytes needing decrypting.
   */
  public void decrypt(final int offset, final byte[] array) {
    for (int b = offset; b < array.length; b++) {
      array[b] = (byte) (array[b] ^ getXor());
    }
  }

  /**
   * Decrypting the bytes in an array with the cipher.
   * Then directly inserting the decrypted
   * bytes back into the same array with same index.
   *
   * @param array Array with bytes needing decrypting.
   */
  public void decrypt(final byte[] array) {
    decrypt(0, array);
  }

  /**
   * Decrypting the bytes in a ByteBuffer with the cipher.
   * First extracting the byte array out of the ByteBuffer object
   * and mutating it (for speed purposes) the array decrypted
   * bytes back into the same array with same index.
   * This will directly modify the ByteBuffer.
   *
   * @param offset     Offset from the start of the array to be decrypted.
   * @param byteBuffer ByteBuffer with bytes needing decrypting.
   */
  public void decrypt(final int offset, final ByteBuffer byteBuffer) {
    final byte[] array = byteBuffer.array();
    decrypt(offset, array);
  }

  /**
   * To be documented.
   *
   * @param byteBuffer ByteBuffer object.
   */
  public void decrypt(final ByteBuffer byteBuffer) {
    decrypt(0, byteBuffer);
  }

  /**
   * To be documented.
   *
   * @return Rc4 object.
   */
  public Rc4 fork() {
    final int[] state2 = new int[state.length];
    System.arraycopy(state, 0, state2, 0, state2.length);
    return new Rc4(state2, initState, indexI, indexJ);
  }

  /**
   * To be documented.
   */
  public void reset() {
    System.arraycopy(initState, 0, state, 0, state.length);
    indexI = 0;
    indexJ = 0;
  }
}
