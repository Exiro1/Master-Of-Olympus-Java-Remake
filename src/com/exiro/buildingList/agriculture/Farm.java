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
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Meadow;
import com.exiro.utils.Point;
import com.exiro.utils.Time;

import java.awt.*;
import java.util.ArrayList;

public class Farm extends ResourceGenerator {

    int Rlevel;
    float growth;
    TileImage growthImg;


    public Farm() {
        super(false, ObjectType.FARM, BuildingCategory.FOOD, 0, 10, 36, 10, 0, 0, 3, 3, null, false, GameManager.currentCity, 0, Resource.CORN,8);
        this.Rlevel = 0;
        maxPerCarter = 4;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        bi.addText("Reserve de " + getStock() + " chargements de " + getResource().getName(), 16, 20, 80);
        bi.addText("La production est complétée à " + percentCompleted+"%", 16, 20, 110);

        return bi;
    }

    public void setFarmType(Resource r) {
        this.setResource(r);
    }


    int tickSinceStart = 0;
    int integralStaff = 0;
    int year = -1;
    int month = 6;
    Time nextEvolve = null;
    Time timeStart = null;
    Time startConvertion ;
    int percentCompleted = 0;

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive() && getPop() > 0) {


            tickSinceStart++;
            integralStaff += getPop();
            if(startConvertion == null)
                startConvertion = GameManager.getInstance().getTimeManager().getTime();
            int timeSinceStart = GameManager.getInstance().getTimeManager().daysSince(startConvertion);
            percentCompleted = Math.min((int) (((float) timeSinceStart / (float) 365) * 100), 100);

            if (year == -1) {
                if (GameManager.getInstance().getTimeManager().timeHasPassed(GameManager.getInstance().getTimeManager().getYear(), month, 0)) {
                    year = GameManager.getInstance().getTimeManager().getYear() + 1;
                    timeStart = GameManager.getInstance().getTimeManager().getTime();
                    tickSinceStart = 0;
                    integralStaff = 0;
                    Rlevel = 0;
                    nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);

                } else {
                    year = GameManager.getInstance().getTimeManager().getYear();
                    timeStart = GameManager.getInstance().getTimeManager().getTime();
                    tickSinceStart = 0;
                    integralStaff = 0;
                    Rlevel = 0;
                    nextEvolve = GameManager.getInstance().getTimeManager().getFutureTime(0, 2, 0);
                }
            } else if (GameManager.getInstance().getTimeManager().timeHasPassed(year, month, 0) && (getStock()<getMaxStockOut())) {
                int unit = (int) Math.ceil((((float) integralStaff / ((float) tickSinceStart * getPopMax())) * 8.0f) * (GameManager.getInstance().getTimeManager().daysSince(timeStart) / 360.0f));
                resourceCreated(unit);
                tickSinceStart = 0;
                integralStaff = 0;
                Rlevel = 0;
                startConvertion = GameManager.getInstance().getTimeManager().getTime();
                timeStart = GameManager.getInstance().getTimeManager().getTime();
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
                    month = 6;
                    break;
                case CARROT:
                    i += 19;
                    month = 3;
                    break;
                case ONION:
                    i += 25;
                    month = 3;
                    break;
            }
            growthImg = ImageLoader.getImage(getPath(), getBitmapID(), i);

        }
        return succ;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> cases = super.getPlace(xPos, yPos, yLenght, xLenght, city);

        for(Case c : cases){
            if(c.getTerrain() instanceof Meadow)
                return cases;
        }
        cases.clear();
        return cases;
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
