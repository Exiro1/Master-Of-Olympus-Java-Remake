package com.exiro.buildingList.stockage;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class Granary extends StoreBuilding {


    public Granary(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city) {
        super(false, ObjectType.GRANARY, BuildingCategory.MARKET, pop, 18, 80, 10, xPos, yPos, 4, 4, cases, built, city, 0);
    }

    public Granary() {
        super(false, ObjectType.GRANARY, BuildingCategory.MARKET, 0, 18, 80, 10, 0, 0, 4, 4, null, false, GameManager.currentCity, 0);
    }


    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite("Zeus_General", 3, 29, 16, city, this);
            s.setTimeBetweenFrame(0.1f);
            s.setOffsetX(111);
            s.setOffsetY(31);
            addSprite(s);
            updateStock();
        }
        return succ;
    }




    @Override
    public void Render(Graphics g, int camX, int camY) {
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        g.drawString(getPop() + "/" + getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);

        //render only buildingSprite because movingSprite are render separately

        int i = 0;
        for (BuildingSprite s : bsprites) {
            if ((isActive() && getPop() > 0) || i > 0)
                s.Render(g, camX, camY);
            i++;
        }

    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {
        if (isActive() && getPop() > 0) {

        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    @Override
    public boolean canStock(Resource r) {
        return r.canGranary();
    }

    public void updateStock() {
        int i = 0;

        ArrayList<BuildingSprite> toR = new ArrayList<>();
        for (int l = 1; l < bsprites.size(); l++) {
            toR.add(bsprites.get(l));
        }
        for (BuildingSprite bs : toR) {
            removeSprites(bs);
        }
        toR.clear();
        for (Resource r : stockage.keySet()) {
            int k = 0;
            for (int j = 0; j < stockage.get(r); j++) {
                k++;
                if (k * r.getWeight() >= 4) {
                    setCaseRes(r, i);
                    k = 0;
                    i++;
                }
            }
            if (k > 0) {
                setCaseRes(r, i);
                i++;
            }
        }
        for (; i < 8; i++) {
            setCaseRes(Resource.NULL, i);
        }
    }

    public int getRessourceIndex(Resource res) {
        int i = 0;
        switch (res) {

            case SEA_URCHIN:
                i = 45;
                break;
            case FISH:
                i = 46;
                break;
            case MEAT:
                i = 47;
                break;
            case CHEESE:
                i = 48;
                break;
            case CARROT:
                i = 49;
                break;
            case ONION:
                i = 50;
                break;
            case CORN:
                i = 51;
                break;
            case NULL:
                return 0;
        }
        return i;
    }

    public void setCaseRes(Resource r, int index) {
        int i = 0;
        if ((i = getRessourceIndex(r)) == 0)
            return;
        int ofx = 0, ofy = 0;
        switch (index) {
            case 0:
                ofx = 33;
                ofy = -48;
                break;
            case 1:
                ofx = 53;
                ofy = -55;
                break;
            case 2:
                ofx = 74;
                ofy = -62;
                break;
            case 3:
                ofx = 94;
                ofy = -69;
                break;
            case 4:
                ofx = 114;
                ofy = -76;
                break;
            case 5:
                ofx = 38;
                ofy = -65;
                break;
            case 6:
                ofx = 58;
                ofy = -72;
                break;
            case 7:
                ofx = 78;
                ofy = -79;
                break;
        }

        BuildingSprite bs = new BuildingSprite("Zeus_General", 3, i, 1, city, this);
        bs.setOffsetX(ofx);
        bs.setOffsetY(ofy);
        addSprite(bs);
    }


}
