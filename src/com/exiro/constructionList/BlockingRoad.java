package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;

public class BlockingRoad extends Construction {


    Road r;

    public BlockingRoad(City city) {
        super(true, ObjectType.BLOCKABLE_ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, true);
    }

    public BlockingRoad() {
        super(true, ObjectType.BLOCKABLE_ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, true);
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    Case c = city.getMap().getCase(xPos + j, yPos - i);
                    if (c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> place;
        place = getPlace(xPos, yPos, yLenght, xLenght, city);

        if (place.size() == xLenght * yLenght) {
            this.xPos = xPos;
            this.yPos = yPos;
            setXB(xPos);
            setYB(yPos);
            r = (Road) place.get(0).getObject();
            city.getConstructions().remove(r);
            city.removeObj(r);
            this.built = true;
            city.getOwner().pay(this.cost);
            city.addConstruction(this);
            city.addObj(this);
            for (Case c : place) {
                c.setOccupied(true);
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
    public void delete() {
        super.delete();
        r.getMainCase().setObject(r);
        r.getMainCase().setOccupied(true);
        city.getConstructions().add(r);
        city.addObj(r);
    }

    @Override
    public void process(double deltatime, int deltaDays) {

    }

}
