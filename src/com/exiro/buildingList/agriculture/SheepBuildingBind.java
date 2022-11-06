package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.ButtonType;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.animals.Sheep;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Meadow;

import java.awt.*;
import java.util.ArrayList;

public class SheepBuildingBind extends Building {


    public SheepBuildingBind() {
        super(false, ObjectType.SHEEP, BuildingCategory.FOOD, 0, 0, 10, 10, 0, 0, 1, 1, null, false, GameManager.currentCity, 0);
    }

    @Override
    public Interface getInterface() {
        return null;
    }

    @Override
    public void buttonClickedEvent(ButtonType type, int ID) {

    }

    @Override
    public void processSprite(double delta) {

    }

    @Override
    public void Render(Graphics g, int camX, int camY) {

    }

    @Override
    public void process(double deltatime, int deltaDays) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    @Override
    public ArrayList<Case> getAccess() {
        return null;
    }

    @Override
    public void delete() {

    }

    public boolean build(int xPos, int yPos) {

        if (getCity().getMap().getCase(xPos, yPos).getTerrain() instanceof Meadow && getCity().getMap().getCase(xPos, yPos).getObject() == null) {
            Sheep s = new Sheep();
            s.setX(xPos);
            s.setY(yPos);
            s.setXB(xPos);
            s.setYB(yPos);
            getCity().getResourceManager().addSheep(s);
            s.setMainCase(city.getMap().getCase(getXB(), getYB()));
            return true;
        }

        return true;
    }

    //@Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        if (getCity().getMap().getCase(xPos, yPos) == null)
            return null;
        if (getCity().getMap().getCase(xPos, yPos).getTerrain() instanceof Meadow && getCity().getMap().getCase(xPos, yPos).getObject() == null) {
            ArrayList<Case> cc = new ArrayList<>();
            cc.add(getCity().getMap().getCase(xPos, yPos));
            return cc;
        }
        return null;
    }

    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.NULL;
    }

}
