package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryHarverster;
import com.exiro.constructionList.Tree;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Harvester;
import com.exiro.sprite.industry.LumberJack;
import com.exiro.systemCore.GameManager;

public class Sawmill extends IndustryHarverster {


    int harvester = 0;

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 1, 10, getCity(), this);
            s.setOffsetX(16);
            s.setOffsetY(-12);
            s.setTimeBetweenFrame(0.1f);
            addSprite(s);
            return true;
        }
        return false;
    }

    public Sawmill() {
        super(false, ObjectType.SAWMILL, BuildingCategory.INDUSTRY, 0, 12, 60, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.WOOD, 22, 3, 25, 100);
    }

    @Override
    public void processSprite(double delta) {
        super.processSprite(delta);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isWorking()) {
            if (harvester < harvesterNbr) {
                Case dir = null;
                Tree t = null;
                for (Case c : city.getMap().getTrees()) {
                    if (!((Tree) c.getObject()).isBeingcut() && !((Tree) c.getObject()).isCut() && ((Tree) c.getObject()).getAccessible()) {
                        for (Case n : c.getNeighbour()) {
                            if (city.getPathManager().getPathTo(getAccess().get(0), n, FreeState.WALKABLE.getI()) != null) {
                                dir = n;
                                t = (Tree) c.getObject();
                                t.setBeingcut(true);
                                break;
                            }
                        }
                        if (dir != null)
                            break;
                    }
                }
                if (dir != null) {
                    LumberJack bh = new LumberJack(city, dir, timeToHarvest, this, t);
                    addSprite(bh);
                    harvester++;
                }
            }
        }
    }

    @Override
    public void harvestFinished(Harvester har) {
        super.harvestFinished(har);
        harvester--;
    }

    @Override
    protected void addPopulation() {

    }
}
