package com.exiro;

import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Player;
import com.exiro.render.EntityRender;
import com.exiro.systemCore.GameManager;
import com.exiro.systemCore.GameThread;
import com.exiro.terrainGenerator.MapCreatorFrame;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {


        if(args.length > 0 && args[0].contains("map")){

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MapCreatorFrame ex = new MapCreatorFrame();
                        ex.setVisible(true);
                    }
                });
        }else {
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

            EntityRender.setEntityRender(ObjectType.ROAD);

            System.out.println(c.getMap().toString());

            Thread t = new Thread(new GameThread(gm));
            t.start();
        }

    }





}
