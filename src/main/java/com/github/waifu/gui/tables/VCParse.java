package com.github.waifu.gui.tables;

import com.github.waifu.entities.Account;
import com.github.waifu.entities.Raid;
import com.github.waifu.entities.Raider;
import com.github.waifu.gui.GUI;
import com.github.waifu.gui.actions.TableCopyAction;
import com.github.waifu.gui.models.VCTableModel;
import com.github.waifu.util.Utilities;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;
import net.sourceforge.tess4j.util.LoadLibs;
import org.json.JSONArray;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 *
 */
public class VCParse extends JFrame {
    private JPanel main;
    private JTable vcParseTable;
    private JCheckBox showInGroup;
    private JCheckBox removeCelestial;
    private JCheckBox removeInVCCheckBox;
    private JCheckBox removeMarkedRaidersCheckBox;
    private boolean destroy = false;
    private TableRowSorter<TableModel> sorter;
    private RowFilter<Object, Object> celestial = RowFilter.regexFilter("");
    private RowFilter<Object, Object> ingroup = RowFilter.regexFilter("");
    private RowFilter<Object, Object> invc = RowFilter.regexFilter("");
    private RowFilter<Object, Object> removeMarked = RowFilter.regexFilter("");

    /**
     * @param image
     * @param raiders
     * @throws IOException
     * @throws TesseractException
     * @throws InterruptedException
     */
    public VCParse(Image image, List<Raider> raiders) throws IOException, TesseractException, InterruptedException {
        createTable(image, raiders);
        if (destroy) {
            return;
        }
        setContentPane(main);
        setAlwaysOnTop(true);
        setResizable(false);
        setTitle("OsancTools");
        setIconImage(new ImageIcon(Utilities.getImageResource("Gravestone.png")).getImage());
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //setMinimumSize(new Dimension(screenSize.width / 4, screenSize.height / 4));
        setVisible(true);
        pack();
        new TableCopyAction(vcParseTable);
    }

    /**
     * createTable method.
     * <p>
     * Creates the table model, adds rows to the model, and applies the model to the table.
     *
     * @param image   image of the /who to parse
     * @param raiders list of members in the webapp
     */
    private void createTable(Image image, List<Raider> raiders) throws TesseractException {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);
        Tesseract instance = new Tesseract();
        instance.setLanguage("eng");
        instance.setDatapath(LoadLibs.extractTessResources("tessdata").getPath());
        instance.setOcrEngineMode(1);
        String result = instance.doOCR(ImageHelper.convertImageToGrayscale(bufferedImage));
        if (!result.contains("Players online")) {
            Component rootPane = GUI.getFrames()[0].getComponents()[0];
            if (raiders == null) {
                JOptionPane.showMessageDialog(rootPane, "Failed get members", "Error", JOptionPane.WARNING_MESSAGE);
                destroy = true;
            }
            JOptionPane.showMessageDialog(rootPane, "Failed to parse image", "Error", JOptionPane.WARNING_MESSAGE);
            destroy = true;
            this.dispose();
            return;
        }
        List<String> temp = Arrays.asList(Arrays.asList(result.split(": ")).get(1).replace(",", "").replace("\n", " ").split(" "));
        List<String> names = new ArrayList<>(temp);
        DefaultTableModel tableModel = new VCTableModel();
        for (int i = 0; i < raiders.size(); i++) {
            String username = raiders.get(i).getServerNickname();
            List<String> usernames = Utilities.parseUsernamesFromNickname(raiders.get(i).getServerNickname());
            String inGroup = "Not In Group";
            String inGroupUsername = "";
            String inVC = "Not In VC";
            for (String s : usernames) {
                for (String n : names) {
                    if (n.equalsIgnoreCase(s)) {
                        names.remove(n);
                        inGroup = "In Group";
                        inGroupUsername = s;
                        break;
                    }
                }
            }
            if (raiders.get(i).isInVC()) {
                inVC = "In VC";
            }

            Object[] array = new Object[6];
            array[0] = raiders.get(i).getResizedAvatar(vcParseTable.getRowHeight(), vcParseTable.getRowHeight());
            array[1] = username;
            array[2] = inGroupUsername;
            array[3] = inGroup;
            array[4] = inVC;
            array[5] = false;
            tableModel.addRow(array);
        }
        vcParseTable.setDefaultRenderer(Object.class, new VCTableRenderer());
        sorter = new TableRowSorter<>(tableModel);
        vcParseTable.setRowSorter(sorter);
        vcParseTable.setModel(tableModel);
        addActionListeners();
    }

    /**
     * addActionListeners method.
     * <p>
     * Constructs all listeners for the JFrame.
     */
    private void addActionListeners() {
        vcParseTable.addPropertyChangeListener(evt -> updateFilters());

        showInGroup.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (showInGroup.isSelected()) {
                    ingroup = RowFilter.notFilter(RowFilter.regexFilter("Not In Group", 3));
                    updateFilters();
                } else {
                    ingroup = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeInVCCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeInVCCheckBox.isSelected()) {
                    invc = RowFilter.regexFilter("Not In VC", 4);
                    updateFilters();
                } else {
                    invc = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeCelestial.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                RowFilter<Object, Object> filter = new RowFilter<Object, Object>() {
                    @Override
                    public boolean include(Entry entry) {
                        String serverNickname = (String) entry.getValue(1);
                        if (GUI.raid.findRaiderByServerNickname(serverNickname).isCelestial()) {
                            return false;
                        } else {
                            return true;
                        }
                    }
                };
                if (removeCelestial.isSelected()) {
                    celestial = filter;
                    updateFilters();
                } else {
                    celestial = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());

        removeMarkedRaidersCheckBox.addActionListener(e -> new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                if (removeMarkedRaidersCheckBox.isSelected()) {
                    removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 5));
                    updateFilters();
                } else {
                    removeMarked = RowFilter.regexFilter("");
                    updateFilters();
                }
                return null;
            }
        }.execute());
    }

    /**
     * updateFilters method.
     * <p>
     * Updates the current filters to the table.
     * This allows real-time updates to filtered rows upon changes.
     */
    private void updateFilters() {
        List<RowFilter<Object, Object>> filters = new ArrayList<>();
        filters.add(this.celestial);
        filters.add(this.ingroup);
        filters.add(this.invc);
        filters.add(this.removeMarked);
        this.sorter.setRowFilter(RowFilter.andFilter(filters));
    }

    /**
     *
     */
    private void createUIComponents() {
        // TODO: place custom component creation code here

    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
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
        main.setLayout(new GridLayoutManager(3, 3, new Insets(0, 0, 0, 0), -1, -1));
        final JScrollPane scrollPane1 = new JScrollPane();
        main.add(scrollPane1, new GridConstraints(0, 0, 1, 3, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        vcParseTable = new JTable();
        vcParseTable.setCellSelectionEnabled(true);
        scrollPane1.setViewportView(vcParseTable);
        showInGroup = new JCheckBox();
        showInGroup.setText("Show In Group");
        main.add(showInGroup, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeCelestial = new JCheckBox();
        removeCelestial.setText("Remove Celestial");
        main.add(removeCelestial, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeInVCCheckBox = new JCheckBox();
        removeInVCCheckBox.setText("Remove In VC");
        main.add(removeInVCCheckBox, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        removeMarkedRaidersCheckBox = new JCheckBox();
        removeMarkedRaidersCheckBox.setText("Remove Marked Raiders");
        main.add(removeMarkedRaidersCheckBox, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return main;
    }

}
