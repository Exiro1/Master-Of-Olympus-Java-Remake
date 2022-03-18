package com.exiro.object;

import com.exiro.buildingList.agriculture.SmallHolding;
import com.exiro.constructionList.SmallHoldingFruit.SmallHoldingTree;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.animals.Fish;
import com.exiro.sprite.animals.Goat;
import com.exiro.sprite.animals.Sheep;

import java.util.ArrayList;

public class ResourceManager {

    private final ArrayList<Sprite> animals = new ArrayList<>();
    private final ArrayList<Sheep> sheeps = new ArrayList<>();
    private final ArrayList<Goat> goats = new ArrayList<>();
    private final ArrayList<Fish> fishes = new ArrayList<>();
    private final ArrayList<SmallHolding> smallHoldings = new ArrayList<>();
    private final ArrayList<SmallHoldingTree> smallHoldingTrees = new ArrayList<>();

    public ResourceManager() {

    }

    public void process() {

    }


    public void removeAnimal(Sprite o) {
        synchronized (animals) {
            animals.remove(o);
        }
    }

    public void addAnimal(Sprite s) {
        synchronized (animals) {
            animals.add(s);
        }
    }

    public void addSheep(Sheep s) {
        addAnimal(s);
        synchronized (sheeps) {
            sheeps.add(s);
        }
    }

    public void removeSheep(Sheep s) {
        removeAnimal(s);
        synchronized (sheeps) {
            sheeps.remove(s);
        }
    }

    public void addSmallHolding(SmallHolding s) {
        synchronized (smallHoldings) {
            smallHoldings.add(s);
        }
        synchronized (smallHoldingTrees) {
            for (SmallHoldingTree t : smallHoldingTrees) {
                if (s.distanceTo(t) <= 40) {
                    s.addTree(t);
                }
            }
        }
    }

    public void removeSmallHolding(SmallHolding s) {
        synchronized (smallHoldings) {
            smallHoldings.remove(s);
        }
    }

    public ArrayList<Sheep> getSheeps() {
        return sheeps;
    }

    public void addTree(SmallHoldingTree t) {
        synchronized (smallHoldingTrees) {
            smallHoldingTrees.add(t);
        }
        for (SmallHolding sh : smallHoldings) {
            if (sh.distanceTo(t) <= 40) {
                sh.addTree(t);
            }
        }
    }

    public void removeTree(SmallHoldingTree t) {
        synchronized (smallHoldingTrees) {
            smallHoldingTrees.remove(t);
        }
        for (SmallHolding sh : smallHoldings) {
            sh.removeTree(t);
        }
    }

    public ArrayList<SmallHoldingTree> getTrees() {
        return smallHoldingTrees;
    }


    public void addGoat(Goat s) {
        addAnimal(s);
        synchronized (goats) {
            goats.add(s);
        }
    }

    public void removeGoat(Goat s) {
        removeAnimal(s);
        synchronized (goats) {
            goats.remove(s);
        }
    }

    public ArrayList<Goat> getGoats() {
        return goats;
    }

    public ArrayList<Sprite> getAnimals() {
        return animals;
    }


    public void addFish(Fish fish) {
        addAnimal(fish);
        synchronized (fishes) {
            fishes.add(fish);
        }
    }

    public ArrayList<Fish> getFishes() {
        return fishes;
    }

    public void removeFish(Fish fish) {
        removeAnimal(fish);
        synchronized (fishes) {
            fishes.remove(fish);
        }
    }

}
