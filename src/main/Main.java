package main;
import inventory.Inventory;
import items.Rarity;
import upgrade.UpgradeService;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Inventory inventory = Inventory.getInstance();
        UpgradeService upgradeService = new UpgradeService();

        System.out.println("Welcome to the Item Upgrade System!");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Create Item");
            System.out.println("2. Display Inventory");
            System.out.println("3. Upgrade Item");
            System.out.println("4. Save Inventory");
            System.out.println("5. Load Inventory");
            System.out.println("6. Exit");
            System.out.print("Your choice: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createItem(scanner, inventory);
                    break;
                case "2":
                    inventory.displayInventory();
                    break;
                case "3":
                    upgradeItem(scanner, upgradeService);
                    break;
                case "4":
                    saveInventory(scanner, inventory);
                    break;
                case "5":
                    loadInventory(scanner, inventory);
                    break;
                case "6":
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please select again.");
            }
        }
    }


    private static void createItem(Scanner scanner, Inventory inventory) {
        System.out.print("Enter item name: ");
        String nameInput = scanner.nextLine().trim();
        if (nameInput.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }
        String name = nameInput.toUpperCase();

        System.out.println("Select rarity:");
        for (Rarity rarity : Rarity.values()) {
            System.out.println("- " + rarity.getDisplayName());
        }
        System.out.print("Enter rarity: ");
        String rarityInput = scanner.nextLine().trim().toUpperCase();

        try {
            Rarity rarity = Rarity.fromDisplayName(rarityInput);
            boolean success = inventory.addItem(name, rarity, 0);
            if (success) {
                System.out.println("Created: " + rarity.getDisplayName() + " " + name);
            } else {
                System.out.println("Failed to create item. An item with the name '" + name +
                        "' already exists with a different rarity.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rarity entered.");
        }
    }


    private static void upgradeItem(Scanner scanner, UpgradeService upgradeService) {
        System.out.print("Enter item name to upgrade: ");
        String nameInput = scanner.nextLine().trim();
        if (nameInput.isEmpty()) {
            System.out.println("Item name cannot be empty.");
            return;
        }
        String name = nameInput.toUpperCase();

        System.out.println("Select current rarity of the item:");
        for (Rarity rarity : Rarity.values()) {
            System.out.println("- " + rarity.getDisplayName());
        }
        System.out.print("Enter current rarity: ");
        String rarityInput = scanner.nextLine().trim().toUpperCase();

        try {
            Rarity currentRarity = Rarity.fromDisplayName(rarityInput);
            if (currentRarity != Rarity.EPIC &&  currentRarity != Rarity.LEGENDARY) {
                boolean success = upgradeService.upgradeItem(name, currentRarity, 0);
                if (success) {
                    System.out.println("Upgrade successful.");
                } else {
                    System.out.println("Upgrade failed.");
                }
            } else if (currentRarity == Rarity.EPIC) {
                System.out.print("Enter current level (0, 1, or 2): ");
                String levelInput = scanner.nextLine().trim();
                int currentLevel;
                try {
                    currentLevel = Integer.parseInt(levelInput);
                    if (currentLevel < 0 || currentLevel > 2) {
                        System.out.println("Invalid level entered. Must be 0, 1, or 2.");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid level entered. Must be an integer (0, 1, or 2).");
                    return;
                }
                boolean success = upgradeService.upgradeItem(name, currentRarity, currentLevel);
                if (success) {
                    System.out.println("Upgrade successful.");
                } else {
                    System.out.println("Upgrade failed.");
                }
            } else {
                System.out.println("Cannot upgrade Legendary items.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid rarity entered.");
        }
    }


    private static void saveInventory(Scanner scanner, Inventory inventory) {
        System.out.print("Enter filename to save inventory (e.g., inventory.ser): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Filename cannot be empty.");
            return;
        }
        inventory.saveInventory(filename);
    }


    private static void loadInventory(Scanner scanner, Inventory inventory) {
        System.out.print("Enter filename to load inventory from (e.g., inventory.ser): ");
        String filename = scanner.nextLine().trim();
        if (filename.isEmpty()) {
            System.out.println("Filename cannot be empty.");
            return;
        }
        inventory.loadInventory(filename);
    }
}


