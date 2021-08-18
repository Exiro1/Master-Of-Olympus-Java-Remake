package com.exiro;

import com.exiro.fileManager.FontLoader;
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
        ImageLoader.getImage("SprMain", 0, 81);
        FontLoader.initLoader();

        System.setProperty("sun.awt.noerasebackground", "true");
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        ttm.setLightWeightPopupEnabled(false);

        Player p = new Player(1000f, 0, "Exiro");
        City c = p.getPlayerCities().get(0);

        GameManager gm = new GameManager(p, c);

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
