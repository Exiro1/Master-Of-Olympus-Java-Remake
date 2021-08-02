package com.exiro.buildingList.delivery;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.constructionList.Road;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.BuildingSprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class Agora extends Building {


    ArrayList<Case> shops;
    ArrayList<Case> tempRoad;
    ArrayList<AgoraShop> room;


    public Agora(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public Agora() {
        super(false, ObjectType.AGORA, BuildingCategory.MARKET, 0, 2, 50, 10, 0, 0, 6, 2, null, false, GameManager.currentCity, 0);
        shops = new ArrayList<>();
        room = new ArrayList<>();
        room.add(AgoraShop.EMPTY);
        room.add(AgoraShop.EMPTY);
        room.add(AgoraShop.EMPTY);
    }


    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();

        ArrayList<Case> horu = new ArrayList<>();
        ArrayList<Case> verl = new ArrayList<>();
        ArrayList<Case> hord = new ArrayList<>();
        ArrayList<Case> verr = new ArrayList<>();

        horu.add(city.getMap().getCase(xPos, yPos));
        horu.add(city.getMap().getCase(xPos + 1, yPos));
        horu.add(city.getMap().getCase(xPos + 2, yPos));
        horu.add(city.getMap().getCase(xPos + 3, yPos));
        horu.add(city.getMap().getCase(xPos + 4, yPos));
        horu.add(city.getMap().getCase(xPos + 5, yPos));

        hord.add(city.getMap().getCase(xPos, yPos - 1));
        hord.add(city.getMap().getCase(xPos + 1, yPos - 1));
        hord.add(city.getMap().getCase(xPos + 2, yPos - 1));
        hord.add(city.getMap().getCase(xPos + 3, yPos - 1));
        hord.add(city.getMap().getCase(xPos + 4, yPos - 1));
        hord.add(city.getMap().getCase(xPos + 5, yPos - 1));

        verr.add(city.getMap().getCase(xPos, yPos - 1));
        verr.add(city.getMap().getCase(xPos, yPos));
        verr.add(city.getMap().getCase(xPos, yPos + 1));
        verr.add(city.getMap().getCase(xPos, yPos + 2));
        verr.add(city.getMap().getCase(xPos, yPos + 3));
        verr.add(city.getMap().getCase(xPos, yPos + 4));

        verl.add(city.getMap().getCase(xPos + 1, yPos - 1));
        verl.add(city.getMap().getCase(xPos + 1, yPos));
        verl.add(city.getMap().getCase(xPos + 1, yPos + 1));
        verl.add(city.getMap().getCase(xPos + 1, yPos + 2));
        verl.add(city.getMap().getCase(xPos + 1, yPos + 3));
        verl.add(city.getMap().getCase(xPos + 1, yPos + 4));

        boolean horbu = true;
        boolean horbd = true;
        boolean verbl = true;
        boolean verbr = true;

        for (Case c : horu) {
            if (!(c != null && c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD)) {
                horbu = false;
            }
        }
        for (Case c : hord) {
            if (!(c != null && c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD)) {
                horbd = false;
            }
        }
        for (Case c : verl) {
            if (!(c != null && c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD)) {
                verbl = false;
            }
        }
        for (Case c : verr) {
            if (!(c != null && c.getObject() != null && c.getObject().getBuildingType() == ObjectType.ROAD)) {
                verbr = false;
            }
        }

        if (horbu) {
            xLenght = 6;
            yLenght = 2;
            for (int i = 1; i < 3; i++) {
                for (int j = 0; j < 6; j++) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos - i);
                        if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos - i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos, yPos - 1));
                shops.add(city.getMap().getCase(xPos + 2, yPos - 1));
                shops.add(city.getMap().getCase(xPos + 4, yPos - 1));
                tempRoad = horu;
            }

        } else if (horbd) {
            xLenght = 6;
            yLenght = 2;
            for (int i = 0; i > -2; i--) {
                for (int j = 0; j < 6; j++) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos - i);
                        if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos - i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos, yPos + 1));
                shops.add(city.getMap().getCase(xPos + 2, yPos + 1));
                shops.add(city.getMap().getCase(xPos + 4, yPos + 1));
                tempRoad = hord;
            }
        } else if (verbr) {
            for (int i = -1; i < 5; i++) {
                for (int j = 1; j < 3; j++) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos + i);
                        if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos + i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos + 1, yPos + 0));
                shops.add(city.getMap().getCase(xPos + 1, yPos + 2));
                shops.add(city.getMap().getCase(xPos + 1, yPos + 4));
                tempRoad = verr;
            }
            xLenght = 2;
            yLenght = 6;
        } else if (verbl) {
            for (int i = -1; i < 5; i++) {
                for (int j = 0; j > -2; j--) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos + i);
                        if (!c.isOccuped() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos + i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos - 1, yPos + 0));
                shops.add(city.getMap().getCase(xPos - 1, yPos + 2));
                shops.add(city.getMap().getCase(xPos - 1, yPos + 4));
                tempRoad = verl;
            }
            xLenght = 2;
            yLenght = 6;
        }

        return place;
    }


    public boolean addShop(AgoraShop shop) {
        int i = 0;
        for (AgoraShop s : this.room) {
            if (s == AgoraShop.EMPTY && !room.contains(shop)) {
                room.set(i, shop);
                updateShop();
                return true;
            }
            i++;
        }
        return false;
    }

    public void updateShop() {
        for (int i = 0; i < 3; i++) {
            Case c = shops.get(i);
            TileImage t = ImageLoader.getImage("Zeus_General", 3, room.get(i).id);
            c.setImg(t.getImg());
            c.setHeight(t.getH());
            c.setWidth(t.getW());
        }
    }


    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            for (Case c : shops) {
                TileImage t = ImageLoader.getImage("Zeus_General", 3, 15);
                c.setImg(t.getImg());
                c.setHeight(t.getH());
                c.setWidth(t.getW());
            }
            for (Case c : tempRoad) {
                ((Road) c.getObject()).setAgora(this);
            }

            return true;
        }
        return false;
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {
        for (Case c : shops) {
            com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(c.getxPos(), (c.getyPos())), c.getWidth(), c.getHeight(), 2);
            g.drawImage(c.getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        }

        //render only buildingSprite because movingSprite are render separately
        for (BuildingSprite s : bsprites) {
            if (isActive() && getPop() > 0)
                s.Render(g, camX, camY);
        }

    }


    @Override
    public void processSprite(double delta) {

    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }
}
