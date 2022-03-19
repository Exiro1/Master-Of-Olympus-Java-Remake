package com.exiro.object;

import java.util.Arrays;
import java.util.List;

public enum Resource {

    SEA_URCHIN("Oursin", 1, false, true, 4, 100, null),
    FISH("Poisson", 1, false, true, 4, 100, null),
    MEAT("Viande", 1, false, true, 4, 150, null),
    CHEESE("Fromage", 1, false, true, 4, 75, null),
    CARROT("Carotte", 1, false, true, 4, 75, null),
    ONION("Oignon", 1, false, true, 4, 75, null),
    WOOD("Bois", 1, true, false, 4, 0, null),
    BRONZE("Bronze", 1, true, false, 4, 0, Arrays.asList(ObjectType.SCULPTURE_STUDIO, ObjectType.ARMORY)),
    MARBLE("Marbre", 1, true, false, 4, 0, Arrays.asList()),
    GRAPE("Raisin", 1, true, false, 4, 0, Arrays.asList(ObjectType.WINERY)),
    OLIVE("Olive", 1, true, false, 4, 0, Arrays.asList(ObjectType.OLIVE_PRESS)),
    WOOL("Laine", 1, true, false, 4, 100, null),
    ARMEMENT("Armement", 1, true, false, 4, 100, Arrays.asList()),
    OLIVE_OIL("Huile d'olive", 1, true, false, 4, 100, null),
    WINE("Vin", 1, true, false, 4, 100, null),
    SCULPTURE("Sculpture", 4, true, false, 1, 0, null),
    CORN("Blé", 1, true, true, 4, 100, null),
    NULL("Vide", 1, true, true, 1, 0, null),
    ;

    private final String name;
    private final int weight;
    private final int maxPerCart;
    private final boolean stock;
    private final boolean granary;
    private final int foodapprov;
    private final List<ObjectType> delivery;

    Resource(String name, int weight, boolean stock, boolean granary, int maxPerCart, int foodapprov, List<ObjectType> delivery) {
        this.name = name;
        this.weight = weight;
        this.stock = stock;
        this.granary = granary;
        this.maxPerCart = maxPerCart;
        this.foodapprov = foodapprov;
        this.delivery = delivery;
    }

    public List<ObjectType> getDelivery() {
        return delivery;
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
