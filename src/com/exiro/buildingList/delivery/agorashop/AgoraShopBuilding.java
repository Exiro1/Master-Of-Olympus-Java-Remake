package com.exiro.buildingList.delivery.agorashop;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.render.IsometricRender;
import com.exiro.sprite.BuildingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.systemCore.GameManager;
import com.exiro.utils.Point;

import java.awt.*;
import java.util.ArrayList;

public class AgoraShopBuilding extends Building {

    Agora agora;
    AgoraShop shop;
    int reserve, distribution;
    Case mcase;
    ArrayList<Resource> resourcesPossible;
    boolean buying = false;

    public AgoraShopBuilding(AgoraShop shop, ObjectType type) {
        super(false, type, BuildingCategory.FOOD, 0, 4, 16, 10, 0, 0, 2, 2, null, false, GameManager.currentCity, 0);
        this.shop = shop;
        reserve = distribution = 0;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {

        if (this instanceof EmptyShop) {
            if (city.getMap().getCase(xPos, yPos) != null && city.getMap().getCase(xPos, yPos).getObject() != null && city.getMap().getCase(xPos, yPos).getObject() instanceof Agora) {
                ArrayList<Case> c = new ArrayList<>();
                c.add(city.getMap().getCase(xPos, yPos));
                return c;
            }
        }

        if (city.getMap().getCase(xPos, yPos) != null && city.getMap().getCase(xPos, yPos).getObject() != null && city.getMap().getCase(xPos, yPos).getObject() instanceof EmptyShop) {
            ArrayList<Case> c = new ArrayList<>();
            c.add(city.getMap().getCase(xPos, yPos));
            c.add(city.getMap().getCase(xPos, yPos));
            c.add(city.getMap().getCase(xPos, yPos));
            c.add(city.getMap().getCase(xPos, yPos));
            return c;
        }
        return null;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> cases = getPlace(xPos, yPos, 0, 0, city);
        if (cases != null) {
            Agora agora = (Agora) cases.get(0).getObject();
            this.agora = agora;
            if (agora.addShop(this)) {
                BuildingSprite bs = new BuildingSprite("Zeus_General", 3, shop.getId() + 1, 1, city, this);
                addSprite(bs);
                setxPos(xPos);
                setyPos(yPos);
                setXB(getxPos());
                setYB(getyPos());
                city.addBuilding(this);
                city.addObj(this);


                for (Case c : cases) {
                    c.setOccuped(true);
                    c.setObject(this);
                    c.setMainCase(false);
                }
                city.getMap().getCase(xPos, yPos).setMainCase(true);
                this.setMainCase(city.getMap().getCase(getxPos(), getyPos()));

                return true;
            }
        }

        return false;
    }


    @Override
    public void Render(Graphics g, int camX, int camY) {

        com.exiro.utils.Point p = IsometricRender.TwoDToIsoTexture(new Point(getxPos(), (getyPos())), getWidth(), getHeight(), 2);
        g.drawImage(getImg(), camX + (int) p.getX(), camY + (int) p.getY(), null);
        if (reserve > 0) {
            bsprites.get(0).Render(g, camX, camY);
            if (isWorking()) {
                bsprites.get(1).Render(g, camX, camY);
            }
        }

        g.drawString(getPop() + "/" + getPopMax() + " " + reserve, camX + (int) p.getX() + 30, camY + (int) p.getY() + 30);
        /*
        if(getPop()>0 && isActive()){
            bsprites.get(1).Render(g,camX,camY);
        }
        */
    }

    public ArrayList<Resource> getResources() {
        return resourcesPossible;
    }

    public boolean needRefuel() {
        return (reserve < 100 && !buying);
    }

    public void refuel(int amount) {
        reserve += amount;
    }

    public void setBuying(boolean buying) {
        this.buying = buying;
    }

    @Override
    public void processSprite(double delta) {
        for (Sprite s : sprites) {
            if (isActive() && getPop() > 0)
                s.process(delta);
        }
    }

    @Override
    public void populate(double deltaTime) {

    }

    @Override
    protected void addPopulation() {

    }

    public void setMcase(Case mcase) {
        this.mcase = mcase;
        this.setxPos(mcase.getxPos());
        this.setyPos(mcase.getyPos());
        setXB(mcase.getxPos());
        setYB(mcase.getyPos());
    }

    public AgoraShop getShop() {
        return shop;
    }

    public void setShop(AgoraShop shop) {
        this.shop = shop;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public int getDistribution() {
        return distribution;
    }

    public void setDistribution(int distribution) {
        this.distribution = distribution;
    }

    public Agora getAgora() {
        return agora;
    }

    public void setAgora(Agora agora) {
        this.agora = agora;
    }
}
