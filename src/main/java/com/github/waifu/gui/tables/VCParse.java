package com.github.waifu.gui.tables;

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
     *
     * @param image
     * @param members
     * @throws IOException
     * @throws TesseractException
     * @throws InterruptedException
     */
    public VCParse(Image image, JSONArray members) throws IOException, TesseractException, InterruptedException {
        createTable(image, members);
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
     * @param members list of members in the webapp
     */
    private void createTable(Image image, JSONArray members) throws TesseractException {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(image, 0, 0, null);
        Tesseract instance = new Tesseract();
        instance.setLanguage("eng");
        instance.setDatapath(LoadLibs.extractTessResources("tessdata").getPath());
        instance.setOcrEngineMode(1);
        String result = instance.doOCR(ImageHelper.convertImageToGrayscale(bufferedImage));
        if (!result.contains("Players online")) {
            Component rootPane = GUI.getFrames()[0].getComponents()[0];
            if (members == null) {
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
        for (int i = 0; i < members.length(); i++) {
            String username = members.getJSONObject(i).getString("server_nickname");
            String id = members.getJSONObject(i).getString("user_id");
            Set<String> usernames = Utilities.getRaiderUsernames(members, i);
            String inGroup = "Not In Group";
            String inGroupUsername = "";
            String inVC = "Not In VC";
            String celestial = "";
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
            if (members.getJSONObject(i).getBoolean("in_vc")) {
                inVC = "In VC";
            }
            if (members.getJSONObject(i).getJSONArray("roles").toList().contains("907008641079586817")) {
                celestial = "@Celestial";
            }
            Object[] array = new Object[7];
            array[0] = inGroupUsername;
            array[1] = inGroup;
            array[2] = inVC;
            array[3] = username;
            array[4] = id;
            array[5] = celestial;
            array[6] = false;
            tableModel.addRow(array);
        }
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
                    ingroup = RowFilter.notFilter(RowFilter.regexFilter("Not In Group", 1));
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
                    invc = RowFilter.regexFilter("Not In VC", 2);
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
                if (removeCelestial.isSelected()) {
                    celestial = RowFilter.notFilter(RowFilter.regexFilter("@Celestial", 5));
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
                    removeMarked = RowFilter.notFilter(RowFilter.regexFilter("true", 6));
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
