package com.github.waifu.packets.packetcapture;

import com.github.waifu.gui.Gui;
import com.github.waifu.packets.Packet;
import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.packetcapture.encryption.Rc4;
import com.github.waifu.packets.packetcapture.encryption.RotmgRc4Keys;
import com.github.waifu.packets.packetcapture.pconstructor.PacketConstructor;
import com.github.waifu.packets.packetcapture.register.Register;
import com.github.waifu.packets.packetcapture.sniff.PaProcessor;
import com.github.waifu.packets.packetcapture.sniff.Sniffer;
import com.github.waifu.packets.reader.BufferReader;
import java.nio.ByteBuffer;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 * The core class to process packets. First the network tap is sniffed to receive all packets. The packets
 * are filtered for port 2050, the rotmg port, and TCP packets. Then the packets are stitched together in
 * streamConstructor and rotmgConstructor class. After the packets are constructed the RC4 cipher is used
 * decrypt the data. The data is then matched with target classes and emitted through the registry.
 */
public class PacketProcessor extends Thread implements PaProcessor {

  /**
   * To be documented.
   */
  private final PacketConstructor incomingPacketConstructor;
  /**
   * To be documented.
   */
  private final PacketConstructor outgoingPacketConstructor;
  /**
   * To be documented.
   */
  private final Sniffer sniffer;
  /**
   * To be documented.
   */
  private final byte[] srcAddr;

  /**
   * Basic constructor of packetProcessor.
   * todo: Add linux and mac support later
   */
  public PacketProcessor() {
    sniffer = new Sniffer(this);
    incomingPacketConstructor = new PacketConstructor(this, new Rc4(RotmgRc4Keys.INCOMING_STRING));
    outgoingPacketConstructor = new PacketConstructor(this, new Rc4(RotmgRc4Keys.OUTGOING_STRING));
    srcAddr = new byte[4];
  }

  /**
   * Start method for PacketProcessor.
   */
  public void run() {
    tapPackets();
  }

  /**
   * Stop method for PacketProcessor.
   */
  public void stopSniffer() {
    sniffer.closeSniffers();
  }

  /**
   * Method to start the packet sniffer that will send packets back to receivedPackets.
   */
  public void tapPackets() {
    //logger.startLogger();
    incomingPacketConstructor.startResets();
    outgoingPacketConstructor.startResets();
    try {
      sniffer.startSniffer();
    } catch (final UnsatisfiedLinkError | NoClassDefFoundError | Exception e) {
      final JTextArea textarea = new JTextArea("> https://npcap.com/");
      textarea.setEditable(false);
      final String text = "<html>Error: failed to find network sniffer.";
      final JLabel label = new JLabel(text);
      final Object[] params = {label, "Please install the latest version here:", textarea};

      JOptionPane.showMessageDialog(Gui.getFrames()[0],
              params, null, JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Incoming byte data received from incoming TCP packets.
   *
   * @param data    Incoming byte stream
   * @param srcAddr Source IP of incoming packets.
   */
  @Override
  public void incomingStream(final byte[] data, final byte[] srcAddr) {
    incomingPacketConstructor.build(data);
  }

  /**
   * Outgoing byte data received from outgoing TCP packets.
   *
   * @param data Outgoing byte stream
   */
  @Override
  public void outgoingStream(final byte[] data) {
    //  logger.addOutgoing(data.length);
    outgoingPacketConstructor.build(data);
  }

  /**
   * Completed packets constructed by stream and rotmg constructor returned to packet constructor.
   * Decoded by the cipher and sent back to the processor to be emitted to subscribed users.
   *
   * @param type Constructed packet type.
   * @param size size of the packet.
   * @param data Constructed packet data.
   */
  public void processPackets(final byte type, final int size, final ByteBuffer data) {
    if (!PacketType.containsKey(type)) {
      //System.err.println("Unknown packet type:" + type + " Data:" + Arrays.toString(data.array()));
      return;
    }
    final Packet packetType = PacketType.getPacket(type).factory();
    packetType.setData(data.array());
    final BufferReader pData = new BufferReader(data);

    try {
      packetType.deserialize(pData);
    } catch (final Exception e) {
      debugPackets(type, data);
      return;
    }
    Register.getInstance().emit(packetType);
  }

  /**
   * Helper for debugging packets.
   *
   * @param type To be documented.
   * @param data To be documented.
   */
  private void debugPackets(final int type, final ByteBuffer data) {
    final Packet packetType = PacketType.getPacket(type).factory();
    try {
      data.position(5);
      final BufferReader pDebug = new BufferReader(data);
      packetType.deserialize(pDebug);
    } catch (final Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Closes the sniffer for shutdown.
   */
  public void closeSniffer() {
    sniffer.closeSniffers();
  }

  /**
   * To be documented.
   */
  @Override
  public void resetIncoming() {
    incomingPacketConstructor.reset();
  }

  /**
   * To be documented.
   */
  @Override
  public void resetOutgoing() {
    outgoingPacketConstructor.reset();
  }
}
