package com.exiro.object;

public enum Resource {

    SEA_URCHIN("Oursin", 1, false, true, 4, 100),
    FISH("Poisson", 1, false, true, 4, 100),
    MEAT("Viande", 1, false, true, 4, 150),
    CHEESE("Fromage", 1, false, true, 4, 75),
    CARROT("Carotte", 1, false, true, 4, 75),
    ONION("Oignon", 1, false, true, 4, 75),
    WOOD("Bois", 1, true, false, 4, 0),
    BRONZE("Bronze", 1, true, false, 4, 0),
    MARBLE("Marbre", 1, true, false, 4, 0),
    GRAPE("Raisin", 1, true, false, 4, 0),
    OLIVE("Olive", 1, true, false, 4, 0),
    WOOL("Laine", 1, true, false, 4, 100),
    ARMEMENT("Armement", 1, true, false, 4, 1),
    OLIVE_OIL("Huile d'olive", 1, true, false, 4, 5),
    WINE("Vin", 1, true, false, 4, 5),
    SCULPTURE("Sculpture", 4, true, false, 1, 0),
    CORN("Blé", 1, true, true, 4, 100),
    NULL("Vide", 1, true, true, 1, 0),
    ;

    private final String name;
    private final int weight;
    private final int maxPerCart;
    private final boolean stock;
    private final boolean granary;
    private final int foodapprov;


    Resource(String name, int weight, boolean stock, boolean granary, int maxPerCart, int foodapprov) {
        this.name = name;
        this.weight = weight;
        this.stock = stock;
        this.granary = granary;
        this.maxPerCart = maxPerCart;
        this.foodapprov = foodapprov;
    }

    public int getMaxPerCart() {
        return maxPerCart;
    }

    public boolean isStock() {
        return stock;
    }

    public boolean isGranary() {
        return granary;
    }

    public String getName() {
        return name;
    }

    public int getWeight() {
        return weight;
    }

    public boolean canStock() {
        return stock;
    }

    public boolean canGranary() {
        return granary;
    }

    public int getFoodapprov() {
        return foodapprov;
    }
}
