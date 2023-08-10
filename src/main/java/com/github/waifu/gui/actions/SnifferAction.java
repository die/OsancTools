package com.github.waifu.gui.actions;

import com.github.waifu.handlers.PacketHandler;
import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.packetcapture.PacketProcessor;
import com.github.waifu.packets.packetcapture.register.Register;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

/**
 * To be documented.
 */
public class SnifferAction implements ActionListener {

  /**
   * To be documented.
   */
  private PacketProcessor packetProcessor;

  /**
   * To be documented.
   */
  public SnifferAction() {
  }

  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    if (!(e.getSource() instanceof final JButton button)) return;

    switch (button.getText()) {
      case "Start Sniffer" -> {
        Register.getInstance().register(PacketType.UPDATE, PacketHandler::handlePacket);
        Register.getInstance().register(PacketType.NOTIFICATION, PacketHandler::logKeyPops);
        packetProcessor = new PacketProcessor();
        packetProcessor.start();
        button.setText("Stop Sniffer");
      }
      case "Stop Sniffer" -> {
        Register.setInstance(new Register());
        packetProcessor.stopSniffer();
        button.setText("Start Sniffer");
      }
      default -> { }
    }
  }
}
