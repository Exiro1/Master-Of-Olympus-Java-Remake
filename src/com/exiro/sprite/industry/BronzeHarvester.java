package com.exiro.sprite.industry;

import com.exiro.buildingList.IndustryHarverster;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Harvester;
import com.exiro.terrainList.Rock;

public class BronzeHarvester extends Harvester {


    //state search : 0 - 96 (12 frames)
    //state dying :  96 - 104 (uni dir) (8 frames)
    //state take on floor : 104 - 116 (uni dir) (12 frames)
    //state return home : 116 - 212 (12 frames)
    //state mining : 212 - 292 (10 frames)


    Rock copper;

    public BronzeHarvester(City c, Case harvestSite, int harvestTime, IndustryHarverster industry, Rock r) {
        super("SprMain", 0, 2594, 12, c, null, harvestSite, harvestTime, industry);
        setOffsetY(-10);
        this.copper = r;
    }


    @Override
    public void arrivedToSite() {
        super.arrivedToSite();
        setDir(getDirToSite(copper.getMainCase()));
        setLocalID(2594 + 212);
        setFrameNumber(10);
    }

    @Override
    public void harvestFinished() {
        super.harvestFinished();
        setLocalID(2594 + 116);
        setFrameNumber(12);
        copper.setMined(false);
    }

    @Override
    public void delete() {
        super.delete();
        if (this.harvesting) {
            copper.setMined(false);
        }
    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.COPPERMINER;
    }
}
