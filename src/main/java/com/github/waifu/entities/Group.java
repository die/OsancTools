package com.github.waifu.entities;

import com.github.waifu.assets.objects.PortalXmlObject;
import com.github.waifu.gui.listeners.GroupListener;
import com.github.waifu.util.Utilities;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * To be documented.
 */
public class Group {

  /**
   * To be documented.
   */
  public List<GroupListener> listeners = new ArrayList<>();
  /**
   * To be documented.
   */
  public List<Account> accounts;
  /**
   * To be documented.
   */
  List<PortalXmlObject> dungeonPops;

  /**
   * To be documented.
   */
  public Group() {
    accounts = new ArrayList<>();
    dungeonPops = new ArrayList<>();
  }

  /**
   * To be documented.
   *
   * @param groupListener To be documented.
   */
  public void addListener(final GroupListener groupListener) {
    this.listeners.add(groupListener);
  }

  /**
   * To be documented.
   *
   * @param groupListener To be documented.
   */
  public void removeListener(final GroupListener groupListener) {
    this.listeners.remove(groupListener);
  }

  /**
   * To be documented.
   *
   * @return To be documented.
   */
  public List<Account> getAccounts() {
    return accounts;
  }

  /**
   * To be documented.
   *
   * @param account To be documented.
   */
  public void addAccount(final Account account) {
    final int index = getAccountIndex(account);
    final boolean exists = index != -1;

    if (exists) {
      accounts.set(index, account);
    } else {
      accounts.add(account);
    }
    for (final GroupListener groupListener : listeners) {
      groupListener.update(account, exists);
    }
  }

  public Account getAccountByNickname(final List<String> igns) {
    for (final Account account : accounts) {
      final String name = account.getName();
      if (igns.stream().anyMatch(name::equalsIgnoreCase)) {
        return account;
      }
    }
    return null;
  }
  /**
   * To be documented.
   *
   * @param account To be documented.
   * @return To be documented.
   */
  public int getAccountIndex(Account account) {
    for (int i = 0; i < accounts.size(); i++) {
      if (accounts.get(i).getName().equalsIgnoreCase(account.getName())) {
        return i;
      }
    }
    return -1;
  }
}