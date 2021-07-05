package com.exiro.buildingList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.RoadMap;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.sprite.Immigrant;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class House extends Building {


    static final int[] maxPerLvl = {1, 5, 10, 15, 20, 25, 30, 35, 45};
    private final double deltaFood = 0.001f;
    private final double deltaWater = 0.002f;
    private final double deltaHdo = 0.0005f;
    private final double deltaWool = 0.0005f;
    final Map<Integer, Double> timeBeforeComming = new HashMap<>();
    private int level;
    private double food, water, wool, hdo;
    private int popInArrival = 0;


    public House(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, int level) {
        super(false, ObjectType.HOUSE, null, pop, maxPerLvl[level], 50, 10, xPos, yPos, 2, 2, cases, built, city, 0);
        this.level = level;
    }

    public House() {
        super(false, ObjectType.HOUSE, null, 0, maxPerLvl[0], 50, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
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
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            s.process(delta);
        }
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
                    changeLvlImg(level);
                    popMax = maxPerLvl[level];
                }
            }
        }
        if (popMax > pop + popInArrival) {
            addPopulation();
        }

    }


    public void changeLvlImg(int lvl) {
        Random ran = new Random();
        TileImage t = ImageLoader.getImage(type.getPath(), type.getBitmapID(), -ran.nextInt(2) + lvl * 2 - 1);
        assert t != null;
        setImg(t);
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
                addSprite(immigrant);
            }
            i++;
        }
        SpriteManager();
        for (Integer t : toRemove) {
            timeBeforeComming.remove(t);
        }


    }

    public void SpriteManager() {
        ArrayList<Sprite> toDestroy = new ArrayList<>();

        synchronized (msprites) {
            for (MovingSprite s : msprites) {
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
        for (Sprite s : spritesToDestroy) {
            removeSprites(s);
        }
    }

    public void addPopulation() {
        Random ran = new Random();
        double d = ran.nextDouble();
        d = d * 30;
        int nbr = Math.min((popMax - (pop + popInArrival)), 5);
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
