package items;


public enum Rarity {
    COMMON("COMMON"),
    GREAT("GREAT"),
    RARE("RARE"),
    EPIC("EPIC"),
    LEGENDARY("LEGENDARY");

    private final String displayName;

    Rarity(String displayName) {
        this.displayName = displayName;
    }


    public String getDisplayName() {
        return displayName;
    }


    public static Rarity fromDisplayName(String input) {
        for (Rarity rarity : Rarity.values()) {
            if (rarity.getDisplayName().equalsIgnoreCase(input)) {
                return rarity;
            }
        }
        throw new IllegalArgumentException("No enum constant for display name: " + input);
    }
}
