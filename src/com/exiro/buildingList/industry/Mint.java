package com.exiro.buildingList.industry;

import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.IndustryHarverster;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Harvester;
import com.exiro.sprite.industry.SilverHarvester;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Rock;

public class Mint extends IndustryHarverster {


    int harvester = 0;


    public void createBuildingSpriteWork() {
        BuildingSprite s = new BuildingSprite(getType().getPath(), getType().getBitmapID(), 21, 10, getCity(), this);
        s.setOffsetX(9);
        s.setOffsetY(16);
        s.setTimeBetweenFrame(0.1f);
        addSprite(s);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean succ = super.build(xPos, yPos);
        if (succ) {
            return true;
        }
        return false;
    }

    public Mint() {
        super(false, ObjectType.MINT, BuildingCategory.INDUSTRY, 0, 15, 160, 5, 0, 0, 2, 2, null, false, GameManager.currentCity, 0, Resource.NULL, 22, 3, 25, 200);
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
                Rock r = null;
                for (Case c : city.getMap().getSilvers()) {
                    if (!((Rock) c.getTerrain()).isMined() && ((Rock) c.getTerrain()).isAccessible()) {
                        for (Case n : c.getNeighbour()) {
                            if (city.getPathManager().getPathTo(getAccess().get(0), n, FreeState.WALKABLE.getI()) != null) {
                                dir = n;
                                r = ((Rock) c.getTerrain());
                                ((Rock) c.getTerrain()).setMined(true);
                                break;
                            }
                        }
                        if (dir != null)
                            break;
                    }
                }
                if (dir != null) {
                    SilverHarvester bh = new SilverHarvester(city, dir, timeToHarvest, this, r);
                    addSprite(bh);
                    harvester++;
                }
            }
        }
    }

    @Override
    public void harvestFinished(Harvester har) {
        //super.harvestFinished(har);
        unit += unitPerHarvester;
        if (unit >= this.unitNeeded) {
            city.getOwner().pay(-100); //we add 100 drachmas
            unit -= unitNeeded;
        }
        harvester--;
    }

    @Override
    protected void addPopulation() {

    }
}
