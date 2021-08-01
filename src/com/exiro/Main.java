package com.exiro;

import com.exiro.buildingList.House;
import com.exiro.buildingList.delivery.WaterWell;
import com.exiro.buildingList.stockage.Stock;
import com.exiro.constructionList.Road;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Player;
import com.exiro.render.EntityRender;
import com.exiro.systemCore.GameManager;
import com.exiro.systemCore.GameThread;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        ImageLoader.initLoader();

        System.setProperty("sun.awt.noerasebackground", "true");
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        ttm.setLightWeightPopupEnabled(false);

        Player p = new Player(1000f, 0, "Exiro");
        City c = p.getPlayerCities().get(0);

        GameManager gm = new GameManager(p, c);


        House h = new House(0, 0, 0, null, false, c, 0);
        h.build(4, 4);
        WaterWell puit = new WaterWell(false, 0, 0, 0, null, false, c);
        puit.build(7, 4);
        WaterWell puit2 = new WaterWell(false, 0, 0, 0, null, false, c);
        puit2.build(7, 8);
        WaterWell puit3 = new WaterWell(false, 0, 0, 0, null, false, c);
        puit3.build(7, 11);
        Stock s = new Stock(0, 0, c);
        s.build(12, 12);

        for (int i = 0; i < 7; i++) {
            Road r = new Road(c);
            r.build(i, 0);
        }
        Road r2 = new Road(c);
        r2.build(6, 1);
        Road r3 = new Road(c);
        r3.build(6, 2);
        Road r4 = new Road(c);
        r4.build(6, 3);
        /*  Immigrant immigrant = new Immigrant(c,c.getPathManager().getPathTo(c.getMap().getCase(0,0),c.getMap().getCase(6,3), RoadMap.FreeState.NON_BLOCKING),null);
           c.addSprite(immigrant);*/
        EntityRender.setEntityRender(ObjectType.ROAD);

        System.out.println(c.getMap().toString());

        Thread t = new Thread(new GameThread(gm));
        t.start();
    }

    /*
    static void test() throws IOException {
        TileImage img = ImageLoader.getImage("Zeus_Terrain",5,60);
        img.getID();
    }
    */


}
