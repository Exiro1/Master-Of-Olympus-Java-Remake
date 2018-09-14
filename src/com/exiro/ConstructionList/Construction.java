package com.exiro.ConstructionList;

import com.exiro.BuildingList.BuildingType;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;

import java.util.ArrayList;

public abstract class Construction extends ObjectClass {

    ArrayList<Case> cases;
    int cost;
    int deleteCost;
    int xPos, yPos;
    int xLenght, yLenght;
    float cachet;
    City city;
    boolean built;


    public Construction(boolean isActive, BuildingType type, int width, int height, int size, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built) {
        super(isActive, type, type.getPath(), width, height, size);
        this.cases = cases;
        this.cost = cost;
        this.deleteCost = deleteCost;
        this.xPos = xPos;
        this.yPos = yPos;
        this.xLenght = xLenght;
        this.yLenght = yLenght;
        this.cachet = cachet;
        this.city = city;
        this.built = built;
    }

    public static ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    if (!city.getMap().getCase(xPos + j, yPos - i).isOccuped()) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    public boolean build(int xPos, int yPos) {
        ArrayList<Case> place;
        place = getPlace(xPos, yPos, yLenght, xLenght, city);

        if (place.size() == xLenght * yLenght) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.built = true;
            city.getOwner().pay(this.cost);
            city.getConstructions().add(this);
            city.addObj(this);
            for (Case c : place) {
                c.setOccuped(true);
                c.setBuildingType(getBuildingType());
                c.setObject(this);
                c.setMainCase(false);
            }
            city.getMap().getCase(xPos, yPos).setMainCase(true);
            city.getMap().getCase(xPos, yPos).setImg(getImg());
            city.getMap().getCase(xPos, yPos).setHeight(getHeight());
            city.getMap().getCase(xPos, yPos).setWidth(getWidth());
            city.getMap().getCase(xPos, yPos).setSize(getSize());
            cases = place;

            return true;
        } else {
            return false;
        }


    }

    public void delete() {

        if (this.built) {
            city.getOwner().pay(this.getDeleteCost());
            city.getConstructions().remove(this);
            city.removeObj(this);
            for (Case c : getCases()) {
                c.setBuildingType(BuildingType.EMPTY);
                c.setOccuped(false);
                c.setObject(null);
            }
        }
    }

    public ArrayList<Case> getAccess() {
        ArrayList<Case> access = new ArrayList<>();
        for (int i = 0; i < xLenght + 2; i++) {
            for (int j = 0; j < yLenght + 2; j++) {
                if (!((i == 0 && j == 0) || (i == 0 && j == yLenght + 1) || (i == xLenght + 1 && j == 0) || (i == xLenght + 1 && j == yLenght + 1))) {
                    Case c = city.getMap().getCase(xPos + i - 1, yPos - j + 1);

                    if (c != null && !cases.contains(c) && c.getBuildingType() == BuildingType.ROAD && c.getObject().isActive()) {
                        access.add(c);
                    }
                }
            }
        }
        return access;
    }


    public ArrayList<Case> getCases() {
        return cases;
    }

    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getDeleteCost() {
        return deleteCost;
    }

    public void setDeleteCost(int deleteCost) {
        this.deleteCost = deleteCost;
    }

    public int getxPos() {
        return xPos;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public int getYpos() {
        return yPos;
    }

    public void setYpos(int ypos) {
        yPos = ypos;
    }

    public int getxLenght() {
        return xLenght;
    }

    public void setxLenght(int xLenght) {
        this.xLenght = xLenght;
    }

    public int getyLenght() {
        return yLenght;
    }

    public void setyLenght(int yLenght) {
        this.yLenght = yLenght;
    }

    public float getCachet() {
        return cachet;
    }

    public void setCachet(float cachet) {
        this.cachet = cachet;
    }
}
