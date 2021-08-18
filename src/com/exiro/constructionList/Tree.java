package com.exiro.constructionList;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Time;

import java.util.ArrayList;
import java.util.Random;

public class Tree extends Construction {


    int terraintype; // 0 = normal   1 = tall grass   2 = swamp
    boolean accessible;
    boolean cut;
    boolean beingcut;
    Time grown;

    public Tree(int xpos, int ypos, int id, City c, int terraintype) {
        super(false, ObjectType.TREE, null, 0, 2, xpos, ypos, 1, 1, 0, c, true, false);
        this.setLocalID(id);
        this.setBitmapID(4);
        this.terraintype = terraintype;
        updateImg();
        setXB(xpos);
        setYB(ypos);
        city.addConstruction(this);
        city.addObj(this);
        Case ca = city.getMap().getCase(xPos, yPos);
        cases = new ArrayList<>();
        cases.add(ca);

        ca.setOccuped(true);
        ca.setObject(this);
        ca.setMainCase(true);
        this.setMainCase(ca);
    }

    @Override
    public void process(double deltatime) {
        if (cut) {
            if (GameManager.getInstance().getTimeManager().timeHasPassed(grown)) {
                setCut(false);
            }
        }
    }

    public boolean isCut() {
        return cut;
    }


    public void setCut(boolean cut) {
        this.cut = cut;
        if (cut) {
            grown = GameManager.getInstance().getTimeManager().getFutureTime(0, 3, 0);
            Random r = new Random();
            this.setLocalID(terraintype * 8 + r.nextInt(8) + 96);
        } else {
            Random r = new Random();
            int nbr = r.nextInt(96);
            this.setLocalID(nbr);
        }
        updateImg();
    }

    public boolean isBeingcut() {
        return beingcut;
    }

    public void setBeingcut(boolean beingcut) {
        this.beingcut = beingcut;
    }

    public boolean getAccessible() {
        return accessible;
    }

    public void setAccessible(boolean accessible) {
        this.accessible = accessible;
    }
}
