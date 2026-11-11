package com.exiro;

import com.exiro.fileManager.FontLoader;
import com.exiro.fileManager.ImageLoader;
import com.exiro.fileManager.SoundLoader;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Player;
import com.exiro.render.EntityRender;
import com.exiro.soundManager.SoundManager;
import com.exiro.systemCore.GameManager;
import com.exiro.systemCore.GameLoop;
import com.exiro.systemCore.Simulation;
import com.exiro.systemCore.World;
import com.exiro.terrainGenerator.MapCreatorFrame;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {


        if(args.length > 0 && args[0].contains("map")){

                EventQueue.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        MapCreatorFrame ex = new MapCreatorFrame();
                        ex.setVisible(true);
                    }
                });
        }else {
            SoundLoader.init();
            ImageLoader.initLoader();
            ImageLoader.getImage("SprMain", 0, 81);
            FontLoader.initLoader();

            SoundManager sm = new SoundManager();

            System.setProperty("sun.awt.noerasebackground", "true");
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            ToolTipManager ttm = ToolTipManager.sharedInstance();
            ttm.setLightWeightPopupEnabled(false);

            World world = new World();
            Player p = new Player(world, 1000f, 0, "Exiro");
            City c = p.getPlayerCities().get(0);

            GameManager gm = new GameManager(world, p, c);

            EntityRender.setEntityRender(ObjectType.ROAD);




            Simulation simulation = new Simulation(gm, sm);
            Thread loop = new Thread(new GameLoop(gm, simulation), "GameLoop");
            loop.start();

            loop.join();
            sm.exit();
            SoundLoader.exit();

        }

    }





}
