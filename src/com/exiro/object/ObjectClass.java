package com.exiro.object;

import com.exiro.terrainList.Elevation;

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
        Active = isActive;
    }

    public abstract ArrayList<Case> getAccess();

    public abstract void delete();

    public abstract boolean build(int xPos, int yPos);

    public abstract ArrayList<Case> getPlace(int startX, int startY, int size, int size1, City currentCity);

    /**
     * Free, constructible cases of the footprint going right (+x) and up (-y) from (xPos, yPos).
     * The placement succeeds when the result holds every case of the footprint.
     *
     * @param allowElevation true for objects that can be placed on slopes (roads)
     */
    protected static ArrayList<Case> freeCases(City city, int xPos, int yPos, int yLenght, int xLenght, boolean allowElevation) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                Case c = city.getMap().getCase(xPos + j, yPos - i);
                if (c == null)
                    continue;
                if (!c.isOccupied() && c.getTerrain().isConstructible() && (allowElevation || !(c.getTerrain() instanceof Elevation)))
                    place.add(c);
            }
        }
        return place;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }

}