package com.exiro.object;

import com.exiro.constructionList.SmallHoldingFruit.SmallHoldingTree;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.animals.Goat;
import com.exiro.sprite.animals.Sheep;

import java.util.ArrayList;

public class ResourceManager {

    private final ArrayList<Sprite> animals = new ArrayList<>();
    private final ArrayList<Sheep> sheeps = new ArrayList<>();
    private final ArrayList<Goat> goats = new ArrayList<>();
    private final ArrayList<SmallHoldingTree> smallHoldingTrees = new ArrayList<>();
    private final ArrayList<SmallHoldingTree> matureTrees = new ArrayList<>();

    public ResourceManager() {

    }

    public void process() {

        matureTrees.removeIf(smallHoldingTree -> !smallHoldingTree.isMature());
        for (SmallHoldingTree tree : smallHoldingTrees) {
            if (tree.isMature() && !matureTrees.contains(tree))
                matureTrees.add(tree);
        }

    }


    public ArrayList<SmallHoldingTree> getMatureTrees() {
        return matureTrees;
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

    public ArrayList<Sheep> getSheeps() {
        return sheeps;
    }

    public void addTree(SmallHoldingTree t) {
        synchronized (sheeps) {
            smallHoldingTrees.add(t);
        }
    }

    public void removeTree(SmallHoldingTree t) {
        synchronized (smallHoldingTrees) {
            smallHoldingTrees.remove(t);
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


}
