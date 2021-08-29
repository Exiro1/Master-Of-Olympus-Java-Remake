package com.exiro.buildingList;

import com.exiro.constructionList.Rubble;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.MapObject;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public abstract class Building extends MapObject {

    final boolean DEBUG = false;

    final ObjectType type;
    protected final ArrayList<BuildingSprite> bsprites = new ArrayList<>();
    final ArrayList<MovingSprite> msprites = new ArrayList<>();
    protected final City city;
    BuildingCategory category;

    float safetyLvl = 200;

    int pop;
    int popMax;
    final int cost;
    final int deleteCost;
    final int yLenght;
    final int xLenght;
    protected ArrayList<Sprite> sprites = new ArrayList<>();
    private final int ID;
    int xPos;
    int yPos;
    protected ArrayList<Case> cases;
    boolean built;
    int xpos2;
    int ypos2;
    boolean deleted = false;


    public Building(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, xLenght, yLenght);
        if (!isActive)
            city.getInActives().add(this);
        this.category = category;
        this.pop = pop;
        this.popMax = popMax;
        this.cost = cost;
        this.deleteCost = deleteCost;
        this.xPos = xPos;
        this.yPos = yPos;
        this.yLenght = yLenght;
        this.xLenght = xLenght;
        this.cases = cases;
        this.built = built;
        this.city = city;
        this.ID = ID;
        this.type = type;
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = new BuildingInterface(300, 300, 500, 400, null, this);
        bi.addText(type.getName(), "Zeus.ttf", 32f, 250 - 32 * type.getName().length() / 2 + 16, 50);
        return bi;
    }

    /**
     * Appelé a chaque image
     */
    abstract public void processSprite(double delta);

    /**
     * Appelés toute les secondes
     */
    public void process(double deltaTime) {
        addSafetyLvl((float) -deltaTime);
        if (getSafetyLvl() < 0) {
            destroy();
        }
    }

    /**
     * Remplis si possible le batiment
     * Appelés a chaque image par GameThread
     */
    abstract public void populate(double deltaTime);

    /**
     * Appelé a chaque image , gere l'ajout de personne dans le batiment
     */
    protected abstract void addPopulation();

    /**
     * Construit le Batiment
     *
     * @param xPos Coordonnée en X
     * @param yPos Coordonnée en Y
     */
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> place;
        place = getPlace(xPos, yPos, yLenght, xLenght, city);

        if (place.size() == xLenght * yLenght) {
            this.xPos = xPos;
            this.yPos = yPos;
            setXB(xPos);
            setYB(yPos);

            this.built = true;
            this.xpos2 = xPos + xLenght;
            this.ypos2 = yPos + yLenght;

            city.getOwner().pay(this.cost);
            city.addBuilding(this);
            city.addObj(this);
            for (Case c : place) {
                c.setOccuped(true);
                c.setObject(this);
                c.setMainCase(false);
            }
            cases = place;
            city.getMap().getCase(xPos, yPos).setMainCase(true);
            this.setMainCase(city.getMap().getCase(xPos, yPos));
            /*
            city.getMap().getCase(xPos, yPos).setImg(getImg());
            city.getMap().getCase(xPos, yPos).setHeight(getHeight());
            city.getMap().getCase(xPos, yPos).setWidth(getWidth());
            city.getMap().getCase(xPos, yPos).setSize(getSize());
            */
            if (getAccess().size() > 0) {
                setActive(true);
                city.getInActives().remove(this);
            } else {
                setActive(false);
                city.getInActives().add(this);
            }

            return true;
        } else {
            return false;
        }
    }

    public void destroy() {
        this.delete();
        if (this.built) {
            for (Case c : getCases()) {
                c.setObject(new Rubble());
                c.getObject().build(c.getxPos(), c.getyPos());
                c.setMainCase(true);
            }
        }
    }

    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    Case c = city.getMap().getCase(xPos + j, yPos - i);
                    if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    /**
     * Permet de savoir par ou les Sprite sortes/arrive
     *
     * @return toute les routes actives adjacent
     */
    public ArrayList<Case> getAccess() {
        ArrayList<Case> access = new ArrayList<>();
        for (int i = 0; i < xLenght + 2; i++) {
            for (int j = 0; j < yLenght + 2; j++) {
                if (!((i == 0 && j == 0) || (i == 0 && j == yLenght + 1) || (i == xLenght + 1 && j == 0) || (i == xLenght + 1 && j == yLenght + 1))) {
                    Case c = city.getMap().getCase(xPos + i - 1, yPos - j + 1);

                    if (c != null && c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD && c.getObject().isActive()) {
                        access.add(c);
                    }
                }
            }
        }
        return access;
    }

    /**
     * Detruit le batiment et l objet
     */
    public void delete() {

        if (this.built) {
            setActive(false);
            city.getOwner().pay(this.getDeleteCost());
            city.removeBuilding(this);
            for (Case c : getCases()) {
                c.setOccuped(false);
                c.setObject(null);
                c.setMainCase(true);
            }
            for (Sprite s : getSprites()) {
                s.delete();
            }
            clearMovingSprite();
            clearBuildingSprite();
            city.removeObj(this);
            deleted = true;
        }
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        int lvl = getMainCase().getZlvl();

        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos() - lvl, getyPos() - lvl), getWidth(), getHeight(), getSize());
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);

        if (DEBUG) {
            g.drawString(getPop() + "/" + getPopMax(), camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);
            g.setColor(Color.RED);
            g.drawRect(camX + (int) p.getX(), camY + (int) p.getY(), getWidth(), getHeight());
            g.setColor(Color.BLACK);
        }

        //render only buildingSprite because movingSprite are render separately
        for (BuildingSprite s : bsprites) {
            if (isActive() && getPop() > 0) {
                s.Render(g, camX, camY);
            } else if (msprites.size() > 0) {
                clearMovingSprite();
            }
        }

    }

    public boolean isWorking() {
        return isActive() && getPop() > 0;
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
                '}';
    }

    public void setPopMax(int popMax) {
        this.popMax = popMax;
    }

    public float getSafetyLvl() {
        return safetyLvl;
    }

    public void setSafetyLvl(float safetyLvl) {
        this.safetyLvl = safetyLvl;
    }

    public void addSafetyLvl(float safetyLvl) {
        this.safetyLvl += safetyLvl;
    }

    public void setxPos(int xPos) {
        this.xPos = xPos;
    }

    public void setyPos(int yPos) {
        this.yPos = yPos;
    }

    public City getCity() {
        return city;
    }

    public ArrayList<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(ArrayList<Sprite> sprites) {
        this.sprites = sprites;
    }

    public ArrayList<MovingSprite> getMovingSprites() {
        return msprites;
    }

    public ArrayList<BuildingSprite> getBuildingSprites() {
        return bsprites;
    }

    public void addSprite(Sprite s) {
        sprites.add(s);
        if (s instanceof MovingSprite)
            msprites.add((MovingSprite) s);
        if (s instanceof BuildingSprite)
            bsprites.add((BuildingSprite) s);
    }

    public void clearBuildingSprite() {
        ArrayList<Sprite> toRemove = new ArrayList<>(bsprites);
        for (Sprite s : toRemove) {
            removeSprites(s);
        }
    }

    public void clearMovingSprite() {
        ArrayList<Sprite> toRemove = new ArrayList<>(msprites);
        for (Sprite s : toRemove) {
            removeSprites(s);
        }
    }

    public void removeSprites(Sprite s) {
        synchronized (sprites) {
            s.delete();
            sprites.remove(s);
        }

        if (s instanceof MovingSprite)
            synchronized (msprites) {
                msprites.remove((MovingSprite) s);
            }
        if (s instanceof BuildingSprite)
            synchronized (bsprites) {
                bsprites.remove((BuildingSprite) s);
            }
    }

    @Override
    public void setActive(boolean active) {
        super.setActive(active);
        city.setActiveBuilding(city.getActiveBuilding() + (active ? 1 : -1));
    }

    public boolean isDeleted() {
        return deleted;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
    }

    public int getPopMax() {
        return popMax;
    }

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }

    public int getCost() {
        return cost;
    }

    public int getDeleteCost() {
        return deleteCost;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public BuildingCategory getCategory() {
        return category;
    }

    public void setCategory(BuildingCategory category) {
        this.category = category;
    }


}
