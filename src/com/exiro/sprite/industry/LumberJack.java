package com.exiro.sprite.industry;

import com.exiro.buildingList.IndustryHarverster;
import com.exiro.constructionList.Tree;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.sprite.Direction;
import com.exiro.sprite.Harvester;

public class LumberJack extends Harvester {


//state search : 0 - 96 (12 frames)
    //state dying :  96 - 104 (uni dir) (8 frames)
    //state mining : 104 - 200 (12 frames)
    //state return home : 200 - 280 (12 frames)

    private static int baseIndex = 4328;

    Tree tree;

    public LumberJack(City c, Case harvestSite, int harvestTime, IndustryHarverster industry, Tree tree) {
        super("SprMain", 0, baseIndex, 12, c, null, harvestSite, harvestTime, industry);
        setOffsetY(-10);
        this.tree = tree;
        this.tree.setBeingcut(true);
    }


    @Override
    public void arrivedToSite() {
        super.arrivedToSite();
        setDir(Direction.NORD_EST);
        setLocalID(baseIndex + 104);
        setFrameNumber(12);
    }

    @Override
    public void harvestFinished() {
        super.harvestFinished();
        setDir(getDirToSite(tree.getMainCase()));
        setLocalID(baseIndex + 200);
        setFrameNumber(12);
        tree.setCut(true);
        tree.setBeingcut(false);
    }

    @Override
    public void delete() {
        super.delete();
        if (this.harvesting) {
            tree.setBeingcut(false);
        }
    }

}
