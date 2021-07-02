package com.exiro.Object;

public enum Ressource {

    SEA_URCHIN("Oursin", 1, false, true, 4),
    FISH("Poisson", 1, false, true, 4),
    MEAT("Viande", 1, false, true, 4),
    CHEESE("Fromage", 1, false, true, 4),
    CARROT("Carotte", 1, false, true, 4),
    ONION("Oignon", 1, false, true, 4),
    WOOD("Bois", 1, true, false, 4),
    BRONZE("Bronze", 1, true, false, 4),
    MARBLE("Marbre", 1, true, false, 4),
    GRAPE("Raisin", 1, true, false, 4),
    OLIVE("Olive", 1, true, false, 4),
    WOOL("Laine", 1, true, false, 4),
    ARMEMENT("Armement", 1, true, false, 4),
    OLIVE_OIL("Huile d'olive", 1, true, false, 4),
    WINE("Vin", 1, true, false, 4),
    SCULPTURE("Sculpture", 4, true, false, 1),
    CORN("Blé", 1, true, true, 4),
    NULL("Vide", 1, true, true, 1),
    ;

    private final String name;
    private final int weight;
    private final int maxPerCart;
    private final boolean stock;
    private final boolean granary;


    Ressource(String name, int weight, boolean stock, boolean granary, int maxPerCart) {
        this.name = name;
        this.weight = weight;
        this.stock = stock;
        this.granary = granary;
        this.maxPerCart = maxPerCart;
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
}
