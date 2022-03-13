package com.exiro.constructionList.SmallHoldingFruit;

import com.exiro.constructionList.Construction;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Meadow;

import java.util.ArrayList;
import java.util.Random;

public class SmallHoldingTree extends Construction {

    static Random ran = new Random();
    boolean mature = false;
    boolean available = true;
    int monthStart = 0;
    int lvl = 0;
    int startYear;
    boolean update = false;
    int growth = 0;

    public SmallHoldingTree(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, int monthStart) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, false);
        this.monthStart = monthStart;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean success = super.build(xPos, yPos);
        if (success) {
            city.getResourceManager().addTree(this);
        }
        return success;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> cases = super.getPlace(xPos, yPos, yLenght, xLenght, city);

        for (Case c : cases) {
            if (c.getTerrain() instanceof Meadow)
                return cases;
        }
        cases.clear();
        return cases;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean av) {
        this.available = av;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Fruit mûr à " + Math.min((int) (growth / 3.0f), 100) + "%", 16, 20, 80);
        return bi;
    }//

    @Override
    public void process(double deltatime, int deltaDays) {


        int month = GameManager.getInstance().getTimeManager().getMonth();

        growth += deltaDays + ran.nextInt(3) - 1;

        if (month == (monthStart) && lvl != 0) {
            update = true;
            lvl = 0;
            growth = 0;
            mature = false;
        }

        if (growth > (lvl + 1) * 60 && lvl != -1 && lvl < 5) {
            lvl++;
            update = true;
        }

        if (update) {
            if (lvl == 5)
                mature = true;
            update = false;
            setImg(ImageLoader.getImage(getBuildingType().getPath(), getBitmapID(), getLocalID() + lvl));
        }
    }

    public int getLvl() {
        return lvl;
    }

    public boolean isMature() {
        return mature;
    }

    @Override
    public void delete() {
        super.delete();
        city.getResourceManager().removeTree(this);
    }

    public int getTreeLvl() {
        return lvl;
    }

    public void resourceGathered() {
        lvl = -1;
        growth = 0;
        mature = false;
        setImg(ImageLoader.getImage(getBuildingType().getPath(), getBitmapID(), getLocalID()));
    }

}
