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
import com.exiro.utils.Time;

import java.awt.*;
import java.util.ArrayList;

public class Farm extends ResourceGenerator {

    int Rlevel;
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

    public void setFarmType(Resource r) {
        this.setResource(r);
    }


    int tickSinceStart = 0;
    int integralStaff = 0;
    int year = -1;
    int month = 6;
    Time nextEvolve = null;


    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive() && getPop() > 0) {


            tickSinceStart++;
            integralStaff += getPop();

            if (year == -1) {
                if (GameManager.getInstance().getTimeManager().timeHasPassed(GameManager.getInstance().getTimeManager().getYear(), month, 0)) {
                    year = GameManager.getInstance().getTimeManager().getYear() + 1;
                    tickSinceStart = 0;
                    integralStaff = 0;
                    Rlevel = 0;
                    nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);

                } else {
                    year = GameManager.getInstance().getTimeManager().getYear();
                    tickSinceStart = 0;
                    integralStaff = 0;
                    Rlevel = 0;
                    nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);
                }
            } else if (GameManager.getInstance().getTimeManager().timeHasPassed(year, month, 0)) {
                int unit = (int) Math.ceil(((float) integralStaff / ((float) tickSinceStart * getPopMax())) * 8.0f);
                resourceCreated(unit);
                tickSinceStart = 0;
                integralStaff = 0;
                Rlevel = 0;
                year = GameManager.getInstance().getTimeManager().getYear() + 1;
                nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);
            }

            if (GameManager.getInstance().getTimeManager().timeHasPassed(nextEvolve)) {
                if (Rlevel < 6) {
                    Rlevel++;
                    changeLevel(Rlevel);
                }
                nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);
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
            int i = 0;
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
