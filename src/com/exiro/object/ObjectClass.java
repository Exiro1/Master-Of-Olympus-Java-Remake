package com.exiro.object;

import java.util.ArrayList;

public abstract class ObjectClass extends BaseObject implements Cloneable {

    private boolean Active = false;
    public boolean canBuild;

    public ObjectClass(ObjectType type, String filename, int size, int bitmapID, int localID, int xlen, int ylen, boolean active, boolean canBuild) {
        super(type, filename, size, bitmapID, localID, xlen, ylen);
        Active = active;
        this.canBuild = canBuild;
    }

    public ObjectClass(boolean isActive, ObjectType type, int xLenght, int yLenght) {
        super(type, type.getPath(), type.getSize(), type.getBitmapID(), type.getLocalID(), xLenght, yLenght);
    }

    public abstract ArrayList<Case> getAccess();

    public abstract void delete();

    public abstract boolean build(int xPos, int yPos);

    public abstract ArrayList<Case> getPlace(int startX, int startY, int size, int size1, City currentCity);

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

}