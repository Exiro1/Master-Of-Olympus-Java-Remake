package com.exiro.buildingList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Immigrant;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class House extends Building {

    static final int[] maxPerLvl = {4, 8, 16, 24, 32, 40, 48, 54, 60};
    final ArrayList<Arrival> arrivals = new ArrayList<>();
    private final double deltaFood = 0.016f;
    private final double deltaWater = 0.016f;
    private final double deltaCulture = 0.5f;
    private final double deltaHdo = 0.001f;
    private final double deltaWool = 0.0008f;
    int maxfood, maxWater = 60, maxWool = 8, maxHDO = 5;
    private int level;
    private double food, water, wool, hdo, drama, gymnasium, philosophia;

    public House(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, int level) {
        super(false, ObjectType.HOUSE, null, pop, maxPerLvl[level], 15, 10, xPos, yPos, 2, 2, cases, built, city, 0);
        this.level = level;
    }

    private int popInArrival = 0;


    public House() {
        super(false, ObjectType.HOUSE, null, 0, maxPerLvl[0], 15, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
    }

    @Override
    public void process(double deltaTime) {
        super.process(deltaTime);
        if (isActive()) {
            addFood(-deltaFood * deltaTime * getPop());
            addWater(-deltaWater * deltaTime * getPop());
            addHdo(-deltaHdo * deltaTime * getPop());
            addWool(-deltaWool * deltaTime * getPop());

            addPhi(-deltaCulture * deltaTime);
            addGym(-deltaCulture * deltaTime);
            addDrama(-deltaCulture * deltaTime);


            if (level < 7) {
                if (popMax <= pop) {
                    boolean evolvabe = false;
                    switch (level) {
                        case 0:
                            evolvabe = true;//true
                            break;
                        case 1:
                            if (getFood() > pop && getWater() > 10)
                                evolvabe = true;
                            break;
                        case 2:
                            if (getFood() > pop && getWool() > 5 && getWater() > 10)
                                evolvabe = true;
                            break;
                        case 3:
                            if (getFood() > pop && getWool() > 5 && getWater() > 10 && getHdo() > 3)
                                evolvabe = true;
                            break;
                        case 4:
                            if (getFood() > pop && getWool() > 5 && getWater() > 15 && getHdo() > 3)
                                evolvabe = true;
                            break;
                        case 5:
                            if (getFood() > pop && getWool() > 7 && getWater() > 15 && getHdo() > 5)
                                evolvabe = true;
                            break;
                        case 6:
                            if (getFood() > pop && getWool() > 8 && getWater() > 15 && getHdo() > 5)
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
    public void Render(Graphics g, int camX, int camY) {
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        g.drawString(getPop() + "/" + getPopMax() + " " + (int) getFood() + " " + (int) getWater(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);

        //render only buildingSprite because movingSprite are render separately
        for (BuildingSprite s : bsprites) {
            if (isActive() && getPop() > 0)
                s.Render(g, camX, camY);
        }

    }

    @Override
    public void populate(double deltaTime) {
        ArrayList<Arrival> toRemove = new ArrayList<>();
        int i = 0;
        for (Arrival a : arrivals) {
            a.time -= (float) deltaTime;
            if (a.time <= 0) {
                toRemove.add(a);
                System.out.println(a);
                Immigrant immigrant = new Immigrant(city, city.getPathManager().getPathTo(city.getMap().getCase(0, 0), getAccess().get(0), FreeState.ALL_ROAD.i), this, a.getNbr());
                addSprite(immigrant);
            }
            i++;
        }
        SpriteManager();
        for (Arrival t : toRemove) {
            arrivals.remove(t);
        }


    }

    public void changeLvlImg(int lvl) {
        Random ran = new Random();
        TileImage t = ImageLoader.getImage(type.getPath(), type.getBitmapID(), -ran.nextInt(2) + lvl * 2 - 1);
        assert t != null;
        setImg(t);
    }

    public void addPopulation() {
        Random ran = new Random();
        float d = ran.nextFloat();
        d = d * 30;
        int nbr = Math.min((popMax - (pop + popInArrival)), 4);
        popInArrival = popInArrival + nbr;
        city.setPopInArrvial(city.getPopInArrvial() + nbr);
        arrivals.add(new Arrival(nbr, d));

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

    public void setWater(double water) {
        this.water = water;
    }

    public int getMaxfood() {
        return getPop() * 2;
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

    public void addGym(double gymnasium) {
        this.gymnasium = this.gymnasium + gymnasium;
        if (this.gymnasium < 0)
            this.gymnasium = 0;
    }

    public void addDrama(double drama) {
        this.drama = this.drama + drama;
        if (this.drama < 0)
            this.drama = 0;
    }

    public void addPhi(double philosophia) {
        this.philosophia = this.philosophia + philosophia;
        if (this.philosophia < 0)
            this.philosophia = 0;
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

    public void setDrama(double drama) {
        this.drama = drama;
    }

    public void setGymnasium(double gymnasium) {
        this.gymnasium = gymnasium;
    }

    public void setPhilosophia(double philosophia) {
        this.philosophia = philosophia;
    }

    public void setMaxfood(int maxfood) {
        this.maxfood = maxfood;
    }

    public int getMaxWater() {
        return maxWater;
    }

    public void setMaxWater(int maxWater) {
        this.maxWater = maxWater;
    }

    public int getMaxWool() {
        return maxWool;
    }

    public void setMaxWool(int maxWool) {
        this.maxWool = maxWool;
    }

    public int getMaxHDO() {
        return maxHDO;
    }

    public void setMaxHDO(int maxHDO) {
        this.maxHDO = maxHDO;
    }

    class Arrival {
        int nbr;
        float time;

        Arrival(int nbr, float time) {
            this.nbr = nbr;
            this.time = time;
        }

        int getNbr() {
            return nbr;
        }

        float getTime() {
            return time;
        }
    }
}
