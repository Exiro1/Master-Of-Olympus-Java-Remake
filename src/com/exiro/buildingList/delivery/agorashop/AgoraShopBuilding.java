package com.exiro.buildingList.delivery.agorashop;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class AgoraShopBuilding extends Building {

    AgoraShop shop;


    public AgoraShopBuilding(AgoraShop shop, ObjectType type) {
        super(false, type, BuildingCategory.FOOD, 0, 0, 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
        this.shop = shop;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        if (city.getMap().getCase(xPos, yPos) != null && city.getMap().getCase(xPos, yPos).getObject() != null && city.getMap().getCase(xPos, yPos).getObject() instanceof Agora) {
            ArrayList<Case> c = new ArrayList<>();
            c.add(city.getMap().getCase(xPos, yPos));
            return c;
        }
        return null;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> cases = getPlace(xPos, yPos, 0, 0, city);
        if (cases != null) {
            Agora agora = (Agora) cases.get(0).getObject();
            return agora.addShop(this.shop);
        }
        return false;
    }

    @Override
    public void processSprite(double delta) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

}
