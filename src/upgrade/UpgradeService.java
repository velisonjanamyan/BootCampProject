package upgrade;

import inventory.Inventory;
import items.Rarity;


public class UpgradeService {
    private Inventory inventory;

    public UpgradeService() {
        inventory = Inventory.getInstance();
    }



    public boolean upgradeItem(String name, Rarity currentRarity, int currentLevel) {
        int itemCount = inventory.getItemCount(name, currentRarity, currentLevel);
        if (itemCount == 0) return false;
        if (currentRarity == Rarity.EPIC) {
            if (currentLevel == 0) {
                if (itemCount > 1) {
                    boolean addStatus = inventory.addItemForUpgrade(name, currentRarity, currentLevel + 1);
                    if (!addStatus) {
                        return false;
                    }
                    boolean removeStatus = inventory.removeItems(name, currentRarity, currentLevel, 2);
                    if (!removeStatus) {
                        return false;
                    }
                    return true;
                }
                System.out.println("Not enough EPIC0 items to perform the upgrade.");
                return false;

            }
            if (currentLevel == 1) {
                itemCount += inventory.getItemCount(name, currentRarity, 0);
                if (itemCount > 1) {
                    boolean addStatus = inventory.addItemForUpgrade(name, currentRarity, currentLevel + 1);
                    if (!addStatus) {
                        return false;
                    }
                    boolean removeStatus = inventory.removeItems(name, currentRarity, 0, 1);
                    if(!removeStatus) {
                        removeStatus = inventory.removeItems(name, currentRarity, currentLevel, 2);
                    }else {
                        removeStatus = inventory.removeItems(name, currentRarity, currentLevel, 1);
                    }
                    if (!removeStatus) {
                        return false;
                    }
                    return true;
                }
                System.out.println("Not enough EPIC1 items to perform the upgrade.");
                return false;
            }
            if (currentLevel == 2 && itemCount > 2) {
                boolean addStatus = inventory.addItemForUpgrade(name, Rarity.LEGENDARY, currentLevel + 1);
                if (!addStatus) {
                    return false;
                }
                boolean removeStatus = inventory.removeItems(name, currentRarity, currentLevel, 3);
                if (!removeStatus) {
                    return false;
                }
                return true;
            }
            System.out.println("Not enough EPIC2 items to perform the upgrade.");
            return false;
        }

        if (itemCount >= 3) {
            boolean addStatus = inventory.addItemForUpgrade(name, getNextRarity(currentRarity), 0);
            if (!addStatus) {
                return false;
            }
            boolean removeStatus = inventory.removeItems(name, currentRarity, currentLevel, 3);
            if (!removeStatus) {
                return false;
            }
            return true;
        }
        System.out.println("Not enough " + name + " items to perform the upgrade.");
        return false;
    }

    private Rarity getNextRarity(Rarity current) {
        switch (current) {
            case COMMON:
                return Rarity.GREAT;
            case GREAT:
                return Rarity.RARE;
            case RARE:
                return Rarity.EPIC;
            case EPIC:
                return Rarity.LEGENDARY;
            default:
                return null;
        }
    }
}

