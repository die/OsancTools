package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.Gui;
import com.github.waifu.util.Pair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  private JPanel setTablePanel;
  /**
   * Text area.
   */
  private final JLabel label;

  /**
   * Create action.
   *
   * @param setTablePanel table.
   */
  public ParseGuildLeakersAction(final JPanel setTablePanel) {
    this.setTablePanel = setTablePanel;
    this.label = new JLabel();
  }

  /**
   * Actions.
   *
   * @param e the action that occurred.
   */
  @Override
  public void actionPerformed(final ActionEvent e) {
    if (Gui.getRaid() != null) {
      final Map<String, Pair<List<Raider>, List<Account>>> map = new HashMap<>();
      final List<Raider> raiders = Gui.getRaid().getRaiders();
      final List<Account> crashers = Gui.getRaid().getCrashers();

      if (raiders != null && crashers != null) {
        for (final Raider r : raiders) {
          final List<String> guilds = r.getAccountGuildNames();
          for (final Account a : crashers) {
            String guild = a.getGuild();
            if (!guild.equals("") && guilds.contains(guild)) {
              System.out.println("Found connection " + r.getServerNickname() + " | " + a.getName() + " | " + a.getGuild());
              if (map.containsKey(guild)) {
                map.get(guild).left().add(r);
                map.get(guild).right().add(a);
              } else {
                final List<Raider> guildLeakers = new ArrayList<>();
                final List<Account> guildCrashers = new ArrayList<>();
                guildLeakers.add(r);
                guildCrashers.add(a);

                map.put(guild, new Pair(guildLeakers, guildCrashers));
              }
            }
          }
        }

        if (map.isEmpty()) {
          JOptionPane.showMessageDialog(setTablePanel, "Found no leakers.", "Parse Guild Leakers", JOptionPane.PLAIN_MESSAGE);
        } else {
          for (String key : map.keySet()) {
            StringBuilder output = new StringBuilder(label.getText());
            output.append("<html><u>Guild:</u> <b>").append(key).append("</b><br>");
            for (final Raider r : map.get(key).left()) {
              for (final Account a : map.get(key).right()) {
                output.append(r.getSniffedAccount().getName()).append(" ⇒ ").append(a.getName()).append("<br>");
                System.out.println(output.toString());
              }
            }
            output.append("<br></html>");
            label.setText(output.toString());
          }
          JOptionPane.showMessageDialog(setTablePanel, label, "Parse Guild Leakers", JOptionPane.WARNING_MESSAGE);
        }
      }
    }
  }
}
