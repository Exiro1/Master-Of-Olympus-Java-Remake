package com.exiro.sprite.industry;

import com.exiro.buildingList.IndustryHarverster;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Harvester;
import com.exiro.terrainList.Rock;

public class SilverHarvester extends Harvester {


//state search : 0 - 96 (12 frames)
    //state dying :  96 - 104 (uni dir) (8 frames)
    //state take on floor : 104 - 116 (uni dir) (12 frames)
    //state return home : 116 - 212 (12 frames)
    //state mining : 212 - 292 (10 frames)

    private static final int baseIndex = 3740;

    Rock silver;

    public SilverHarvester(City c, Case harvestSite, int harvestTime, IndustryHarverster industry, Rock r) {
        super("SprMain", 0, baseIndex, 12, c, null, harvestSite, harvestTime, industry);
        setOffsetY(-10);
        this.silver = r;
    }


    @Override
    public void arrivedToSite() {
        super.arrivedToSite();
        setDir(getDirToSite(silver.getMainCase()));
        setLocalID(baseIndex + 212);
        setFrameNumber(10);
    }

    @Override
    public void harvestFinished() {
        super.harvestFinished();
        setLocalID(baseIndex + 116);
        setFrameNumber(12);
        silver.setMined(false);
    }

    @Override
    public void delete() {
        super.delete();
        if (this.harvesting) {
            silver.setMined(false);
        }
    }
    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.SILVERMINER;
    }
}

