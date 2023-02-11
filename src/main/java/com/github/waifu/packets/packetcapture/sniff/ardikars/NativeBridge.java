package com.github.waifu.packets.packetcapture.sniff.ardikars;

import com.github.waifu.packets.packetcapture.sniff.netpackets.RawPacket;
import com.sun.jna.Pointer;
import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import pcap.spi.Interface;
import pcap.spi.Pcap;
import pcap.spi.Service;
import pcap.spi.exception.ErrorException;

/**
 * Bridge class to hook directly into native methods instead of using
 * preset methods used by the ardikars library.
 */
public final class NativeBridge {

  /**
   * To be documented.
   */
  private NativeBridge() {

  }

  /**
   * The main looping function on the network tap.
   * This is method calls the wrapper method that
   * in turn starts the packet sniffing.
   *
   * @param pcap        Packet capture class wrapping the interface for sniffing the wire.
   * @param packetCount Number of packets to listen to. -1 loops infinitely.
   * @param listener    Lambda abstract interface used when packets are captured.
   */
  public static void loop(final Pcap pcap, final int packetCount, final PacketListener listener) {
    try {
      final Field field = pcap.getClass().getDeclaredField("pointer");
      field.setAccessible(true);
      final Pointer p = (Pointer) field.get(pcap);

      try {
        NativeMappings.pcap_loop(p, packetCount, new GotPacketFuncExecutor(listener, SimpleExecutor.getInstance()), null);
      } catch (final Error error) {
        error.printStackTrace();
      }
    } catch (final Exception e) {
      //e.printStackTrace();
    }
  }

  /**
   * Returns a list of all interfaces on the device.
   *
   * @param service Service object used to grab the list of interfaces.
   * @return List of all interfaces on the device.
   * @throws ErrorException Error when attempting to grab interfaces.
   */
  public static Interface[] getInterfaces(final Service service) throws ErrorException {
    final List<Interface> list = new ArrayList<>();
    Interface i = service.interfaces();
    while (i != null) {
      list.add(i);
      i = i.next();
    }
    return list.toArray(new Interface[0]);
  }

  /**
   * Interface class for responding to captured packets.
   */
  public interface PacketListener {
    /**
     * To be documented.
     *
     * @param packet To be documented.
     */
    void gotPacket(RawPacket packet);
  }

  /**
   * Thread executor when packets are captured.
   */
  public static final class SimpleExecutor implements Executor {

    /**
     * To be documented.
     */
    private static final SimpleExecutor INSTANCE = new SimpleExecutor();

    /**
     * To be documented.
     */
    private SimpleExecutor() {
    }

    /**
     * To be documented.
     *
     * @return To be documented.
     */
    public static SimpleExecutor getInstance() {
      return INSTANCE;
    }

    /**
     * To be documented.
     *
     * @param command To be documented.
     */
    @Override
    public void execute(final Runnable command) {
      command.run();
    }
  }

  /**
   * Interface class for unwrapping captured packets from native
   * pointers to useful byte arrays and timestamps.
   */
  private static final class GotPacketFuncExecutor implements NativeMappings.PcapHandler {

    /**
     * To be documented.
     */
    private final PacketListener listener;
    /**
     * To be documented.
     */
    private final Executor executor;
    /**
     * To be documented.
     */
    private final int timestampPrecision = 1;

    GotPacketFuncExecutor(final PacketListener listener, final Executor executor) {
      this.listener = listener;
      this.executor = executor;
    }

    @Override
    public void got_packet(final Pointer args, final Pointer header, final Pointer packet) {
      final Instant now = buildTimestamp(header);
      final int len = NativeMappings.PcapPkthdr.getLen(header);
      final byte[] data = packet.getByteArray(0, NativeMappings.PcapPkthdr.getCaplen(header));

      try {
        executor.execute(() -> {
          if (data.length == len) {
            listener.gotPacket(RawPacket.newPacket(data, now));
          }
        });
      } catch (final Throwable throwable) {
        throwable.printStackTrace();
      }
    }

    private Instant buildTimestamp(final Pointer header) {
      final long epochSecond = NativeMappings.PcapPkthdr.getTvSec(header).longValue();
      switch (timestampPrecision) {
        case 0:
          return Instant.ofEpochSecond(epochSecond, NativeMappings.PcapPkthdr.getTvUsec(header).intValue() * 1000);
        case 1:
          return Instant.ofEpochSecond(epochSecond, NativeMappings.PcapPkthdr.getTvUsec(header).intValue());
        default:
          throw new AssertionError("Never get here.");
      }
    }
  }
}
