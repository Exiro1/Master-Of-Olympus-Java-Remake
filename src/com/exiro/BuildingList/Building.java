package com.exiro.BuildingList;

import com.exiro.ConstructionList.Construction;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Sprite.Sprite;

import java.util.ArrayList;

public abstract class Building extends ObjectClass {

    BuildingType type;
    ArrayList<Sprite> sprites = new ArrayList<>();
    BuildingCategory category;
    int pop;
    int popMax;
    int cost, deleteCost;
    int xPos, yPos, yLenght, xLenght;
    ArrayList<Case> cases;
    boolean built;
    City city;
    private int ID;


    public Building(boolean isActive, BuildingType type, String path, int width, int height, int size, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, path, width, height, size);
        if (!isActive)
            city.getInActives().add(this);
        this.category = category;
        this.pop = pop;
        this.popMax = popMax;
        this.cost = cost;
        this.deleteCost = deleteCost;
        this.xPos = xPos;
        this.yPos = yPos;
        this.yLenght = yLenght;
        this.xLenght = xLenght;
        this.cases = cases;
        this.built = built;
        this.city = city;
        this.ID = ID;
        this.type = type;
    }


    abstract public void process(double deltaTime);

    /**
     * Remplis si possible le batiment
     * Appelés a chaque image par GameThread
     */
    abstract public void populate(double deltaTime);

    /**
     * Appelé a chaque image , gere l'ajout de personne dans le batiment
     */
    abstract void addPopulation();

    /**
     * Construit le Batiment
     *
     * @param xPos Coordonnée en X
     * @param yPos Coordonnée en Y
     */
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> place;
        place = Construction.getPlace(xPos, yPos, yLenght, xLenght, city);

        if (place.size() == xLenght * yLenght) {
            this.xPos = xPos;
            this.yPos = yPos;
            this.built = true;


            city.getOwner().pay(this.cost);
            city.addBuilding(this);
            city.addObj(this);
            for (Case c : place) {
                c.setOccuped(true);
                c.setBuildingType(type);
                c.setObject(this);
                c.setMainCase(false);
            }
            cases = place;
            city.getMap().getCase(xPos, yPos).setMainCase(true);
            city.getMap().getCase(xPos, yPos).setImg(getImg());
            city.getMap().getCase(xPos, yPos).setHeight(getHeight());
            city.getMap().getCase(xPos, yPos).setWidth(getWidth());
            city.getMap().getCase(xPos, yPos).setSize(getSize());
            if (getAccess().size() > 0) {
                setActive(true);
                city.getInActives().remove(this);
            } else {
                setActive(false);
                city.getInActives().add(this);
            }


            return true;
        } else {
            return false;
        }


    }


    public ArrayList<Case> getAccess() {
        ArrayList<Case> access = new ArrayList<>();
        for (int i = 0; i < xLenght + 2; i++) {
            for (int j = 0; j < yLenght + 2; j++) {
                if (!((i == 0 && j == 0) || (i == 0 && j == yLenght + 1) || (i == xLenght + 1 && j == 0) || (i == xLenght + 1 && j == yLenght + 1))) {
                    Case c = city.getMap().getCase(xPos + i - 1, yPos - j + 1);

                    if (c != null && c.getBuildingType() == BuildingType.ROAD && c.getObject().isActive()) {
                        access.add(c);
                    }
                }
            }
        }
        return access;
    }

    /**
     * Detruit le batiment et l objet
     */
    public void delete() {

        if (this.built) {
            city.getOwner().pay(this.getDeleteCost());
            city.removeBuildingj(this);
            for (Case c : getCases()) {
                c.setBuildingType(BuildingType.EMPTY);
                c.setOccuped(false);
                c.setObject(null);
                c.setMainCase(true);
            }
            city.removeObj(this);
        }
    }

    @Override
    public String toString() {
        return "Building{" +
                "type=" + type.name() +
                ", pop=" + pop +
                ", popMax=" + popMax +
                ", cost=" + cost +
                ", built=" + built +
                ", city=" + city.getName() +
                '}';
    }

    public int getPopMax() {
        return popMax;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getCost() {
        return cost;
    }

    public int getDeleteCost() {
        return deleteCost;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public BuildingCategory getCategory() {
        return category;
    }

    public void setCategory(BuildingCategory category) {
        this.category = category;
    }
}
