package com.exiro.sprite.delivery.carter;

import com.exiro.buildingList.Building;
import com.exiro.buildingList.IndustryConverter;
import com.exiro.buildingList.StoreBuilding;
import com.exiro.reader.TileImage;
import com.exiro.moveRelated.FreeState;
import com.exiro.moveRelated.Path;
import com.exiro.object.*;
import com.exiro.sprite.Direction;
import com.exiro.sprite.MovingSprite;

import java.util.Map;

public abstract class Carter extends MovingSprite{


    Resource resource;
    int amount;
    int currentDelivery = 0;
    int command = 0;
    Building origin,trueOrigin;
    CarterState carterState;



    public enum CarterState { WAITING, RETURNING_EMPTY,RETURNING_FULL, DELIVERING_STORE,DELIVERING_CONVERTER, COMMAND, DONE}


    public Carter(String filePath, int bitID, int localId, int frameNumber, City c, ObjectClass destination, Resource res, int amount, int command, int currentDelivery, Building origin) {
        super(filePath, bitID, localId, frameNumber, c, destination);
        this.resource = res;
        this.amount = amount;
        this.currentDelivery = currentDelivery;
        this.command = command;
        this.origin = origin;
        this.trueOrigin = origin;

    }

    public static Carter startDelivery(City c,Building from, Building to, Resource r, int amount,int currentDelivery){
        Carter carter;
        if (r == Resource.WOOD || r == Resource.MARBLE || r == Resource.SCULPTURE) {
            carter = new ComplexCarter(c,to,from.getAccess().get(0),r,amount,0,currentDelivery,from);
            carter.setImage(carter.getDir(), 0);
        }else{
            carter = new SimpleCarter(c,to,from,r,amount,0,currentDelivery);
        }
        if(to == null){
            carter.setCarterState(CarterState.WAITING);
        }else {
            carter.setRoutePath(c.getPathManager().getPathTo(from.getAccess().get(0), to.getAccess().get(0), FreeState.ALL_ROAD.getI()));
            if (to instanceof StoreBuilding)
                carter.carterState = CarterState.DELIVERING_STORE;
            if (to instanceof IndustryConverter)
                carter.carterState = CarterState.DELIVERING_CONVERTER;
        }
        return carter;
    }

    public static Carter startCommand(City c,Building from, Building to, Resource r, int command){
        Carter carter;
        if (r == Resource.WOOD || r == Resource.MARBLE || r == Resource.SCULPTURE) {
            carter = new ComplexCarter(c,to,from.getAccess().get(0),r,0,command,0,from);
            carter.setImage(carter.getDir(), 0);
        }else{
            carter = new SimpleCarter(c,to,from,r,0,command,0);
        }
        carter.setRoutePath(c.getPathManager().getPathTo(from.getAccess().get(0),to.getAccess().get(0),FreeState.ALL_ROAD.getI()));
        carter.carterState = CarterState.COMMAND;
        return carter;
    }

    @Override
    public void process(double deltaTime, int deltaDays) {
        super.process(deltaTime, deltaDays);

        if (hasArrived() && carterState != CarterState.WAITING) {
            Arrived();
        } else if (carterState == CarterState.WAITING) {
            searchForStoreBuilding();
        }

    }


    private void Arrived(){
        switch (carterState){
            case DELIVERING_CONVERTER:
                ((IndustryConverter) destination).delivered(getCurrentDelivery());
                setAmount(getAmount() - getCurrentDelivery());
                if (getAmount() > 0) {
                    carterState = CarterState.WAITING;
                    searchForStoreBuilding();
                }else {
                    returnToHomeEmpty();
                }
                break;
            case DELIVERING_STORE:
                StoreBuilding g = (StoreBuilding) destination;
                g.stock(resource, getCurrentDelivery());
                setAmount(getAmount() - getCurrentDelivery());
                if (getAmount() > 0) {
                    carterState = CarterState.WAITING;
                    searchForStoreBuilding();
                } else {
                   returnToHomeEmpty();
                }
                break;

            case RETURNING_EMPTY:
                carterState = CarterState.DONE;
                break;
            case RETURNING_FULL:
                if(trueOrigin instanceof IndustryConverter){
                    ((IndustryConverter) trueOrigin).addIncomming(amount - getCommand());
                    ((IndustryConverter) trueOrigin).delivered(amount);

                }else if(trueOrigin instanceof StoreBuilding){
                    ((StoreBuilding) trueOrigin).unReserved(resource, getCommand()-amount);
                    ((StoreBuilding) trueOrigin).stock(resource,amount);
                }
                carterState = CarterState.DONE;
                break;
            case COMMAND:
                g = (StoreBuilding)getDestination();
                amount = g.unstockWithReservation(resource, getCommand());
                returnToHomeFull();
                break;

        }


    }

    public void searchForStoreBuilding(){


        if(resource.getDelivery() != null){
            for (ObjectType type : resource.getDelivery()) {
                for (Building b : getC().getBuildingList(type)) {
                    if (b instanceof IndustryConverter) {
                        if (((IndustryConverter) b).roomForInputResources() > 0) {
                            Path p = getC().getPathManager().getPathTo(getXB(), getYB(), b.getAccess().get(0).getxPos(), b.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                            if (p != null) {
                                int delivery = Math.min(((IndustryConverter) b).roomForInputResources(), amount);
                                ((IndustryConverter) b).addIncomming(delivery);
                                setArrived(false);

                                currentDelivery = delivery;
                                setRoutePath(p);
                                setDestination(b);
                                carterState = CarterState.DELIVERING_CONVERTER;
                                return;
                            }
                        }
                    }
                }
            }
        }
        for (Building b : getC().getBuildings()) {
            if (b instanceof StoreBuilding g) {
                if (g.getFreeSpace(resource) > 0 && g.getAccess().size() > 0) {
                    Path p = getC().getPathManager().getPathTo(getXB(), getYB(), g.getAccess().get(0).getxPos(), g.getAccess().get(0).getyPos(), FreeState.ALL_ROAD.i);
                    if (p != null) {
                        setArrived(false);
                        setRoutePath(p);
                        setDestination(g);
                        currentDelivery = getAmount() - g.reserve(resource, getAmount());
                        carterState = CarterState.DELIVERING_STORE;
                        return;
                    }
                }
            }
        }

    }

    public abstract void setArrived(boolean hasarrived);

    public abstract boolean hasArrived();

    public void returnToHomeEmpty(){
        setArrived(false);
        setCommand(0);
        setAmount(0);
        setResource(Resource.NULL);
        setImage(dir,0);
        updateImg();
        setOrigin((Building) getDestination());
        setDestination(trueOrigin);
        setRoutePath(c.getPathManager().getPathTo(getOrigin().getAccess().get(0), ((ObjectClass) getDestination()).getAccess().get(0), FreeState.ALL_ROAD.getI()));
        carterState = CarterState.RETURNING_EMPTY;
    }

    public void returnToHomeFull(){
        setArrived(false);
        setImage(dir,0);
        updateImg();
        setOrigin((Building) getDestination());
        setDestination(trueOrigin);
        setRoutePath(c.getPathManager().getPathTo(getOrigin().getAccess().get(0), ((ObjectClass) getDestination()).getAccess().get(0), FreeState.ALL_ROAD.getI()));
        carterState = CarterState.RETURNING_FULL;
    }


    @Override
    public void delete() {
        super.delete();
        BaseObject obj = getDestination();
        if(obj instanceof StoreBuilding){
            ((StoreBuilding) obj).setCarterAvailable(true);
        }else if(obj instanceof IndustryConverter){
            ((IndustryConverter) obj).setCarterAvailable(true);
        }
        switch (carterState) {
            case WAITING:

                break;
            case RETURNING_EMPTY:

                break;
            case RETURNING_FULL:
                if(obj instanceof StoreBuilding){
                    ((StoreBuilding) trueOrigin).unReserved(getResource(),getCommand());
                }else if(obj instanceof IndustryConverter){
                    ((IndustryConverter) trueOrigin).addIncomming(-getCommand());
                }
                break;
            case DELIVERING_STORE:
                ((StoreBuilding) getDestination()).unReserved(getResource(),getCurrentDelivery());
                break;
            case DELIVERING_CONVERTER:
                ((IndustryConverter) trueOrigin).addIncomming(-getCurrentDelivery());
                break;
            case COMMAND:
                if(obj instanceof StoreBuilding){
                    ((StoreBuilding) getDestination()).unReserveUnstockage(getResource(),getCommand());
                }
                break;
            case DONE:
                break;
        }
    }

    @Override
    public Map<Direction, TileImage[]> getSpriteSet() {
        return null;
    }


    public CarterState getCarterState() {
        return carterState;
    }

    public void setCarterState(CarterState carterState) {
        this.carterState = carterState;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCurrentDelivery() {
        return currentDelivery;
    }

    public void setCurrentDelivery(int currentDelivery) {
        this.currentDelivery = currentDelivery;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public Building getOrigin() {
        return origin;
    }

    public void setOrigin(Building origin) {
        this.origin = origin;
    }
}
