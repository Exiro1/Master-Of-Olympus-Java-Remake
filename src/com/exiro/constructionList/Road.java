package com.exiro.constructionList;

import com.exiro.buildingList.delivery.Agora;
import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectType;
import com.exiro.systemCore.GameManager;
import com.exiro.terrainList.Elevation;

import java.util.ArrayList;
import java.util.Random;

public class Road extends Construction {


    Agora agora;

    boolean start = false;

    public Road(boolean isActive, ObjectType type, ArrayList<Case> cases, int cost, int deleteCost, int xPos, int yPos, int xLenght, int yLenght, float cachet, City city, boolean built, boolean isFloor) {
        super(isActive, type, cases, cost, deleteCost, xPos, yPos, xLenght, yLenght, cachet, city, built, isFloor);
    }

    public Road(City city) {
        super(false, ObjectType.ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, city, false, true);
    }

    public Road() {
        super(false, ObjectType.ROAD, new ArrayList<>(), 5, 1, 0, 0, 1, 1, 0f, GameManager.currentCity, false, true);
    }


    @Override
    public void process(double deltatime) {

    }


    public void setStart(boolean start) {
        this.start = start;
    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        ArrayList<Case> place = new ArrayList<>();
        for (int i = 0; i < yLenght; i++) {
            for (int j = 0; j < xLenght; j++) {
                if (!(xPos + j < 0 || yPos - i < 0)) {
                    Case c = city.getMap().getCase(xPos + j, yPos - i);
                    if(c==null)
                        continue;
                    if (!c.isOccupied() && c.getTerrain().isConstructible()) {
                        place.add(city.getMap().getCase(xPos + j, yPos - i));
                    }
                }
            }
        }
        return place;
    }

    @Override
    public boolean build(int xPos, int yPos) {
        boolean isBuilt = super.build(xPos, yPos);
        if (isBuilt) {//change en route bloqué
            if (getMainCase().getTerrain() instanceof Elevation) {
                ((Elevation) getMainCase().getTerrain()).setHasRoad(true);
            }
            city.getPathManager().addRoad(this);
            if (getAccess().size() == 0) {
                city.getInActives().add(this);
                setActive(false);
            } else {
                setActive(true);
            }
        } else {
            //setType(BuildingType.BLOCKABLE_ROAD);
        }
        return true;
    }

    public void updateRoadImg() {
        // d : WEST SOUTH EAST NORTH
        short d = 0b0000;
        int i = 0;
        for (Case c : getMainCase().getNeighbour()) {

            if (c != null && c.getObject() != null && (c.getObject() instanceof Road || c.getObject() instanceof BlockingRoad)) {
                d |= 1 << i;
            }
            i++;
        }
        int index = 0;
        if (d == 0b0000) {
            index = 246;
        } else if (d == 0b0001) {
            index = 242;
        } else if (d == 0b0010) {
            index = 243;
        } else if (d == 0b0011) {
            index = 238;
        } else if (d == 0b0100) {
            index = 244;
        } else if (d == 0b0101) {
            index = 234;
        } else if (d == 0b0110) {
            index = 239;
        } else if (d == 0b0111) {
            index = 247;
        } else if (d == 0b1000) {
            index = 245;
        } else if (d == 0b1001) {
            index = 241;
        } else if (d == 0b1010) {
            index = 235;
        } else if (d == 0b1011) {
            index = 250;
        } else if (d == 0b1100) {
            index = 240;
        } else if (d == 0b1101) {
            index = 249;
        } else if (d == 0b1110) {
            index = 248;
        } else if (d == 0b1111) {
            index = 251;
        }
        setPath("Zeus_Terrain");
        setBitmapID(3);
        setLocalID(index);
        this.updateImg();
    }


    @Override
    public void delete() {
        if (!this.start && agora == null) {
            super.delete();
            city.getPathManager().deleteRoad(this);
        }
    }

    public void setBlockableRoad() {
        setType(ObjectType.ROAD);
    }

    public Agora getAgora() {
        return agora;
    }

    public void setAgora(Agora agora) {
        this.agora = agora;
        Random random = new Random();
        TileImage img = ImageLoader.getImage("Zeus_General", 3, 12 + random.nextInt(3));
        assert img != null;
        setImg(img);
    }
}




