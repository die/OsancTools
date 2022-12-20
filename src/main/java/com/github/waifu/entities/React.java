package com.github.waifu.entities;

import com.github.waifu.enums.Problem;
import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * React class to store react data.
 */
public class React {

    private final String id;
    private final String name;
    private String type;
    private final String requirement;
    private ImageIcon image;
    private List<Raider> raiders;

    /**
     * React method.
     *
     * Constructs a React with limited information.
     *
     * @param id id of the React.
     * @param name name of the React.
     * @param imageURL url of the image of the React.
     * @param raiders list of raiders who have reacted to this React.
     */
    public React(String id, String name, String requirement, String imageURL, List<Raider> raiders) throws MalformedURLException {
        this.id = id;
        this.name = name;
        this.type = "manual";
        this.requirement = requirement;
        this.image = new ImageIcon(new URL(imageURL));
        this.raiders = raiders;
    }

    /**
     * React method.
     *
     * Constructs a React with all information.
     *
     * @param id id of the React.
     * @param name name of the React.
     * @param type type of React in how it should be checked.
     * @param imageURL url of the image of the React.
     * @param raiders list of raiders who have reacted to this React.
     */
    public React(String id, String name, String type, String requirement, String imageURL, List<Raider> raiders) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.requirement = requirement;
        try {
            this.image = new ImageIcon(new URL(imageURL));
        } catch (MalformedURLException exception) {
            this.image = null;
        }
        this.raiders = raiders;
    }

    /**
     *
     * @return
     */
    public String getId() {
        return this.id;
    }

    /**
     *
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     *
     * @return
     */
    public String getRequirement() {
        return this.requirement;
    }

    /**
     *
     * @return
     */
    public ImageIcon getImage() {
        return this.image;
    }

    /**
     *
     * @return
     */
    public List<Raider> getRaiders() {
        return this.raiders;
    }

    /**
     *
     * @param raiders
     */
    public void setRaiders(List<Raider> raiders) {
        this.raiders = raiders;
    }

    /**
     * parseReact method.
     *
     * Parsing the react given its type.
     *
     * @param type type of React in how it should be checked.
     */
    public void parseReact(String type) {
        switch (type) {
            case "item":
                for (Raider r : raiders) {
                    for (Account a : r.getAccounts()) {
                        parseItem(a);
                    }
                }
                break;
            case "class":
                for (Raider r : raiders) {
                    for (Account a : r.getAccounts()) {
                        parseClass(a);
                    }
                }
                break;
            case "dps":
                for (Raider r : raiders) {
                    for (Account a : r.getAccounts()) {
                        parseDps(a);
                    }
                }
                break;
            case "lock":
                for (Raider r : raiders) {
                    for (Account a : r.getAccounts()) {
                        a.getCharacters().get(0).getInventory().getIssue().setWhisper("/t " + a.getName() + " Please trade me so I can confirm your effusion.");
                    }
                }
                break;
            default:
                for (Raider r : raiders) {
                    for (Account a : r.getAccounts()) {
                        a.getCharacters().get(0).getInventory().getIssue().setWhisper("/lock " + a.getName());
                    }
                }
                break;
        }
    }

    /**
     * parseItem method.
     *
     * Checks if the inventory contains the proper item for the React.
     *
     * @param account account of the raider.
     */
    private void parseItem(Account account) {
        Inventory inventory = account.getRecentCharacter().getInventory();
        if (inventory != null) {
            String ability = inventory.getAbility().getName();
            switch (name) {
                case "Ogmur/Pogmur":
                    if (ability.equals("Shield of Ogmur UT") || ability.equals("Crystallised Fang's Venom UT")) {
                        inventory.getIssue().setWhisper("None");
                        inventory.getIssue().setProblem(Problem.NONE);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
                    } else {
                        inventory.getIssue().setProblem(Problem.MISSING_REACT);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                    }
                    break;
                case "Slow":
                    JSONObject slowItems = Utilities.json.getJSONObject("reacts").getJSONObject("slowItems");
                    boolean good = true;
                    for (String keys : slowItems.keySet()) {
                        JSONObject jsonObject = (JSONObject) slowItems.get(keys);
                        JSONArray items = jsonObject.getJSONArray("items");
                        String item = ability.substring(0, ability.length() - 3);
                        if (items.toList().contains(item)) {
                            JSONArray slotArray = jsonObject.getJSONArray("slot");
                            for (int j = 0; j < slotArray.length(); j++) {
                                String slot = slotArray.getString(j);
                                Item item1 = switch (slot) {
                                    case "weapon" -> inventory.getWeapon();
                                    case "ability" -> inventory.getAbility();
                                    case "armor" -> inventory.getArmor();
                                    case "ring" -> inventory.getRing();
                                    default -> null;
                                };
                                assert item1 != null;
                                String substring = item1.getName().substring(0, item1.getName().length() - 3);
                                if (items.toList().contains(substring)) {
                                    item1.setImage(Utilities.markImage(item1.getImage(), Problem.NONE.getColor()));
                                } else {
                                    item1.setImage(Utilities.markImage(item1.getImage(), Problem.MISSING_REACT.getColor()));
                                    good = false;
                                }
                            }
                            if (good) {
                                inventory.getIssue().setWhisper("None");
                                inventory.getIssue().setProblem(Problem.NONE);
                            } else {
                                inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                                inventory.getIssue().setProblem(Problem.MISSING_REACT);
                            }
                            break;
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                            inventory.getIssue().setProblem(Problem.MISSING_REACT);
                            inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
                        }
                    }
                    break;
                case "Fungal Tome":
                    if (ability.equals("Tome of the Mushroom Tribes UT") || ability.equals("Non-Fungible Tome UT")) { // accounts for april fools name SMH
                        inventory.getIssue().setWhisper("None");
                        inventory.getIssue().setProblem(Problem.NONE);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
                    } else {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                        inventory.getIssue().setProblem(Problem.MISSING_REACT);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
                    }
                    break;
                case "Marble Seal":
                    if (ability.equals("Marble Seal UT")) {
                        inventory.getIssue().setWhisper("None");
                        inventory.getIssue().setProblem(Problem.NONE);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
                    } else {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                        inventory.getIssue().setProblem(Problem.MISSING_REACT);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT.getColor()));
                    }
                    break;
            }
        }
    }

    /**
     * parseDps method.
     *
     * Checks if the inventory meets the required DPS and Class.
     *
     * @param account account of the raider.
     */
    public void parseDps(Account account) {
        Inventory inventory = account.getRecentCharacter().getInventory();
        String type = account.getRecentCharacter().getType();
        if (inventory.getItems().stream().allMatch(item -> item.getName().equals("Empty slot"))) {
            inventory.getIssue().setWhisper("Private Profile");
        } else {
            List<String> ranged = Arrays.asList("Wizard", "Mystic", "Necromancer", "Priest", "Summoner", "Sorcerer", "Bard", "Archer", "Huntress");
            switch (name) {
                case "Wizard/Summoner 2/4":
                    if (!(type.equals("Wizard") || type.equals("Summoner"))) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard/Summoner that is 2/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "2/4+ Wizard":
                    if (!account.getRecentCharacter().getType().equals("Wizard")) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard that is 2/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "2/4+ Ranged":
                    if (!ranged.contains(account.getRecentCharacter().getType())) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a ranged class that is 2/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 2)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "Wizard/Summoner 3/4":
                    if (!(account.getRecentCharacter().getType().equals("Wizard") || account.getRecentCharacter().getType().equals("Summoner"))) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard/Summoner that is 3/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "3/4+ Wizard":
                    if (!account.getRecentCharacter().getType().equals("Wizard")) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a Wizard that is 3/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "3/4+ Ranged":
                    if (!ranged.contains(account.getRecentCharacter().getType())) {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to a ranged class that is 3/4 dps.");
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT.getColor()));
                    } else {
                        account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
                        if (inventory.calculateDps(account.getRecentCharacter().getSkin(), 3)) {
                            inventory.getIssue().setWhisper("None");
                        } else {
                            inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
            }
        }
    }

    /**
     * parseClass method.
     *
     * Checks if the inventory meets the required Class and T6/T7 Item.
     *
     * @param account account of the raider.
     */
    public void parseClass(Account account) {
        String type = account.getRecentCharacter().getType();
        Inventory inventory = account.getRecentCharacter().getInventory();
        if (name.equals(type)) {
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.NONE.getColor()));
            inventory.getIssue().setProblem(Problem.NONE);
            inventory.getIssue().setWhisper("None");
            if (requirement.contains("T6")) {
                if (inventory.getAbility().getName().equals("Empty slot") || inventory.getItems().get(1).getName().endsWith("ST")) {
                    inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
                    inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
                } else if (inventory.getItems().get(1).getName().endsWith("UT")) {
                    JSONArray reskins = Utilities.json.getJSONObject("allowedSTSets").getJSONObject("Oryxmas").getJSONArray("items");
                    String ability = inventory.getAbility().getName();
                    if (reskins.toList().contains(ability.substring(0, ability.length() - 3))) {
                        inventory.getIssue().setWhisper("None");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
                    } else {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
                    }
                } else {
                    String ability = inventory.getAbility().getName();
                    if (Integer.parseInt(ability.substring(ability.length() - 1)) >= 6) {
                        inventory.getIssue().setWhisper("None");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.NONE.getColor()));
                    } else {
                        inventory.getIssue().setWhisper("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), Problem.MISSING_REACT_CLASS_ABILITY_T6.getColor()));
                    }
                }
            }
        } else {
            inventory.getIssue().setWhisper("/t " + account.getName() + " Please switch to the class you reacted to (" + name + ")");
            account.getRecentCharacter().setSkinImage(Utilities.markImage(account.getRecentCharacter().getSkinImage(), Problem.MISSING_REACT_CLASS.getColor()));
        }
    }
}
