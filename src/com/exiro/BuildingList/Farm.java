package com.exiro.BuildingList;

import com.exiro.FileManager.ImageLoader;
import com.exiro.MoveRelated.Path;
import com.exiro.MoveRelated.RoadMap;
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
    int carterWaiting = 0;


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
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            s.process(delta);
        }
    }

    @Override
    public void process(double deltaTime) {

        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            growth += factor * deltaTime * speedFactor;

            if (growth > 10) {
                growth = 0;
                Rlevel++;
                changeLevel(Rlevel);
            }
            if (Rlevel > 5) {
                Rlevel = 0;
                recoltEnded(8);
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
        for (int j = 0; j < Math.ceil(unit / 4.0); j++) {
            Carter carter = new Carter(city, null, this, Ressource.CORN, 3);
            addSprite(carter);
            carterWaiting++;
        }
    }

    public void manageCarter() {
        if (carterWaiting > 0) {
            //optimiser en ayant un hashmap dans city <BuildingType, ArrayList<Building> > ?
            for (Building b : city.getBuildings()) {
                if (b instanceof Granary) {
                    for (MovingSprite c : msprites) {
                        if (c.getRoutePath() != null)
                            continue;
                        Granary g = (Granary) b;
                        if (g.canStock(ressource, ((Carter) c).getAmmount())) {
                            Path p = city.getPathManager().getPathTo(getAccess().get(0), g.getAccess().get(0), RoadMap.FreeState.ALL_ROAD);
                            if (p != null) {
                                ((Carter) c).setPath(p);
                                carterWaiting--;
                                //g.reserve(ressource, ((Carter)c).getAmmount());
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : msprites) {
            if (c.hasArrived) {
                ObjectClass des = c.getDestination();
                if (des instanceof Granary) {
                    Granary g = (Granary) des;
                    g.stock(ressource, ((Carter) c).getAmmount());
                }
                toDestroy.add(c);
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
