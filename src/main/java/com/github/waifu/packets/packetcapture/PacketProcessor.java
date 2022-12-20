package com.github.waifu.packets.packetcapture;

import com.github.waifu.packets.Packet;
import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.packetcapture.encryption.RC4;
import com.github.waifu.packets.packetcapture.encryption.RotMGRC4Keys;
import com.github.waifu.packets.reader.BufferReader;
import com.github.waifu.packets.packetcapture.sniff.PProcessor;
import com.github.waifu.packets.packetcapture.sniff.Sniffer;
import com.github.waifu.packets.packetcapture.pconstructor.PacketConstructor;
import com.github.waifu.packets.packetcapture.register.Register;

import java.nio.ByteBuffer;

/**
 * The core class to process packets. First the network tap is sniffed to receive all packets. The packets
 * are filtered for port 2050, the rotmg port, and TCP packets. Then the packets are stitched together in
 * streamConstructor and rotmgConstructor class. After the packets are constructed the RC4 cipher is used
 * decrypt the data. The data is then matched with target classes and emitted through the registry.
 */
public class PacketProcessor extends Thread implements PProcessor {
    private final PacketConstructor incomingPacketConstructor;
    private final PacketConstructor outgoingPacketConstructor;
    private final Sniffer sniffer;
    private final byte[] srcAddr;

    /**
     * Basic constructor of packetProcessor
     * TODO: Add linux and mac support later
     */
    public PacketProcessor() {
        sniffer = new Sniffer(this);
        incomingPacketConstructor = new PacketConstructor(this, new RC4(RotMGRC4Keys.INCOMING_STRING));
        outgoingPacketConstructor = new PacketConstructor(this, new RC4(RotMGRC4Keys.OUTGOING_STRING));
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
        } catch (UnsatisfiedLinkError e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Incoming byte data received from incoming TCP packets.
     *
     * @param data    Incoming byte stream
     * @param srcAddr Source IP of incoming packets.
     */
    @Override
    public void incomingStream(byte[] data, byte[] srcAddr) {
        incomingPacketConstructor.build(data);
    }

    /**
     * Outgoing byte data received from outgoing TCP packets.
     *
     * @param data Outgoing byte stream
     */
    @Override
    public void outgoingStream(byte[] data) {
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
    public void processPackets(byte type, int size, ByteBuffer data) {
        if (!PacketType.containsKey(type)) {
            //System.err.println("Unknown packet type:" + type + " Data:" + Arrays.toString(data.array()));
            return;
        }
        Packet packetType = PacketType.getPacket(type).factory();
        packetType.setData(data.array());
        BufferReader pData = new BufferReader(data);

        try {
            packetType.deserialize(pData);
        } catch (Exception e) {
            debugPackets(type, data);
            return;
        }
        Register.INSTANCE.emit(packetType);
    }

    /**
     * Helper for debugging packets
     */
    private void debugPackets(int type, ByteBuffer data) {
        Packet packetType = PacketType.getPacket(type).factory();
        try {
            data.position(5);
            BufferReader pDebug = new BufferReader(data);
            packetType.deserialize(pDebug);
        } catch (Exception ignored) { }
    }

    /**
     * Closes the sniffer for shutdown.
     */
    public void closeSniffer() {
        sniffer.closeSniffers();
    }

    @Override
    public void resetIncoming() {
        incomingPacketConstructor.reset();
    }

    @Override
    public void resetOutgoing() {
        outgoingPacketConstructor.reset();
    }
}
