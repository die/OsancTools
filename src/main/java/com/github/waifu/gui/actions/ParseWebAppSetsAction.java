package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Character;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.tables.SetTable;
import com.github.waifu.handlers.PacketHandler;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.packets.PacketType;
import com.github.waifu.packets.packetcapture.PacketProcessor;
import com.github.waifu.packets.packetcapture.register.Register;
import com.github.waifu.util.Utilities;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 * To be documented.
 */
public class ParseWebAppSetsAction implements ActionListener {

  /**
   * To be documented.
   */
  private final JProgressBar progressBar;
  /**
   * To be documented.
   */
  private final JButton stopButton;
  /**
   * To be documented.
   */
  private final JButton parseSetsButton;
  /**
   * To be documented.
   */
  private PacketProcessor packetProcessor;

  /**
   * To be documented.
   *
   * @param progressBar To be documented.
   * @param stopButton To be documented.
   * @param parseSetsButton To be documented.
   */
  public ParseWebAppSetsAction(final JProgressBar progressBar, final JButton stopButton, final JButton parseSetsButton) {
    this.progressBar = progressBar;
    this.stopButton = stopButton;
    this.parseSetsButton = parseSetsButton;
    this.packetProcessor = new PacketProcessor();
  }


  /**
   * To be documented.
   *
   * @param e To be documented.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() {
        try {
          if (parseSetsButton.getText().equals("Stop Sniffer")) {
            // if (Gui.getRaid().isWebAppRaid()) { //&& !Gui.getRaid().getStatus().equals("RUNNING"))
            packetProcessor.closeSniffer();
            packetProcessor = null;
            Register.setInstance(new Register());
            final List<Raider> sets = getSnifferSets(Gui.getRaid(), progressBar);
            if (sets != null) {
              // new SetTable(sets);
            }
            parseSetsButton.setText("Parse Sets");
            Gui.setProcessRunning(false);
          } else if (Gui.checkProcessRunning()) {
            return null;
          } else if (Gui.getRaid() == null || !Gui.getRaid().isWebAppRaid()) {
            if (!startSniffer()) {
              return null;
            }
            Gui.setRaid(new Raid());
            Register.getInstance().register(PacketType.UPDATE, PacketHandler::handlePacket);
            packetProcessor = new PacketProcessor();
            packetProcessor.start();
          } else if (Gui.getRaid().isWebAppRaid()) { // webapp
            final String[] options = {"Sniffer", "Realmeye"};
            final String message = "Please choose the method to parse:\n\nNote: You can parse through Realmeye\nand then parse with the sniffer,\nbut not the other way around.";
            final int result = JOptionPane.showOptionDialog(Gui.getFrames()[0], message, "", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, new ImageIcon(Utilities.getClassResource("images/gui/Gravestone.png")), options, options[1]);
            Gui.setWorker(this);
            Gui.setProcessRunning(true);
            if (result == 0) {
              if (!startSniffer()) {
                return null;
              }
              Register.getInstance().register(PacketType.UPDATE, PacketHandler::handlePacket);
              packetProcessor = new PacketProcessor();
              packetProcessor.start();
            } else if (result == 1) {
              if (RealmeyeRequestHandler.checkDirectConnect()) {
                stopButton.setText("Stop Process");
                Gui.setWorker(this);
                progressBar.setValue(0);
                Gui.setProcessRunning(true);
                final List<Raider> sets = getRealmeyeSets(Gui.getRaid(), progressBar);
                if (sets != null) {
                  // new SetTable(sets);
                }
                Gui.setProcessRunning(false);
                stopButton.setText("Finished");
              }
            } else {
              Gui.setProcessRunning(false);
              return null;
            }
          }
        } catch (final Exception ex) {
          ex.printStackTrace();
          Gui.setProcessRunning(false);
        }
        return null;
      }

      /**
       * Starts the sniffer.
       *
       * @return if the sniffer was able to start.
       */
      private boolean startSniffer() {
        parseSetsButton.setEnabled(false);
        if (PacketHandler.isXMLNull()) {
          if (!PacketHandler.loadXML()) {
            parseSetsButton.setEnabled(true);
            return false;
          }
        }
        parseSetsButton.setEnabled(true);
        parseSetsButton.setText("Stop Sniffer");
        Gui.setWorker(this);
        progressBar.setValue(0);
        return true;
      }
    }.execute();
  }

  private List<Raider> getSnifferSets(final Raid raid, final JProgressBar bar) {
    if (raid.getRaiders().isEmpty()) {
      return null;
    } else {
      if (!raid.isWebAppRaid()) {
        return parseRaiderInventories(raid.getRaiders(), bar);
      } else {
        final List<Raider> raiders = removeRunes(raid.getRaiders());
        return parseRaiderInventories(raiders, bar);
      }
    }
  }

  /**
   * getSets method.
   *
   * <p>Constructs a map containing accounts and their inventories.
   *
   * @param raid To be documented.
   * @param bar progress bar to be updated over time.
   * @return To be documented.
   */
  private List<Raider> getRealmeyeSets(final Raid raid, final JProgressBar bar) throws InterruptedException, IOException {
    if (raid.getRaiders().isEmpty()) {
      return null;
    } else {
      final List<Raider> raiders = removeRunes(raid.getRaiders());
      bar.setMaximum(raiders.size());
      for (int i = 0; i < raiders.size(); i++) {
        for (int j = 0; j < raiders.get(i).getAccounts().size(); j++) {
          final String username = raiders.get(i).getAccounts().get(j).getName();
          final Account account = RealmeyeRequestHandler.parseHTML(Objects.requireNonNull(RealmeyeRequestHandler.getRealmeyeData(username)), username);
          if (account.getCharacters() != null) {
            account.getCharacters().get(0).getInventory().parseInventory();
          }
          raiders.get(i).getAccounts().set(j, account);
        }
        bar.setValue(i + 1);
      }
      return raiders;
    }
  }

  private List<Raider> parseRaiderInventories(final List<Raider> raiders, final JProgressBar bar) {
    bar.setMaximum(raiders.size());
    for (int i = 0; i < raiders.size(); i++) {
      for (int j = 0; j < raiders.get(i).getAccounts().size(); j++) {
        if (raiders.get(i).getAccounts().get(j).getCharacters() != null) {
          raiders.get(i).getAccounts().get(j).getRecentCharacter().parseCharacter();
        } else {
          final List<Character> characters = new ArrayList<>();
          final Character character = new Character();
          character.parseCharacter();
          characters.add(character);
          raiders.get(i).getAccounts().set(j, new Account(raiders.get(i).getAccounts().get(j).getName(), characters));
        }
      }
      bar.setValue(i + 1);
    }
    return raiders;
  }

  private List<Raider> removeRunes(final List<Raider> raiders) {
    final List<Raider> newRaiders = new ArrayList<>();
    for (final Raider r : raiders) {
      if (r.getRoles() != null) {
        final List<Object> reactIds = r.getReacts().toList();
        if (reactIds.isEmpty()) {
          newRaiders.add(r);
        } else {
          if (!reactIds.contains(Rune.SHIELD_RUNE.id) && !reactIds.contains(Rune.SWORD_RUNE.id) && !reactIds.contains(Rune.HELMET_RUNE.id)) {
            newRaiders.add(r);
          }
        }
      }
    }
    return newRaiders;
  }

  /**
   * To be documented.
   */
  public enum Rune {

    /**
     * To be documented.
     */
    SHIELD_RUNE(20),
    /**
     * To be documented.
     */
    SWORD_RUNE(21),
    /**
     * To be documented.
     */
    HELMET_RUNE(22);

    /**
     * To be documented.
     */
    private final int id;

    /**
     * To be documented.
     *
     * @param id To be documented.
     */
    Rune(final int id) {
      this.id = id;
    }
  }

}
