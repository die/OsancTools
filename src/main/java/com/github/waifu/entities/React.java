package com.github.waifu.entities;

import com.github.waifu.util.Utilities;
import org.json.JSONArray;
import javax.swing.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * React class to store react data.
 */
public class React {

    private String id;
    private String name;
    private String type;
    private String requirement;
    private ImageIcon image;
    private List<Account> raiders;

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
    public React(String id, String name, String requirement, String imageURL, List<Account> raiders) throws MalformedURLException {
        this.id = id;
        this.name = name;
        this.type = "";
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
    public React(String id, String name, String type, String requirement, String imageURL, List<Account> raiders) throws MalformedURLException {
        this.id = id;
        this.name = name;
        this.type = type;
        this.requirement = requirement;
        this.image = new ImageIcon(new URL(imageURL));
        this.raiders = raiders;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequirement() {
        return this.requirement;
    }

    public ImageIcon getImage() {
        return this.image;
    }

    public List<Account> getRaiders() {
        return this.raiders;
    }

    public void setRaiders(List<Account> raiders) {
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
                for (Account a : raiders) {
                    parseItem(a);
                }
                break;
            case "class":
                for (Account a : raiders) {
                    parseClass(a);
                }
                break;
            case "dps":
                for (Account a : raiders) {
                    parseDps(a);
                }
                break;
            case "lock":
                for (Account a : raiders) {
                    a.getCharacters().get(0).getInventory().setMessage("/t " + a.getName() + " Please trade me so I can confirm your effusion.");
                }
                break;
            default:
                for (Account a : raiders) {
                    a.getCharacters().get(0).getInventory().setMessage("/lock " + a.getName());
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
        Inventory inventory = account.getCharacters().get(0).getInventory();
        if (inventory != null) {
            String ability = inventory.getAbility().getName();
            switch (name) {
                case "Ogmur/Pogmur":
                    if (ability.equals("Shield of Ogmur UT") || ability.equals("Crystallised Fang's Venom UT")) {
                        inventory.setMessage("None");
                        inventory.setProblem("Good", 0);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "good"));
                    } else {
                        inventory.setProblem("Missing Item", 2);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
                        inventory.setMessage("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                    }
                    break;
                case "Slow":
                    JSONArray allowedSlows = Utilities.json.getJSONArray("slows");
                    if (allowedSlows.toList().contains(ability)) {
                        inventory.setMessage("None");
                        inventory.setProblem("Good", 0);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "good"));
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                        inventory.setProblem("Missing Item", 2);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
                    }
                    break;
                case "Fungal Tome":
                    if (ability.equals("Tome of the Mushroom Tribes UT") || ability.equals("Non-Fungible Tome UT")) { // accounts for april fools name SMH
                        inventory.setMessage("None");
                        inventory.setProblem("Good", 0);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "good"));
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                        inventory.setProblem("Missing Item", 2);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
                    }
                    break;
                case "Marble Seal":
                    if (ability.equals("Marble Seal UT")) {
                        inventory.setMessage("None");
                        inventory.setProblem("Good", 0);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "good"));
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your " + name.toLowerCase() + " so I can confirm it. You can swap it out after.");
                        inventory.setProblem("Missing Item", 2);
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
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
        Inventory inventory = account.getCharacters().get(0).getInventory();
        String type = account.getCharacters().get(0).getType();
        if (inventory.getItems().stream().allMatch(item -> item.getName().equals("Empty slot"))) {
            inventory.setMessage("Private Profile");
        } else {
            switch (name) {
                case "Wizard/Summoner 2/4":
                    if (!(type.equals("Wizard") || type.equals("Summoner"))) {
                        inventory.setMessage("Wrong Class");
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "issue"));
                    } else {
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "good"));
                        if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 2)) {
                            inventory.setMessage("None");
                        } else {
                            inventory.setMessage("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "2/4+ Wizard":
                    if (!account.getCharacters().get(0).getType().equals("Wizard")) {
                        inventory.setMessage("Wrong Class");
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "issue"));
                    } else {
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "good"));
                        if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 2)) {
                            inventory.setMessage("None");
                        } else {
                            inventory.setMessage("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "2/4+ Ranged":
                    if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 2)) {
                        inventory.setMessage("None");
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your 2/4+ set so I can confirm it. You can swap it out after.");
                    }
                    break;
                case "Wizard/Summoner 3/4":
                    if (!(account.getCharacters().get(0).getType().equals("Wizard") || account.getCharacters().get(0).getType().equals("Summoner"))) {
                        inventory.setMessage("Wrong Class");
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "issue"));
                    } else {
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "good"));
                        if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 3)) {
                            inventory.setMessage("None");
                        } else {
                            inventory.setMessage("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "3/4+ Wizard":
                    if (!account.getCharacters().get(0).getType().equals("Wizard")) {
                        inventory.setMessage("Wrong Class");
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "issue"));
                    } else {
                        account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "good"));
                        if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 3)) {
                            inventory.setMessage("None");
                        } else {
                            inventory.setMessage("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
                        }
                    }
                    break;
                case "3/4+ Ranged":
                    if (inventory.calculateDps(account.getCharacters().get(0).getSkin(), 3)) {
                        inventory.setMessage("None");
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your 3/4+ set so I can confirm it. You can swap it out after.");
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
        String type = account.getCharacters().get(0).getType();
        Inventory inventory = account.getCharacters().get(0).getInventory();
        if (name.equals(type)) {
            account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "good"));
            if (requirement.contains("T6")) {
                if (inventory.getAbility().getName().equals("Empty slot") || inventory.getItems().get(1).getName().endsWith("UT") || inventory.getItems().get(1).getName().endsWith("ST")) {
                    inventory.setMessage("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after.");
                    inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
                } else {
                    if (Integer.parseInt(inventory.getItems().get(1).getName().substring(inventory.getItems().get(1).getName().length() - 1)) >= 6) {
                        inventory.setMessage("None");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "good"));
                    } else {
                        inventory.setMessage("/t " + account.getName() + " Please equip your T6/T7 ability so I can confirm it. You can swap it out after.");
                        inventory.getAbility().setImage(Utilities.markImage(inventory.getAbility().getImage(), "issue"));
                    }
                }
            }
        } else {
            inventory.setMessage("Wrong Class");
            account.getCharacters().get(0).setSkinImage(Utilities.markImage(account.getCharacters().get(0).getSkinImage(), "issue"));
        }
    }
}
