package com.exiro.buildingList.agriculture;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.ResourceGenerator;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.IsometricRender;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class Farm extends ResourceGenerator {

    int Rlevel;
    float speedFactor = 1;
    float growth;
    TileImage growthImg;


    public Farm(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLength, int xLength, ArrayList<Case> cases, boolean built, City city, int ID, Resource resource, int level) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLength, xLength, cases, built, city, ID, resource);
        this.Rlevel = level;
    }

    public Farm(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, Resource resource, int level) {
        super(false, ObjectType.FARM, BuildingCategory.FOOD, pop, 10, 36, 10, xPos, yPos, 3, 3, cases, built, city, 0, resource);
        this.Rlevel = level;
    }

    public Farm() {
        super(false, ObjectType.FARM, BuildingCategory.FOOD, 0, 10, 36, 10, 0, 0, 3, 3, null, false, GameManager.currentCity, 0, Resource.CORN);
        this.Rlevel = 0;
    }




    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive() && getPop() > 0) {
            float factor = (getPop() * 1.0f) / (getPopMax() * 1.0f);
            growth += factor * deltaTime * speedFactor;

            if (growth > 40) {
                growth = 0;
                Rlevel++;
                changeLevel(Rlevel);
            }
            if (Rlevel > 5) {
                Rlevel = 0;
                resourceCreated(6);
            }

        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        super.Render(g, camX, camY);
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), growthImg.getW(), growthImg.getH(), 1);
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
        switch (getResource()) {
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


    public void resourceCreated(int unit) {
        super.resourceCreated(unit);
        changeLevel(0);
    }


    @Override
    protected void addPopulation() {

    }
}
