package com.github.waifu.gui.actions;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.entities.React;
import com.github.waifu.gui.Gui;
import com.github.waifu.gui.tables.ReactTable;
import com.github.waifu.handlers.RealmeyeRequestHandler;
import com.github.waifu.handlers.RequirementSheetHandler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * To be documented.
 */
public class ParseWebAppReactsAction implements ActionListener {

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
  private final JButton parseReactsButton;


  /**
   * To be documented.
   *
   * @param progressBar To be documented.
   * @param stopButton To be documented.
   * @param parseReactsButton To be documented.
   */
  public ParseWebAppReactsAction(final JProgressBar progressBar, final JButton stopButton, final JButton parseReactsButton) {
    this.progressBar = progressBar;
    this.stopButton = stopButton;
    this.parseReactsButton = parseReactsButton;
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
          if (Gui.checkProcessRunning()) {
            return null;
          } else if (Gui.getRaid() != null && Gui.getRaid().isWebAppRaid()) { // webapp
            if (RealmeyeRequestHandler.checkDirectConnect()) {
              // check if the requirement sheet supports reacts
              if (!RequirementSheetHandler.getRequirementSheet().has("reacts")) {
                Gui.displayMessage("The current requirement sheet does not support react parsing.", JOptionPane.ERROR_MESSAGE);
                return null;
              }
              stopButton.setText("Stop Process");
              Gui.setWorker(this);
              progressBar.setValue(0);
              Gui.setProcessRunning(true);
              final List<React> reacts = getRealmeyeReacts(Gui.getRaid(), progressBar);
              if (reacts != null) {
                new ReactTable(reacts);
              }
              Gui.setProcessRunning(false);
              stopButton.setText("Finished");
            }
          }
        } catch (final Exception ex) {
          ex.printStackTrace();
          Gui.setProcessRunning(false);
        }
        return null;
      }
    }.execute();
  }

  /**
   * getReacts method.
   *
   * <p>Constructs a list of React objects that contain:
   * React metadata, including icon and name.
   * All raiders who reacted and their Accounts.
   *
   * @param raid JSONObject returned from the WebApp.
   * @param bar  progress bar to be updated over time.
   * @return To be documented.
   */
  private List<React> getRealmeyeReacts(final Raid raid, final JProgressBar bar) throws InterruptedException, IOException {
    if (raid.getRaiders().isEmpty()) {
      return null;
    } else {
      final JSONObject requirementSheet = RequirementSheetHandler.getRequirementSheet();
      final JSONArray items = (JSONArray) requirementSheet.get("reactItem");
      final JSONArray classes = (JSONArray) requirementSheet.get("reactClass");
      final JSONArray reactDPS = (JSONArray) requirementSheet.get("reactDps");
      final List<React> reactList = createReactObjects();
      bar.setMaximum(reactList.size());
      int count = 1;
      for (final React react : reactList) {
        for (final Raider raider : raid.getRaiders()) {
          if (raider.getReacts().toList().contains(Integer.parseInt(react.getId()))) {
            /* Create deep copy */
            final Raider temp = new Raider();
            for (int i = 0; i < raider.getAccounts().size(); i++) {
              final String username = raider.getAccounts().get(i).getName();
              final Account account = RealmeyeRequestHandler.parseHTML(RealmeyeRequestHandler.getRealmeyeData(username), username);
              raider.getAccounts().set(i, account);
              temp.getAccounts().add(account);
            }
            react.getRaiders().add(temp);
          }
        }

        final String reactName = react.getName();
        if (items.toList().contains(reactName)) {
          react.setType("item");
        } else if (classes.toList().contains(reactName)) {
          react.setType("class");
        } else if (reactDPS.toList().contains(reactName)) {
          react.setType("dps");
        } else if (reactName.contains("Effusion")) {
          react.setType("lock");
        }
        react.parseReact(react.getType());
        bar.setValue(count);
        count++;
      }
      return reactList;
    }
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   * @throws MalformedURLException To be documented.
   */
  private List<React> createReactObjects() throws MalformedURLException {
    final JSONArray reacts = Gui.getJson().getJSONObject("raid").getJSONArray("reacts");
    final List<React> reactList = new ArrayList<>();
    for (int i = 0; i < reacts.length(); i++) {
      final String id = String.valueOf(reacts.getJSONObject(i).getInt("id"));
      final String name = reacts.getJSONObject(i).getString("name");
      final String icon = reacts.getJSONObject(i).getString("icon");
      final String requirement = String.valueOf(reacts.getJSONObject(i).get("reqs"));
      final React react = new React(id, name, requirement, icon, new ArrayList<>());
      reactList.add(react);
    }
    return reactList;
  }
}
