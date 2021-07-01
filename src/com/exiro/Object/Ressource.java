package com.exiro.Object;

public enum Ressource {

    SEA_URCHIN("Oursin", 1, false, true),
    FISH("Poisson", 1, false, true),
    MEAT("Viande", 1, false, true),
    CHEESE("Fromage", 1, false, true),
    CARROT("Carotte", 1, false, true),
    ONION("Oignon", 1, false, true),
    WOOD("Bois", 1, true, false),
    BRONZE("Bronze", 1, true, false),
    MARBLE("Marbre", 1, true, false),
    GRAPE("Raisin", 1, true, false),
    OLIVE("Olive", 1, true, false),
    WOOL("Laine", 1, true, false),
    ARMEMENT("Armement", 1, true, false),
    OLIVE_OIL("Huile d'olive", 1, true, false),
    WINE("Vin", 1, true, false),
    SCULPTURE("Sculpture", 4, true, false),
    CORN("Blé", 1, true, true),
    NULL("Vide", 1, true, true),
    ;

    private final String name;
    private final int weight;
    private final boolean stock;
    private final boolean granary;


    Ressource(String name, int weight, boolean stock, boolean granary) {
        this.name = name;
        this.weight = weight;
        this.stock = stock;
        this.granary = granary;
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
