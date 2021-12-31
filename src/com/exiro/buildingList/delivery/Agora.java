package com.exiro.buildingList.delivery;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.BuildingCategory;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.buildingList.delivery.agorashop.AgoraShopBuilding;
import com.exiro.buildingList.delivery.agorashop.EmptyShop;
import com.exiro.constructionList.Road;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.AgoraSupplierChief;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.delivery.AgoraDelivery;
import com.exiro.systemCore.GameManager;

import java.awt.*;
import java.util.ArrayList;

public class Agora extends Building {


    ArrayList<Case> shops;
    ArrayList<Case> roads;
    ArrayList<AgoraShopBuilding> shopsBuildings;
    AgoraDelivery delivery;
    float timeBeforeDelivery = 0.0f;

    ArrayList<AgoraSupplierChief> chiefs;


    public Agora(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
    }

    public Agora() {
        super(false, ObjectType.AGORA, BuildingCategory.MARKET, 0, 0, 40, 10, 0, 0, 6, 2, null, false, GameManager.currentCity, 0);
        shops = new ArrayList<>();
        shopsBuildings = new ArrayList<>();
        chiefs = new ArrayList<>();

    }

    public void initAgora() {
        for (int i = 0; i < 3; i++) {
            EmptyShop e = new EmptyShop();
            Case c = shops.get(i);
            e.build(c.getxPos(), c.getyPos());

            /*
            TileImage t = ImageLoader.getImage("Zeus_General", 3, 15);
            c.setImg(t.getImg());
            c.setHeight(t.getH());
            c.setWidth(t.getW());
            */
        }
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
                        if (!c.isOccupied() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos - i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos, yPos - 1));
                shops.add(city.getMap().getCase(xPos + 2, yPos - 1));
                shops.add(city.getMap().getCase(xPos + 4, yPos - 1));
                roads = horu;
            }

        } else if (horbd) {
            xLenght = 6;
            yLenght = 2;
            for (int i = 0; i > -2; i--) {
                for (int j = 0; j < 6; j++) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos - i);
                        if (!c.isOccupied() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos - i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos, yPos + 1));
                shops.add(city.getMap().getCase(xPos + 2, yPos + 1));
                shops.add(city.getMap().getCase(xPos + 4, yPos + 1));
                roads = hord;
            }
        } else if (verbr) {
            for (int i = -1; i < 5; i++) {
                for (int j = 1; j < 3; j++) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos + i);
                        if (!c.isOccupied() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos + i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos + 1, yPos + 0));
                shops.add(city.getMap().getCase(xPos + 1, yPos + 2));
                shops.add(city.getMap().getCase(xPos + 1, yPos + 4));
                roads = verr;
            }
            xLenght = 2;
            yLenght = 6;
        } else if (verbl) {
            for (int i = -1; i < 5; i++) {
                for (int j = 0; j > -2; j--) {
                    if (!(xPos + j < 0 || yPos - i < 0)) {
                        Case c = city.getMap().getCase(xPos + j, yPos + i);
                        if (!c.isOccupied() && c.getTerrain().isConstructible()) {
                            place.add(city.getMap().getCase(xPos + j, yPos + i));
                        }
                    }
                }
            }
            if (place.size() == 12) {
                shops.add(city.getMap().getCase(xPos - 1, yPos + 0));
                shops.add(city.getMap().getCase(xPos - 1, yPos + 2));
                shops.add(city.getMap().getCase(xPos - 1, yPos + 4));
                roads = verl;
            }
            xLenght = 2;
            yLenght = 6;
        }

        return place;
    }


    public boolean addShop(AgoraShopBuilding shop) {

        if (shop.getShop() == AgoraShop.EMPTY) {
            updateShop();
            return true;
        }

        boolean exist = false;
        for (AgoraShopBuilding s : this.shopsBuildings) {
            if (s.getShop() == shop.getShop()) {
                exist = true;
            }
        }
        if ((shopsBuildings.size() < 3 && !exist) || shopsBuildings.size() == 0) {
            shopsBuildings.add(shop);
            updateShop();
            shop.setMcase(shops.get(shopsBuildings.size() - 1));
            /*
            if(shops.get(shopsBuildings.size() - 1).getObject() instanceof EmptyShop) {
                city.removeObj(shops.get(shopsBuildings.size() - 1).getObject());
                city.removeBuilding((Building) shops.get(shopsBuildings.size() - 1).getObject());
            }
            */
            shop.setxPos(shops.get(shopsBuildings.size() - 1).getxPos());
            shop.setyPos(shops.get(shopsBuildings.size() - 1).getyPos());
            return true;
        }
        return false;
    }

    public void updateShop() {
        /*
        for (int i = 0; i < shopsBuildings.size(); i++) {

            Case c = shops.get(i);
            TileImage t = ImageLoader.getImage("Zeus_General", 3, shopsBuildings.get(i).getShop().id);
            c.setImg(t.getImg());
            c.setHeight(t.getH());
            c.setWidth(t.getW());
        }*/
    }


    @Override
    public boolean build(int xPos, int yPos) {
        if (super.build(xPos, yPos)) {
            for (Case c : shops) {
                TileImage t = ImageLoader.getImage("Zeus_General", 3, 15);
                c.setImg(t.getImg());
                c.setHeight(t.getH());
                c.setWidth(t.getW());
                c.setMainCase(true);
            }
            for (Case c : roads) {
                ((Road) c.getObject()).setAgora(this);
            }
            initAgora();
            return true;
        }
        return false;
    }

    @Override
    public void Render(Graphics g, int camX, int camY) {

    }


    public void distribute() {

    }


    @Override
    public void processSprite(double delta) {
        for (AgoraShopBuilding shop : shopsBuildings) {
            shop.processSprite(delta);
        }
        for (Sprite s : sprites) {
            if (isActive())
                s.process(delta);
        }
    }

    @Override
    public void populate(double deltaTime) {
        ArrayList<Sprite> toDestroy = new ArrayList<>();
        for (MovingSprite c : getMovingSprites()) {
            if (c.hasArrived) {
                if (c.getDestination() == this) {
                    toDestroy.add(c);
                } else {
                    Path p = city.getPathManager().getPathTo(c.getXB(), c.getYB(), this.getAccess().get(0).getxPos(), this.getAccess().get(0).getyPos(), FreeState.ACTIVE_ROAD.getI());
                    if (p != null) {
                        c.setRoutePath(p);
                        c.setDestination(this);
                        c.hasArrived = false;
                    }
                }
            }
        }
        for (Sprite s : toDestroy) {
            removeSprites(s);
        }
    }

    @Override
    protected void addPopulation() {

    }

    public void createResupply(StoreBuilding storeBuilding, ArrayList<Resource> needed, AgoraShopBuilding building) {
        chiefs.add(new AgoraSupplierChief(city, roads.get(0), storeBuilding, needed, building));
        addSprite(chiefs.get(chiefs.size() - 1));
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);
        if (isActive()) {

            for (AgoraShopBuilding shopBuilding : shopsBuildings) {
                if (shopBuilding.getPop() > 0) {
                    if (shopBuilding.needRefuel()) {
                        boolean br = false;
                        ArrayList<Resource> needed = shopBuilding.getResources();
                        for (Resource r : needed) {
                            for (Building b : city.getBuildings()) {
                                if (b instanceof StoreBuilding) {
                                    StoreBuilding g = (StoreBuilding) b;
                                    if (g.hasStockAvailable(r)) {
                                        shopBuilding.setBuying(true);
                                        createResupply(g, needed, shopBuilding);
                                        br = true;
                                        break;
                                    }
                                }
                            }
                            if (br)
                                break;
                        }
                    }
                }
            }
            boolean ready = false;
            for (AgoraShopBuilding b : shopsBuildings) {
                if (b.getReserve() > 0)
                    ready = true;
            }
            if (ready)
                startDelivery();


            ArrayList<AgoraSupplierChief> toR = new ArrayList<>();
            for (AgoraSupplierChief ch : chiefs) {
                if (ch.hasArrived) {
                    if (ch.getDestination() == this) {
                        removeSprites(ch);
                        toR.add(ch);
                        ch.getAType().refuel(ch.getResourcesGathered());
                        ch.getAType().setBuying(false);
                    } else {
                        StoreBuilding st = (StoreBuilding) ch.getDestination();
                        for (Resource r : ch.getNeeded()) {
                            if (st.getStockAvailable(r) > 0) {
                                int retr = st.unStock(r, Math.min(st.getStockAvailable(r), (int) (ch.getAType().getShop().getQuerry() - ch.getResourcesGathered()) / r.getFoodapprov()));
                                ch.setResourcesGathered(ch.getResourcesGathered() + retr * r.getFoodapprov());
                            }
                        }
                        ch.setRoutePath(city.getPathManager().getPathTo(city.getMap().getCase(ch.getXB(), ch.getYB()), this.roads.get(0), FreeState.ALL_ROAD.i));
                        ch.setDestination(this);
                        ch.hasArrived = false;

                    }
                }
            }
            chiefs.removeAll(toR);

        }
    }

    public void startDelivery() {
        if (delivery == null) {
            delivery = new AgoraDelivery(city, null, roads.get(0), 30, this);
            addSprite(delivery);
        } else if (delivery.hasArrived && delivery.getDestination() == this) {
            removeSprites(delivery);
            delivery = new AgoraDelivery(city, null, roads.get(0), 30, this);
            addSprite(delivery);
        }
    }

    public ArrayList<AgoraShopBuilding> getShopsBuildings() {
        return shopsBuildings;
    }
}
