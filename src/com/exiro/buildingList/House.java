package com.exiro.buildingList;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Immigrant;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;
import com.exiro.utils.Time;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class House extends Building {

    static final int[] maxPerLvl = {4, 8, 16, 24, 32, 40, 48, 54, 60};

    static final int[] evolveFood = {0, 0, 1, 1, 1, 1, 1, 1};
    static final int[] evolveWater = {0, 0, 0, 1, 1, 1, 1, 1};
    static final int[] evolveWool = {0, 0, 0, 0, 1, 1, 1, 1};
    static final int[] evolveHDO = {0, 0, 0, 0, 0, 0, 1, 1};
    static final int[] evolveCulture = {0, 0, 0, 15, 15, 35, 35, 45};
    static final int[] evolveAppel = {-10, -10, 0, 10, 10, 20, 30, 100};
    static final int[] devolveAppel = {-99, -99, -12, -2, -2, 5, 15, 25};

    final ArrayList<Arrival> arrivals = new ArrayList<>();
    private final double deltaFood = 0.25f;
    private final double deltaWater = 1f;//idk
    private final double deltaCulture = 20;
    private final double deltaHdo = 2f;
    private final double deltaWool = 2f;
    int maxWater = 60, maxWool = 8, maxHDO = 8,maxCulture=100;
    private int level;
    private double food, water, wool, hdo, drama, gymnasium, philosophia,competitor;
    private double appeal = 100;
    private Time timeDevolve = null;

    boolean willDevolve = false;
    EvolveCause evolveCause;

    enum EvolveCause {FOOD,WATER,CULTURE,WOOL,HDO,APPEAL,NONE};
    //enum HouseLevel {HUT,SHACK,HOVEL,HOMESTEAD,TENEMENT,APARTMENT,TOWNHOUSE}

    public House(int pop, int xPos, int yPos, ArrayList<Case> cases, boolean built, City city, int level) {
        super(false, ObjectType.HOUSE, null, pop, maxPerLvl[level], 15, 10, xPos, yPos, 2, 2, cases, built, city, 0);
        this.level = level;
    }

    private int popInArrival = 0;


    public House() {
        super(false, ObjectType.HOUSE, null, 0, maxPerLvl[0], 15, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive()) {
            addFood(-deltaFood * (deltaDays/30f) * getPop());
            addWater(-deltaWater * deltaDays);
            addHdo(-deltaHdo * (deltaDays/30f));
            addWool(-deltaWool * (deltaDays/30f));

            addPhi(-deltaCulture * (deltaDays/30f));
            addGym(-deltaCulture * (deltaDays/30f));
            addDrama(-deltaCulture * (deltaDays/30f));
            addCompetitor(-deltaCulture* (deltaDays/30f));

            checkEvolvability();

            if (popMax > pop + popInArrival) {
                addPopulation();
            }
        }
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();


        String reason = willDevolve?"Cette maison va se détériorer va cause de : " +evolveCause : "Cette maison ne peut pas évoluer a cause de : " +evolveCause;
        if(level == 0){
            reason = "Les habitants de cette maison arrivent";
        }
        bi.addCenteredText(reason,16f,100);

        bi.addImage(ImageLoader.FilePath.ZEUS_GENERAL,7,134,100,200);
        bi.addText(""+((int)getFood()),16f,120,200);
        bi.addImage(ImageLoader.FilePath.ZEUS_GENERAL,7,128,200,200);
        bi.addText(""+((int)getWool()),16f,220,200);
        bi.addImage(ImageLoader.FilePath.ZEUS_GENERAL,7,132,300,200);
        bi.addText(""+((int)getHdo()),16f,320,200);
        return bi;
    }

    public void checkEvolvability(){


        if(level>1) {
            boolean devolvabe = false;
            if (getFood() < evolveFood[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.FOOD;
            }
            if (getWater() < evolveWater[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.WATER;
            }
            if (getWool() < evolveWool[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.WOOL;
            }
            if (getHdo() < evolveHDO[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.HDO;
            }
            if (getCultureLvl() < evolveCulture[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.CULTURE;
            }
            if (getAppeal() < devolveAppel[level]) {
                devolvabe = true;
                evolveCause = EvolveCause.APPEAL;
            }

            if(devolvabe){
                if(timeDevolve == null){
                    timeDevolve = GameManager.getInstance().getTimeManager().getTime();
                    willDevolve = true;
                }else {
                    if(GameManager.getInstance().getTimeManager().daysSince(timeDevolve) > 30){
                        level--;
                        changeLvlImg(level);
                        popMax = maxPerLvl[level];
                        removePopulation(Math.max(0,pop-popMax));
                        timeDevolve = null;
                        willDevolve = false;
                        evolveCause = EvolveCause.NONE;
                    }
                }
                return;
            }else{
                timeDevolve = null;
                willDevolve = false;
                evolveCause = EvolveCause.NONE;
            }

        }
        if (popMax <= pop && level < 7) {
            boolean evolve = true;
            if(getFood() >= evolveFood[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.FOOD;
            }
            if(getWater() >= evolveWater[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.WATER;
            }
            if(getWool() >= evolveWool[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.WOOL;
            }
            if(getHdo() >= evolveHDO[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.HDO;
            }
            if(getCultureLvl() >= evolveCulture[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.CULTURE;
            }
            if(getAppeal() >= evolveAppel[level+1]){

            }else{
                evolve = false;
                evolveCause = EvolveCause.APPEAL;
            }
            if(evolve)
            {
                level++;
                changeLvlImg(level);
                popMax = maxPerLvl[level];
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
        int lvl = getMainCase().getZlvl();
        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - lvl, getyPos() - lvl), getWidth(), getHeight(), getSize());
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

    public void newPopulation(Immigrant s){
        pop = pop + ((Immigrant) s).getNbr();
        popInArrival = popInArrival - ((Immigrant) s).getNbr();
        city.setPopInArrvial(city.getPopInArrvial() - ((Immigrant) s).getNbr());
        city.setPopulation(city.getPopulation() + ((Immigrant) s).getNbr());
    }
    public void removePopulation(int nbr){
        pop = pop - nbr;
        city.setPopulation(city.getPopulation() - nbr);
    }

    public void SpriteManager() {
        ArrayList<Sprite> toDestroy = new ArrayList<>();

        synchronized (msprites) {
            for (MovingSprite s : msprites) {
                if (s.hasArrived) {
                    toDestroy.add(s);
                    if (s instanceof Immigrant) {
                        newPopulation((Immigrant) s);
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
    public void addCompetitor(double competitor) {
        this.competitor = this.competitor + competitor;
        if (this.competitor < 0)
            this.competitor = 0;
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

    public double getAppeal() {
        return appeal;
    }

    public void addAppeal(double appeal) {
        this.appeal = this.appeal + appeal;
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

    public int getCultureLvl(){
        int lvl = 0;
        if(philosophia>0)
            lvl+=15;
        if(gymnasium>0)
            lvl+=20;
        if(drama>0)
            lvl+=25;
        if(competitor>0)
            lvl+=20;
        return lvl;
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
