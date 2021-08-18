package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public abstract class Construction extends ObjectClass {

    ArrayList<Case> cases;
    int cost;
    int deleteCost;
    int xPos, yPos;
    int xLenght, yLenght;
    float cachet;
    final City city;
    boolean built;
    boolean isFloor;


    public Construction(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, boolean isFloor) {
        super(isActive, type);
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
        this.isFloor = isFloor;
    }

    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    Case c = city.getMap().getCase(xPos + j, yPos - i);
                    if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = new BuildingInterface(300, 300, 500, 400, null, this);
        bi.addText(getType().getName(), "Zeus.ttf", 32f, 250 - 32 * getType().getName().length() / 2 + 16, 50);
        return bi;
    }

    public abstract void process(double deltatime);

    public boolean build(int xPos, int yPos) {
        ArrayList<Case> place;
        place = getPlace(xPos, yPos, yLenght, xLenght, city);

        if (place.size() == xLenght * yLenght) {
            this.xPos = xPos;
            this.yPos = yPos;
            setXB(xPos);
            setYB(yPos);

            this.built = true;
            city.getOwner().pay(this.cost);
            city.addConstruction(this);
            city.addObj(this);
            for (Case c : place) {
                c.setOccuped(true);
                c.setObject(this);
                c.setMainCase(false);
            }
            this.setMainCase(city.getMap().getCase(xPos, yPos));
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

    @Override
    public void Render(Graphics g, int camX, int camY) {
        int lvl = getMainCase().getZlvl();
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - lvl, getYpos() - lvl), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
    }

    public void delete() {

        if (this.built) {
            city.getOwner().pay(this.getDeleteCost());
            city.getConstructions().remove(this);
            city.removeObj(this);
            for (Case c : getCases()) {
                c.setOccuped(false);
                c.setObject(null);
            }
        }
    }

    public ArrayList<Case> getAccess() {
        if (cases == null)
            return null;
        ArrayList<Case> access = new ArrayList<>();
        for (int i = 0; i < xLenght + 2; i++) {
            for (int j = 0; j < yLenght + 2; j++) {
                if (!((i == 0 && j == 0) || (i == 0 && j == yLenght + 1) || (i == xLenght + 1 && j == 0) || (i == xLenght + 1 && j == yLenght + 1))) {
                    Case c = city.getMap().getCase(xPos + i - 1, yPos - j + 1);
                    if (c != null && c.getObject() != null) {
                        if (!cases.contains(c) && c.getObject().getBuildingType() == ObjectType.ROAD && c.getObject().isActive()) {
                            access.add(c);
                        }
                    }
                }
            }
        }
        return access;
    }

    public boolean isFloor() {
        return isFloor;
    }

    public void setFloor(boolean floor) {
        isFloor = floor;
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
