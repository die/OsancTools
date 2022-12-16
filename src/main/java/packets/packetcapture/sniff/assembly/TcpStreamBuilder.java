package packets.packetcapture.sniff.assembly;

import packets.packetcapture.sniff.netpackets.TcpPacket;
import util.Util;

import java.util.HashMap;

/**
 * Stream constructor ordering TCP packets in sequence. Payload is extracted and sent back in its raw form.
 */
public class TcpStreamBuilder {

    HashMap<Long, TcpPacket> packetMap = new HashMap<>();
    PStream stream;
    PReset packetReset;
    private long sequenseNumber;
    private int idNumber;

    /**
     * Constructor of StreamConstructor which needs a reset class to reset if reset
     * packet is retrieved and a constructor class to send ordered packets to.
     *
     * @param preset  Reset class if a reset packet is retrieved.
     * @param pstream Constructor class to send ordered packets to.
     */
    public TcpStreamBuilder(PReset preset, PStream pstream) {
        packetReset = preset;
        stream = pstream;
    }

    /**
     * Build method for ordering packets according to index used by TCP.
     *
     * @param packet TCP packets needing to be ordered.
     */
    public void streamBuilder(TcpPacket packet) {
        if (packet.isResetBit()) {
            if (packet.isSyn()) {
                reset();
            }
            return;
        }
        if (sequenseNumber == 0) {
            sequenseNumber = packet.getSequenceNumber();
            idNumber = packet.getIp4Packet().getIdentification();
        }

        packetMap.put(packet.getSequenceNumber(), packet);

        if (packetMap.size() > 95) {
            long index = sequenseNumber;
            int counter = 0;
            while (counter < 100000) {
                if(packetMap.containsKey(index)) {
                    sequenseNumber = index;
                    break;
                }
                index++;
                counter++;
            }
        } else if (packetMap.size() >= 100) { // Temp hacky solution until better solution is found. TODO: fix this
            reset();
        }

        while (packetMap.containsKey(sequenseNumber)) {
            TcpPacket packetSeqed = packetMap.remove(sequenseNumber);
            idNumber = packetSeqed.getIp4Packet().getIdentification();
            if (packet.getPayload() != null) {
                sequenseNumber += packetSeqed.getPayloadSize();
                stream.stream(packetSeqed.getPayload(), packetSeqed.getIp4Packet().getSrcAddr());
            }
        }
    }

    /**
     * Reset method if a reset packet is retrieved.
     */
    public void reset() {
        packetReset.reset();
        packetMap.clear();
        sequenseNumber = 0;
        idNumber = 0;
    }
}
