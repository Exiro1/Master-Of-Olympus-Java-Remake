package com.exiro.Object;

import com.exiro.BuildingList.BuildingType;
import com.exiro.ConstructionList.Empty;

import java.util.ArrayList;

public class CityMap {

    private ArrayList<Case> cases;
    private int lenght;
    private int height, width;
    private Case startCase;
    private City city;


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
                Empty e = new Empty(city);
                e.setxPos(j);
                e.setYpos(i);
                cases.add(new Case(j, i, BuildingType.EMPTY, e));
                cases.get(cases.size() - 1).setMainCase(true);

                cases.get(cases.size() - 1).setImg(e.getImg());
                cases.get(cases.size() - 1).setHeight(e.getHeight());
                cases.get(cases.size() - 1).setWidth(e.getWidth());
                cases.get(cases.size() - 1).setSize(e.getSize());

            }
        }
        //getCase(startCase.getxPos(),startCase.getyPos()).setOccuped(true);
        //getCase(startCase.getxPos(),startCase.getyPos()).setBuildingType(BuildingType.ROAD);
        //getCase(startCase.getxPos(),startCase.getyPos()).setObject(new Road(city));
        // getCase(startCase.getxPos(),startCase.getyPos()).getObject().setActive(true);

        this.startCase = getCase(startCase.getxPos(), startCase.getyPos());
    }

    public Case getCase(int x, int y) {
        if (x >= 0 && y >= 0 && x < width && y < height)
            return cases.get(width * y + x);

        return null;
    }

    @Override
    public String toString() {
        String str = "";
        for (int i = 0; i < lenght; i++) {
            for (int j = 0; j < lenght; j++) {
                str = str + " " + cases.get(j + i * lenght).getBuildingType().ordinal();
            }
            str = str + "\n";
        }
        return str;

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



