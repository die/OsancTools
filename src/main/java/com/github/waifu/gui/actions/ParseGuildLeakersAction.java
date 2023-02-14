package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Leak;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Parses guild leakers.
 */
public class ParseGuildLeakersAction implements ActionListener {

  /**
   * Panel for the SetTable.
   */
  private final JPanel setTablePanel;
  /**
   * Text area.
   */
  private final JLabel label;
  /**
   * List of leaks.
   */
  private final List<Leak> leaks;

  /**
   * Create action.
   *
   * @param setTablePanel table.
   */
  public ParseGuildLeakersAction(final JPanel setTablePanel) {
    this.setTablePanel = setTablePanel;
    this.label = new JLabel();
    this.leaks = new ArrayList<>();
  }

  /**
   * Actions.
   *
   * @param e the action that occurred.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (Gui.getRaid() != null) {
      final List<Raider> raiders = Gui.getRaid().getRaiders();
      final List<Account> crashers = Gui.getRaid().getCrashers();

      if (raiders != null && crashers != null) {
        for (final Raider r : raiders) {
          final Leak leak = new Leak();
          final List<String> guilds = r.getAccountGuildNames();
          for (final Account a : crashers) {
            final String guild = a.getGuild();
            if (!guild.equals("") && guilds.contains(guild)) {
              final Leak l = getLeakByRaiderName(r.getServerNickname());
              if (l == null) {
                leak.setLeaker(r);
                leak.setGuild(guild);
                leak.addCrasher(a);
                leaks.add(leak);
              } else {
                l.addCrasher(a);
              }
            }
          }
        }
      }

      if (leaks.isEmpty()) {
        JOptionPane.showMessageDialog(setTablePanel, "Found no leakers.", "Parse Guild Leakers", JOptionPane.PLAIN_MESSAGE);
      } else {
        final StringBuilder output = new StringBuilder();
        leaks.sort((l1, l2) -> CharSequence.compare(l1.getGuild(), l2.getGuild()));
        String currentGuild = "";
        for (final Leak l : leaks) {
          final String guild = l.getGuild();
          final StringBuilder result = new StringBuilder();
          if (!guild.equalsIgnoreCase(currentGuild)) {
            currentGuild = guild;
            result.append("<html><br>Guild: <b>").append(guild).append("</b><br>");
          }
          final String leaker = l.getLeaker().getSniffedAccount().getName();
          for (final Account a : l.getCrashers()) {
            result.append(leaker).append(" ⇒ ").append(a.getName()).append("<br>");
          }
          output.append(result);
        }
        output.append("<br></html>");
        label.setText(output.toString());
        JOptionPane.showMessageDialog(setTablePanel, label, "Parse Guild Leakers", JOptionPane.WARNING_MESSAGE);
      }
    }
  }

  private Leak getLeakByRaiderName(final String serverNickname) {
    for (final Leak l : leaks) {
      if (l.getLeaker().getServerNickname().equalsIgnoreCase(serverNickname)) {
        return l;
      }
    }
    return null;
  }
}
