package com.github.waifu.gui.listeners;

import com.github.waifu.entities.Account;

/**
 * To be documented.
 */
public interface GroupListener {
  /**
   * To be documented.
   *
   * @param account To be documented.
   * @param exists To be documented.
   */
  void update(final Account account, final boolean exists);
}
