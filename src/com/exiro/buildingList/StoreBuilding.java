package com.exiro.buildingList;

import com.exiro.ai.AI;
import com.exiro.fileManager.SoundLoader;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.*;
import com.exiro.render.ButtonType;
import com.exiro.render.interfaceList.BuildingInterface;
import com.exiro.render.interfaceList.Interface;
import com.exiro.sprite.delivery.carter.Carter;
import com.exiro.sprite.delivery.carter.SimpleCarter;
import com.exiro.sprite.MovingSprite;
import com.exiro.sprite.Sprite;
import com.exiro.sprite.delivery.carter.ComplexCarter;
import com.exiro.sprite.delivery.carter.TrolleyDriver;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class StoreBuilding extends Building {


    protected HashMap<Resource, Orders> orders;
    protected HashMap<Resource, Integer> stockage;
    protected HashMap<Resource, Integer> maxAllowed;
    int emptyCase = 8;
    HashMap<Resource, Integer> reserved;
    HashMap<Resource, Integer> reservedUnstock;
    int emptyCaseReserved = 8;
    boolean carterAvailable = true;
    ArrayList<Resource> ressourceIndexed;
    Interface currInterface;

    public StoreBuilding(boolean isActive, ObjectType type, BuildingCategory category, int pop, int popMax, int cost, int deleteCost, int xPos, int yPos, int yLenght, int xLenght, ArrayList<Case> cases, boolean built, City city, int ID) {
        super(isActive, type, category, pop, popMax, cost, deleteCost, xPos, yPos, yLenght, xLenght, cases, built, city, ID);
        stockage = new HashMap<>();
        reserved = new HashMap<>();
        reservedUnstock = new HashMap<>();
        maxAllowed = new HashMap<>();
        ressourceIndexed = new ArrayList<>();
        orders = new HashMap<>();
        for (Resource r : Resource.values()) {
            if (canStock(r) && r != Resource.NULL) {
                maxAllowed.put(r, 32 / r.getWeight());
                stockage.put(r, 0);
                reserved.put(r, 0);
                reservedUnstock.put(r, 0);
                ressourceIndexed.add(r);
                orders.put(r, Orders.ACCEPT);
            }
        }
    }

    @Override
    public boolean build(int xPos, int yPos) {
        return super.build(xPos, yPos);
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        manageCarter();
    }

    public void manageCarter() {

        if (carterAvailable) {
            for (Resource r : ressourceIndexed) {
                if (!carterAvailable)
                    break;
                if (orders.get(r) == Orders.EMPTY && hasStockAvailable(r)) {
                    emptyResource(r);
                } else if (orders.get(r) == Orders.OBTAIN && getFreeSpace(r) > 0) {
                    obtainResource(r);
                }
            }
        }

        ArrayList<Sprite> toDestroy = new ArrayList<>();

        for (MovingSprite c : msprites) {
            if(c instanceof Carter){
                if(((Carter) c).getCarterState() == Carter.CarterState.DONE){
                    toDestroy.add(c);
                    carterAvailable = true;
                }
            }
        }

        for (Sprite s : toDestroy) {
            removeSprites(s);
        }

    }



    public void emptyResource(Resource r) {
        for (Building b : city.getBuildings()) {
            if (b instanceof StoreBuilding g) {
                if (g.getFreeSpace(r) > 0 && g.getAccess().size() > 0) {
                    Path p = city.getPathManager().getPathTo(getAccess().get(0).getxPos(), getAccess().get(0).getyPos(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                    if (p != null) {
                        int amount = Math.min(Math.min(getStockAvailable(r), r.getMaxPerCart()), g.getFreeSpace(r));
                        amount = unStock(r, amount);
                        carterAvailable = false;
                        g.reserve(r,amount);
                        addSprite(Carter.startDelivery(city,this,g,r,amount,amount));
                        return;
                    }
                }
            }
        }
    }

    public void obtainResource(Resource r) {
        ArrayList<Building> stores = new ArrayList<>(city.getBuildingList(ObjectType.GRANARY));
        stores.addAll(city.getBuildingList(ObjectType.STOCK));
        for (Building b : stores) {
            if (!(b instanceof StoreBuilding sb))
                continue;
            if (sb.hasStockAvailable(r)) {
                int command = Math.min(Math.min(getFreeSpace(r), sb.getStockAvailable(r)), r.getMaxPerCart());
                command = sb.reserveUnstockage(r, command);
                reserve(r, command);
                addSprite(Carter.startCommand(city,this,sb,r,command));
                carterAvailable = false;
                return;
            }
        }
    }

    public boolean canStock(Resource r) {
        return (!orders.containsKey(r) || (orders.get(r) == Orders.OBTAIN || orders.get(r) == Orders.ACCEPT));
    }

    /**
     * reserve some resource to be retrieved
     *
     * @param r      resource to retrieve
     * @param amount amount to retrieve
     */
    public int reserveUnstockage(Resource r, int amount) {
        int unstockReserveAmount = Math.min(getStockAvailable(r), amount);
        if (reservedUnstock.containsKey(r)) {
            reservedUnstock.replace(r, reservedUnstock.get(r) + unstockReserveAmount);
        } else {
            reservedUnstock.putIfAbsent(r, unstockReserveAmount);
        }
        return unstockReserveAmount;
    }

    /**
     * unreserve some resource
     *
     * @param r      resource to unreserve
     * @param amount amount to unreserve
     */
    public void unReserveUnstockage(Resource r, int amount) {
        if (reservedUnstock.containsKey(r)) {
            reservedUnstock.replace(r, reservedUnstock.get(r) - amount);
        }
    }

    /**
     * get free space for a resource type
     *
     * @param r Resource
     * @return free space
     */
    public int getFreeSpace(Resource r) {
        int space = 0;
        if (!canStock(r))
            return 0;
        reserved.putIfAbsent(r, 0);
        int used = reserved.get(r);
        space = (emptyCaseReserved * 4) / r.getWeight() + (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight(); //surement simplifiable mais j ai un qi d'huitre aujourd'hui

        return Math.min(space, maxAllowed.get(r) - used);
    }

    /**
     * reserve space for resources
     *
     * @param r      resource
     * @param amount amount of resource to stock
     * @return amount of resource that cannot be stored
     */
    public int reserve(Resource r, int amount) {
        if (!canStock(r))
            return 0;
        if (getFreeSpace(r) < amount) {
            int result = amount - getFreeSpace(r);
            reserve(r, getFreeSpace(r));
            return result;
        }
        reserved.putIfAbsent(r, 0);
        int used = reserved.get(r);
        int spaceLeftInCase = (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight();
        if (spaceLeftInCase > 0) {
            reserved.replace(r, reserved.get(r) + Math.min(amount, spaceLeftInCase));
            amount -= Math.min(amount, spaceLeftInCase);
        }
        if (amount > 0) {
            int reservePossible = emptyCaseReserved >= (amount * r.getWeight()) / 4.0 ? (int) Math.ceil((amount * r.getWeight()) / 4.0) : emptyCaseReserved;
            reserved.replace(r, reserved.get(r) + (amount * r.getWeight() <= emptyCaseReserved * 4 ? amount : emptyCaseReserved * (4 / r.getWeight())));
            amount -= (amount * r.getWeight() <= emptyCaseReserved * 4 ? amount : emptyCaseReserved * (4 / r.getWeight()));
            emptyCaseReserved -= reservePossible;
        }
        return amount;
    }

    /**
     * Cancel a space reservation
     *
     * @param r      Ressource
     * @param amount amount
     */
    public void unReserved(Resource r, int amount) {
        reserved.putIfAbsent(r, 0);
        int before = reserved.get(r);
        int after = reserved.get(r) - amount;
        int stockNeededBefore = ((int) Math.ceil((before * r.getWeight() / 4.0)));
        int stockNeededAfter = ((int) Math.ceil((after * r.getWeight() / 4.0)));
        emptyCaseReserved += stockNeededBefore - stockNeededAfter;
        if (reserved.get(r) >= amount) {
            reserved.replace(r, reserved.get(r) - amount);
        } else {
            reserved.replace(r, 0); //no supposed to happen
        }
    }

    /**
     * reserve + store
     *
     * @param amount   amount to store
     * @param resource ressource to store
     * @return amount of ressource that cannot be stored
     */
    public int instantStock(int amount, Resource resource) {
        int avail = reserve(resource, amount);
        stock(resource, avail);
        return amount - avail;
    }

    /**
     * put resources in the stock
     *
     * @param amount
     * @param r
     * @return success
     */
    public boolean stock(Resource r, int amount) {
        //check that the amount specified has been reserved
        stockage.putIfAbsent(r, 0);
        if (reserved.get(r) >= stockage.get(r) + amount) {
            int temp = amount;
            int used = stockage.get(r);
            int spaceLeftInCase = (int) Math.ceil((used * r.getWeight()) / 4.0) * 4 - used * r.getWeight();
            if (spaceLeftInCase > 0) {
                temp -= Math.min(temp, spaceLeftInCase);
            }
            if (temp > 0) {
                int reservePossible = emptyCaseReserved >= (temp * r.getWeight()) / 4.0 ? (int) Math.ceil((temp * r.getWeight()) / 4.0) : emptyCase;
                emptyCase -= reservePossible;
            }
            stockage.replace(r, stockage.get(r) + amount);
        }else{
            return false;
        }
        updateStock();
        return true;
    }

    public boolean hasStockAvailable(Resource resource) {
        return stockage.containsKey(resource) && stockage.get(resource) - reservedUnstock.getOrDefault(resource, 0) > 0;
    }

    public boolean hasStockAvailable(Resource resource, boolean order) {
        if (orders.get(resource) == Orders.OBTAIN)
            return false;
        return stockage.containsKey(resource) && stockage.get(resource) - reservedUnstock.getOrDefault(resource, 0) > 0;
    }

    public int getStockAvailable(Resource resource) {
        if (stockage.containsKey(resource) && stockage.get(resource) - reservedUnstock.getOrDefault(resource, 0) > 0) {
            return stockage.get(resource) - reservedUnstock.getOrDefault(resource, 0);
        } else {
            return 0;
        }
    }

    /**
     * get ressources from the stock
     *
     * @param amount amount of ressources to retrieve
     * @param r      Ressource
     * @return amount of ressources retrieved
     */
    public int unStock(Resource r, int amount) {
        if (!stockage.containsKey(r))
            return 0;

        int ret = 0;
        int before = stockage.get(r);
        int after = stockage.get(r) - amount;
        int stockNeededBefore = ((int) Math.ceil((before * r.getWeight() / 4.0)));
        int stockNeededAfter = ((int) Math.ceil((after * r.getWeight() / 4.0)));

        int beforer = reserved.get(r);
        int afterr = reserved.get(r) - amount;
        int stockNeededBeforer = ((int) Math.ceil((beforer * r.getWeight() / 4.0)));
        int stockNeededAfterr = ((int) Math.ceil((afterr * r.getWeight() / 4.0)));

        emptyCase += stockNeededBefore - stockNeededAfter;
        emptyCaseReserved += stockNeededBeforer - stockNeededAfterr;

        if (stockage.get(r) - reservedUnstock.getOrDefault(r, 0) >= amount) {
            stockage.replace(r, stockage.get(r) - amount);
            reserved.replace(r, reserved.get(r) - amount);
            ret = amount;
        } else {
            ret = stockage.get(r) - reservedUnstock.getOrDefault(r, 0);
            stockage.replace(r, 0); //not supposed to happen
            reserved.replace(r, 0);
        }
        updateStock();
        return ret;
    }

    public int unstockWithReservation(Resource r, int amount) {
        unReserveUnstockage(r, amount);
        return unStock(r, amount);
    }

    public int getTotalStocked() {
        int tot = 0;
        for (Resource r : stockage.keySet()) {
            tot += r.getWeight() * stockage.get(r);
        }
        return tot;
    }

    public abstract void updateStock();

    public void changeOrder(Resource r, Orders o) {
        orders.replace(r, o);
    }

    @Override
    public Interface getInterface() {
        BuildingInterface bi = (BuildingInterface) super.getInterface();
        int i = 122;
        int ID = 0;
        bi.addLine(0, 70, bi.getW());

        bi.addLine(0, 87, bi.getW());
        bi.addText("STOCK", "Zeus.ttf", 10f, 20, 101);
        bi.addText("ORDRES", "Zeus.ttf", 10f, 310, 101);
        bi.addText("LIMITE DE STOCKAGE", "Zeus.ttf", 10f, 450, 101);
        bi.addLine(0, 104, bi.getW());
        for (Resource r : ressourceIndexed) {
            bi.addStockItem(stockage.get(r), r.getName(), orders.get(r), maxAllowed.get(r), i, "Zeus.ttf", ID);
            ID++;
            i += 20;
        }
        currInterface = bi;
        return bi;
    }


    @Override
    public void buttonClickedEvent(ButtonType type, int ID) {
        super.buttonClickedEvent(type, ID);
        if (type == ButtonType.INTERFACE_UP) {
            maxAllowed.replace(ressourceIndexed.get(ID), Math.min(maxAllowed.get(ressourceIndexed.get(ID)) + 4 / ressourceIndexed.get(ID).getWeight(), 32 / ressourceIndexed.get(ID).getWeight()));
            currInterface.requestUpdate();
        } else if (type == ButtonType.INTERFACE_DOWN) {
            maxAllowed.replace(ressourceIndexed.get(ID), Math.max(maxAllowed.get(ressourceIndexed.get(ID)) - 4 / ressourceIndexed.get(ID).getWeight(), 0));
            currInterface.requestUpdate();
        } else if (type == ButtonType.INTERFACE_ORDER) {
            orders.replace(ressourceIndexed.get(ID), orders.get(ressourceIndexed.get(ID)).next());
            currInterface.requestUpdate();
        }
    }

    public boolean isCarterAvailable() {
        return carterAvailable;
    }

    public void setCarterAvailable(boolean carterAvailable) {
        this.carterAvailable = carterAvailable;
    }

    @Override
    public SoundLoader.SoundCategory getSoundCategory() {
        return SoundLoader.SoundCategory.STORAGE;
    }
}
