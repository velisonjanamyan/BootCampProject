package inventory;
import items.Rarity;

import java.io.*;
import java.util.*;

public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L;

    private static Inventory instance;

    private Map<String, Map<Rarity, Map<Integer, Integer>>> map;

    private Inventory() {
        map = new HashMap<>();
    }

    protected Object readResolve() {
        return getInstance();
    }


    public static Inventory getInstance() {
        if (instance == null) {
            instance = new Inventory();
        }
        return instance;
    }

    public boolean addItemForUpgrade(String name, Rarity rarity, int level) {
        Map<Integer, Integer> integerIntegerMap = map.computeIfAbsent(name, k -> new HashMap<>()).computeIfAbsent(rarity, k -> new HashMap<>());
        integerIntegerMap.put(level, integerIntegerMap.getOrDefault(level, 0) + 1);
        return true;
    }


    public boolean addItem(String name, Rarity rarity, int level) {
        if (!map.containsKey(name)) {
            map.computeIfAbsent(name, (k) -> new HashMap<>()).
                    computeIfAbsent(rarity, (k) -> new HashMap<>()).put(level, 1);
            return true;
        }

        Map<Rarity, Map<Integer, Integer>> rarityMap = map.get(name);

        if (!rarityMap.containsKey(rarity)) {

            System.out.println("Error: Cannot create item with name '" + name +
                    "' and rarity '" + rarity.getDisplayName() + "'.");
            System.out.println("Only items with existing rarities can be created for this name.");
            return false;
        }

        Integer i = rarityMap.get(rarity).computeIfPresent(level, (k, v) -> v + 1);
        if(i == null) {
            rarityMap.get(rarity).put(level, 1);
        }
        return true;
    }

    public boolean removeItems(String name, Rarity rarity, int level, int count) {
        Map<Rarity, Map<Integer, Integer>> rarityMapMap = map.get(name);
        if(rarityMapMap == null) {
            return false;
        }

        Map<Integer, Integer> integerIntegerMap = rarityMapMap.get(rarity);
        if(integerIntegerMap == null) {
            return false;
        }

        if(rarity == Rarity.EPIC) {
            if(!integerIntegerMap.containsKey(level)) {
                return false;
            }
        }
        Integer currentCount = integerIntegerMap.get(level);
        if(currentCount < count) {
            return false;
        }

        if(currentCount == count) {
            if (integerIntegerMap.size() == 1) {
                integerIntegerMap.remove(level);
                rarityMapMap.remove(rarity);
                if(rarityMapMap.isEmpty()) {
                    map.remove(name);
                }
            }else {
                integerIntegerMap.remove(level);
            }
            return true;
        }

        integerIntegerMap.put(level, currentCount - count);
        return true;
    }



    public int getItemCount(String name, Rarity rarity, int level) {
        if (!map.containsKey(name)) return 0;

        Map<Rarity, Map<Integer, Integer>> rarityMap = map.get(name);
        if (!rarityMap.containsKey(rarity)) return 0;

        Map<Integer, Integer> levelMap = rarityMap.get(rarity);
        if(!levelMap.containsKey(level)) return 0;
        return levelMap.get(level);
    }

    public void displayInventory() {
        if (map.isEmpty()) {
            System.out.println("Inventory is empty.");
            return;
        }

        for (Map.Entry<String, Map<Rarity, Map<Integer, Integer>>> nameEntry : map.entrySet()) {
            String itemName = nameEntry.getKey();
            Map<Rarity, Map<Integer, Integer>> rarityMap = nameEntry.getValue();

            System.out.println("Item: " + itemName);
            for (Map.Entry<Rarity, Map<Integer, Integer>> rarityEntry : rarityMap.entrySet()) {
                Rarity rarity = rarityEntry.getKey();
                Map<Integer, Integer> levelMap = rarityEntry.getValue();

                System.out.println("  Rarity: " + rarity.getDisplayName());
                for (Map.Entry<Integer, Integer> levelEntry : levelMap.entrySet()) {
                    int level = levelEntry.getKey();
                    int count = levelEntry.getValue();
                    if (isEpicRarity(rarity)) {
                        System.out.println("    Level " + level + ": " + count);
                    } else {
                        System.out.println("    Count: " + count);
                    }
                }
            }
        }
    }

    private boolean isEpicRarity(Rarity rarity) {
        return rarity == Rarity.EPIC;
    }


    public boolean saveInventory(String filename) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(instance);
            System.out.println("Inventory saved successfully to " + filename);
            return true;
        } catch (IOException e) {
            System.out.println("Failed to save inventory: " + e.getMessage());
            return false;
        }
    }


    public boolean loadInventory(String filename) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Inventory loadedInventory = (Inventory) ois.readObject();
            instance = loadedInventory;
            System.out.println("Inventory loaded successfully from " + filename);
            return true;
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
            return false;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Failed to load inventory: " + e.getMessage());
            return false;
        }
    }
}