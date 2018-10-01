package com.exiro.BuildingList;

import com.exiro.MoveRelated.RoadMap;
import com.exiro.Object.Case;
import com.exiro.Object.City;
import com.exiro.Sprite.Immigrant;
import com.exiro.Sprite.Sprite;
import com.exiro.SystemCore.GameManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class House extends Building {

    public static BufferedImage imgSet;
    static int[] maxPerLvl = {1, 5, 10, 15, 20, 25, 30, 35, 45};
    private final double deltaFood = 0.001f;
    private final double deltaWater = 0.002f;
    private final double deltaHdo = 0.0005f;
    private final double deltaWool = 0.0005f;
    Map<Integer, Double> timeBeforeComming = new HashMap<>();
    private int level;
    private double food, water, wool, hdo;
    private int popInArrival = 0;

    public House(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, int level) {
        super(false, BuildingType.HOUSE, "Assets/Building/House/DefaultHouse.png", 118, 128, 2, null, pop, maxPerLvl[level], 50, 10, xPos, yPos, 2, 2, cases, built, city, 0);
        this.level = level;

        setImg(imgSet.getSubimage(0, 0, 118, 128));
    }

    static public House DEFAULT() {
        House h = new House(0, 0, 0, null, false, GameManager.currentCity, 0);
        // h.setHeight(64);
        return h;
    }

    static public void loadSet() {
        try {
            File f = new File("Assets/Building/House/house.png");
            imgSet = ImageIO.read(f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BufferedImage getRender() {
        return getImg();
    }

    /**
     * Detruit le batiment et l objet
     */
    @Override
    public void delete() {
        super.delete();
        city.setPopInArrvial(city.getPopInArrvial() - popInArrival);
        city.setPopulation(city.getPopulation() - pop);

    }

    @Override
    public void process(double deltaTime) {

        addFood(-deltaFood * deltaTime);
        addWater(-deltaWater * deltaTime);
        addHdo(-deltaHdo * deltaTime);
        addWool(-deltaWool * deltaTime);


        if (level < 7) {
            if (popMax <= pop) {
                boolean evolvabe = true;
                switch (level) {
                    case 0:
                        evolvabe = true;//true
                        break;
                    case 1:
                        if (getFood() < 50 || getWater() < 50)
                            evolvabe = true;
                        break;
                    case 2:
                        if (getFood() < 50 || getWool() < 50 || getWater() < 50)
                            evolvabe = true;
                        break;
                    case 3:
                        if (getFood() < 50 || getWool() < 50 || getWater() < 50 || getHdo() < 50)
                            evolvabe = true;
                        break;
                    case 4:
                        if (getFood() < 50 || getWool() < 50 || getWater() < 50 || getHdo() < 50)
                            evolvabe = true;
                        break;
                    case 5:
                        if (getFood() < 50 || getWool() < 50 || getWater() < 50 || getHdo() < 50)
                            evolvabe = true;
                        break;
                    case 6:
                        if (getFood() < 50 || getWool() < 50 || getWater() < 50 || getHdo() < 50)
                            evolvabe = true;
                        break;
                }
                if (evolvabe) {
                    level++;
                    Random ran = new Random();
                    setImg(imgSet.getSubimage(level * 118, ran.nextInt(1) * 128, 118, 128));
                    city.getMap().getCase(xPos, yPos).setImg(getImg());
                    popMax = maxPerLvl[level];
                }
            }
        }
        if (popMax > pop + popInArrival) {
            addPopulation();
        }

    }

    @Override
    public void populate(double deltaTime) {
        ArrayList<Integer> toRemove = new ArrayList<>();
        int i = 0;
        for (Integer a : timeBeforeComming.keySet()) {
            double f = timeBeforeComming.get(a);
            f = f - deltaTime;
            timeBeforeComming.replace(a, timeBeforeComming.get(a), f);
            if (f <= 0) {
                toRemove.add(a);
                System.out.println(a);
                Immigrant immigrant = new Immigrant(city, city.getPathManager().getPathTo(city.getMap().getCase(0, 0), getAccess().get(0), RoadMap.FreeState.ALL_ROAD), this, a);
                city.addSprite(immigrant);
                sprites.add(immigrant);
            }
            i++;
        }
        SpriteManager();
        for (Integer t : toRemove
        ) {
            timeBeforeComming.remove(t);
        }


    }

    public void SpriteManager() {
        ArrayList<Sprite> toDestroy = new ArrayList<>();
        synchronized (sprites) {
            for (Sprite s : sprites) {
                if (s.hasArrived) {
                    toDestroy.add(s);
                    if (s instanceof Immigrant) {
                        pop = pop + ((Immigrant) s).getNbr();
                        popInArrival = popInArrival - ((Immigrant) s).getNbr();
                        city.setPopInArrvial(city.getPopInArrvial() - ((Immigrant) s).getNbr());
                        city.setPopulation(city.getPopulation() + ((Immigrant) s).getNbr());
                    }
                }
            }
        }
        deleteSprites(toDestroy);

    }

    public void deleteSprites(ArrayList<Sprite> spritesToDestroy) {
        synchronized (sprites) {
            sprites.removeAll(spritesToDestroy);
        }
    }

    public void addPopulation() {
        Random ran = new Random();
        Double d = ran.nextDouble();
        d = d * 30;
        int nbr = (popMax - (pop + popInArrival)) <= 5 ? (popMax - (pop + popInArrival)) : 5;
        popInArrival = popInArrival + nbr;
        city.setPopInArrvial(city.getPopInArrvial() + nbr);
        timeBeforeComming.put(nbr, d);

    }


    public int getLevel() {
        return level;
    }

    public double getFood() {
        return food;
    }

    public void addFood(double food) {
        this.food = this.food + food;
        if (this.food < 0)
            this.food = 0;
    }

    public double getWater() {
        return water;
    }

    public void addWater(double water) {
        this.water = this.water + water;
        if (this.water < 0)
            this.water = 0;
    }

    public double getWool() {
        return wool;
    }

    public void addWool(double wool) {
        this.wool = this.wool + wool;
        if (this.wool < 0)
            this.wool = 0;
    }

    public double getHdo() {
        return hdo;
    }

    public void addHdo(double hdo) {
        this.hdo = this.hdo + hdo;
        if (this.hdo < 0)
            this.hdo = 0;
    }

    @Override
    public String toString() {
        return "Building{" +
                "type=" + type.name() +
                ", pop=" + pop +
                ", popMax=" + popMax +
                ", cost=" + cost +
                ", built=" + built +
                ", city=" + city.getName() +
                ", level=" + level +
                '}';
    }
}
