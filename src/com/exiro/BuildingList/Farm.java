package com.exiro.BuildingList;

import com.exiro.FileManager.ImageLoader;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Object.Ressource;
import com.exiro.Render.IsometricRender;
import com.exiro.Sprite.Carter;
import com.exiro.Sprite.MovingSprite;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;
import com.exiro.Utils.Point;
import com.exiro.depacking.TileImage;

import java.awt.*;
import java.util.ArrayList;

public class Farm extends Building {

    Ressource ressource;
    int Rlevel;
    float speedFactor = 1;
    float growth;
    TileImage growthImg;
    int stock = 0;


    public Farm(boolean isActive, BuildingType type, String path, int size, int bitmapID, int localID, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID, Ressource ressource, int level) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        this.ressource = ressource;
        this.Rlevel = level;
    }

    public Farm(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, Ressource ressource, int level) {
        super(false, BuildingType.FARM, BuildingCategory.FOOD, pop, 15, 50, 10, xPos, yPos, 3, 3, cases, built, city, 0);
        this.ressource = ressource;
        this.Rlevel = level;
    }

    public Farm() {
        super(false, BuildingType.FARM, BuildingCategory.FOOD, 0, 15, 50, 10, 0, 0, 3, 3, null, false, GameManager.currentCity, 0);
        this.ressource = Ressource.CORN;
        this.Rlevel = 0;
    }


    @Override
    public void delete() {
        super.delete();
        for (MovingSprite ms : msprites) {
            if (ms instanceof Carter) {
                Carter c = (Carter) ms;
                ObjectClass obj = c.getDestination();
                if (obj instanceof Granary) {
                    Granary g = (Granary) obj;
                    g.unReserved(ressource, c.getCurrentDelivery());
                }
                if (obj instanceof Stock) {
                    Stock s = (Stock) obj;
                    s.unReserved(ressource, c.getCurrentDelivery());
                }
            }
        }
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {

        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            factor = 10;
            growth += factor * deltaTime * speedFactor;

            if (growth > 10) {
                growth = 0;
                Rlevel++;
                changeLevel(Rlevel);
            }
            if (Rlevel > 5) {
                Rlevel = 0;
                recoltEnded(6);
            }

        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
        com.exiro.Utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), growthImg.getW(), growthImg.getH(), 1);
        g.drawImage(growthImg.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() + 1, (getyPos())), growthImg.getW(), growthImg.getH(), 1);
        g.drawImage(growthImg.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() + 2, (getyPos() - 2)), growthImg.getW(), growthImg.getH(), 1);
        g.drawImage(growthImg.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() + 2, (getyPos() - 1)), growthImg.getW(), growthImg.getH(), 1);
        g.drawImage(growthImg.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() + 2, (getyPos())), growthImg.getW(), growthImg.getH(), 1);
        g.drawImage(growthImg.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);


    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            growthImg = ImageLoader.getImage(getPath(), getBitmapID(), 13);

        }
        return succ;
    }

    public void changeLevel(int rlevel) {
        int i = rlevel;
        switch (ressource) {
            case CORN:
                i += 13;
                break;
            case CARROT:
                i += 19;
                break;
            case ONION:
                i += 25;
                break;
        }
        growthImg = ImageLoader.getImage(getPath(), getBitmapID(), i);
    }


    public void recoltEnded(int unit) {
        changeLevel(0);
        stock += unit;
    }

    public void manageCarter() {
        if (stock > 0) {
            int toDeliver = Math.min(stock, 4);
            stock -= toDeliver;
            Carter carter = new Carter(city, null, this, Ressource.CORN, toDeliver);
            addSprite(carter);
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if (c.hasArrived) {
                Carter carter = (Carter) c;
                ObjectClass des = c.getDestination();
                if (des == null || !des.isActive() || ((Building) des).isDeleted()) { //object supprimée / inactif -> on relance la recherche
                    c.hasArrived = false;
                    c.setRoutePath(null);
                    des = null;
                } else if (des instanceof Granary) {
                    Granary g = (Granary) des;
                    g.stock(ressource, carter.getCurrentDelivery());
                    carter.setAmmount(carter.getAmmount() - carter.getCurrentDelivery());
                    if (carter.getAmmount() > 0) {
                        c.hasArrived = false;
                        c.setRoutePath(null);
                    } else {
                        toDestroy.add(c);
                    }
                } else if (des instanceof Stock) {
                    Stock g = (Stock) des;
                    g.stock(ressource, carter.getCurrentDelivery());
                    carter.setAmmount(carter.getAmmount() - carter.getCurrentDelivery());
                    if (carter.getAmmount() > 0) {
                        c.hasArrived = false;
                        c.setRoutePath(null);
                    } else {
                        toDestroy.add(c);
                    }

                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
    }

    @Override
    public void populate(double deltaTime) {
        manageCarter();
    }

    @Override
    void addPopulation() {

    }
}
