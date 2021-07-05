package com.exiro.object;

import com.exiro.sprite.Direction;
import com.exiro.terrainList.Empty;
import com.exiro.terrainList.Water;
import com.exiro.terrainList.WaterCoast;

import java.util.ArrayList;

public class CityMap {

    private ArrayList<Case> cases;
    private int lenght;
    private int height, width;
    private Case startCase;
    private final City city;


    public CityMap(ArrayList<Case> cases, Case startCase, City city) {
        this.cases = cases;
        this.startCase = getCase(startCase.getxPos(), startCase.getyPos());
        this.city = city;
    }

    public CityMap(int height, int width, Case startCase, City city) {
        this.height = height;
        this.width = width;
        this.cases = new ArrayList<>();
        this.city = city;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Empty e = new Empty(1, city);
                e.setxPos(j);
                e.setyPos(i);
                cases.add(new Case(j, i, ObjectType.EMPTY, e));
                cases.get(cases.size() - 1).setMainCase(true);
                //TODO to remove
                /*
                cases.get(cases.size() - 1).setImg(e.getImg());
                cases.get(cases.size() - 1).setHeight(e.getHeight());
                cases.get(cases.size() - 1).setWidth(e.getWidth());
                cases.get(cases.size() - 1).setSize(e.getSize());
                */

            }
        }
        //getCase(startCase.getxPos(),startCase.getyPos()).setOccuped(true);
        //getCase(startCase.getxPos(),startCase.getyPos()).setBuildingType(BuildingType.ROAD);
        //getCase(startCase.getxPos(),startCase.getyPos()).setObject(new Road(city));
        // getCase(startCase.getxPos(),startCase.getyPos()).getObject().setActive(true);

        createSea(15, 15, 9, 7);


        this.startCase = getCase(startCase.getxPos(), startCase.getyPos());
    }

    public void createSea(int xs, int ys, int xlen, int ylen) {


        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);
                if (i == xs) {
                    WaterCoast w;
                    if (j == ys)
                        w = new WaterCoast(i, j, false, Direction.SUD, 0, city);
                    else if (j == ys + ylen - 1)
                        w = new WaterCoast(i, j, false, Direction.EST, 0, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.SUD_EST, 0, city);
                    c.setObject(w);
                } else if (i == xs + xlen - 1) {
                    WaterCoast w;

                    if (j == ys)
                        w = new WaterCoast(i, j, false, Direction.OUEST, 0, city);
                    else if (j == ys + ylen - 1)
                        w = new WaterCoast(i, j, false, Direction.NORD, 0, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.NORD_OUEST, 0, city);
                    c.setObject(w);
                } else if (j == ys) {
                    WaterCoast w;
                    if (i == xs + xlen - 1)
                        w = new WaterCoast(i, j, false, Direction.OUEST, 0, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.SUD_OUEST, 0, city);
                    c.setObject(w);
                } else if (j == ys + ylen - 1) {
                    WaterCoast w = new WaterCoast(i, j, false, Direction.NORD_EST, 0, city);
                    c.setObject(w);
                } else {
                    Water w = new Water(i, j, city);
                    c.setObject(w);
                }


            }
        }


    }


    public Case getCase(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height)
            return cases.get(width * y + x);

        return null;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < lenght; i++) {
            for (int j = 0; j < lenght; j++) {
                str.append(" ").append(cases.get(j + i * lenght).getBuildingType().ordinal());
            }
            str.append("\n");
        }
        return str.toString();

    }


    public Case getStartCase() {
        return startCase;
    }

    public void setStartCase(Case startCase) {
        this.startCase = startCase;
    }

    public ArrayList<Case> getCases() {
        return cases;
    }

    public void setCases(ArrayList<Case> cases) {
        this.cases = cases;
    }

    public int getLenght() {
        return lenght;
    }

    public void setLenght(int lenght) {
        this.lenght = lenght;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }
}



