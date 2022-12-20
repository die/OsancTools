package com.github.waifu.gui;

import com.github.waifu.entities.Raider;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class AccountView extends JFrame {
    private JPanel main;
    private JLabel Realmeye;
    private JLabel DiscordInfo;
    private JTabbedPane DiscordTab;
    private final Raider raider;

    /**
     *
     * @param raider
     */
    public AccountView(Raider raider) {
        this.raider = raider;
        $$$setupUI$$$();
        setContentPane(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setIconImage(new ImageIcon(Utilities.getImageResource("images/gui/Gravestone.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        setVisible(true);
        DiscordInfo.setIcon(raider.getAvatar());
        Realmeye.setText("<html>" + raider.getAccounts().get(0).printAccount().replace("\n", "<br/>") + "</html>");
        DiscordInfo.setText("<html>" + raider.printRaider().replace("\n", "<br/>") + "</html>");
        pack();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        main = new JPanel();
        main.setLayout(new GridLayoutManager(2, 1, new Insets(0, 0, 0, 0), -1, -1));
        DiscordTab = new JTabbedPane();
        main.add(DiscordTab, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200), null, 0, false));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        DiscordTab.addTab("Discord", panel1);
        DiscordInfo = new JLabel();
        DiscordInfo.setHorizontalTextPosition(4);
        DiscordInfo.setText("Label");
        panel1.add(DiscordInfo, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        DiscordTab.addTab("Realmeye", panel2);
        Realmeye = new JLabel();
        Realmeye.setText("Label");
        panel2.add(Realmeye, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
