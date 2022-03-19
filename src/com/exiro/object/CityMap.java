package com.exiro.object;

import com.exiro.constructionList.Tree;
import com.exiro.fileManager.CaseInfo;
import com.exiro.fileManager.MapSettings;
import com.exiro.fileManager.MapUtils;
import com.exiro.sprite.Direction;
import com.exiro.sprite.animals.Fish;
import com.exiro.terrainGenerator.CoastSelector;
import com.exiro.terrainGenerator.CoastType;
import com.exiro.terrainGenerator.ElevationType;
import com.exiro.terrainList.*;

import java.util.ArrayList;
import java.util.Random;

public class CityMap {

    private ArrayList<Case> cases;
    private ArrayList<Case> notNullCases;

    private Case[] caseSorted;
    private int lenght;
    private int height, width;
    private Case startCase;
    private final City city;
    int[][] map;
    int size;


    private ArrayList<Case> coppers;
    private ArrayList<Case> silvers;
    private ArrayList<Case> trees;
    private ArrayList<Case> meadows;
    private Case endCase;


    public CityMap(ArrayList<Case> cases, Case startCase, City city) {
        this.cases = cases;
        this.startCase = getCase(startCase.getxPos(), startCase.getyPos());
        this.city = city;
    }


    public CityMap(MapSettings mapSettings, City city) {
        this.height = mapSettings.getSize();
        this.width = mapSettings.getSize();
        this.cases = new ArrayList<>();
        this.notNullCases = new ArrayList<>();
        this.coppers = new ArrayList<>();
        this.silvers = new ArrayList<>();
        this.trees = new ArrayList<>();
        this.meadows = new ArrayList<>();
        this.city = city;

        createMap(mapSettings,-1);

        for (Case c : this.cases) {
            if(c != null)
                c.initNeighbour(this);
        }

    }

    public Terrain getTerrain(int value,int x, int y,City city,Random r){
        Terrain t = null;

        if(CaseInfo.compareTerrain(value,CaseInfo.LVL0)){
            t = new Empty(r.nextInt(10)+105,city);
            t.setxPos(x);
            t.setyPos(y);
            if(CaseInfo.compareEnv(value,CaseInfo.MEADOW)){
                t = new Meadow(x,y,city,0);
            }
        }else if(CaseInfo.compareTerrain(value,CaseInfo.WATER)){
            t = new Water(x,y,city);
        }else{
            t = new Elevation(x,y, ElevationType.NONE,city,1,false,0);
        }
        return t;
    }

    public void addEnvironment(){
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if(i<size/2){
                    if(size/2+i < j || size/2-i > j){
                        continue;
                    }
                }else{
                    if(size+size/2-i < j || -size/2+i > j){
                        continue;
                    }
                }

                int value = map[i][j];
                if(CaseInfo.compareEnv(value,CaseInfo.FISH)){
                    if(CaseInfo.compareTerrain(value,CaseInfo.WATER)) {
                        Fish f = new Fish(city, getCase(j, i));
                        city.getResourceManager().addFish(f);
                    }else{
                        //stairs
                    }
                }
                if(CaseInfo.compareEnv(value,CaseInfo.STARTENDCASE)){
                        if(startCase == null) {
                            this.startCase = getCase(j, i);
                            this.startCase = getCase(160, 55);   //TODO faire le systeme de start case / end case
                        }else{
                            this.endCase = getCase(j, i);
                            this.endCase = getCase(140, 68);
                        }
                }
                if(CaseInfo.compareEnv(value,CaseInfo.FOREST) && getCase(j,i) != null && getCase(j,i).getTerrain().isConstructible() ){
                    Case c = getCase(j,i);

                    if(c.getTerrain() instanceof Empty){
                        c.getTerrain().setLocalID(3 + r.nextInt(2));
                        c.getTerrain().updateImg();
                    }

                    int type = r.nextInt(3);
                    if(type==1) {
                        int nbr = r.nextInt(96);
                        Tree tree = new Tree(j, i, nbr, city, 0, c);
                        c.setObject(tree);
                        trees.add(c);
                    } else if (type == 2) {
                        int nbr = r.nextInt(3);
                        Tree tree = new Tree(j, i, nbr + 33, city, 0, c);
                        c.setObject(tree);
                        trees.add(c);
                    }
                }
            }
        }
        createMinerals(149, 105, 4, 4);

    }




    public int[][] getMap() {
        return map;
    }


    public void initCase(int i, int j,Random r){
        Terrain t;
        t = getTerrain(map[i][j],j,i,city,r);
        cases.add(new Case(j, i, null, t));
        if(CaseInfo.compareTerrain(map[i][j], CaseInfo.LVL2)){
            cases.get(cases.size() - 1).setZlvl(1);
        }
        if(CaseInfo.compareTerrain(map[i][j], CaseInfo.LVL3)){
            cases.get(cases.size() - 1).setZlvl(2);
        }
        cases.get(cases.size() - 1).setMainCase(true);
        cases.get(cases.size() - 1).setTerrain(t);
        t.setMainCase(cases.get(cases.size() - 1));
        notNullCases.add(cases.get(cases.size() - 1));
    }

    public void createMap(MapSettings settings, int seed){

        if(seed==-1){
            seed = settings.getSeed();
        }

        size = (int) (2f*settings.getSize()/Math.sqrt(2));
        int demisize = (int) (size/2f);

        map = MapUtils.createMap(seed, settings);
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {

                if(i<demisize){
                    if(demisize+i < j || demisize-i > j){
                        cases.add(null);
                        continue;
                    }
                }else{
                    if(size+demisize-i < j || -demisize+i > j){
                        cases.add(null);
                        continue;
                    }
                }

                initCase(i,j,r);

                if(i<demisize){
                    if(demisize+i -5+cases.get(cases.size() - 1).getZlvl()*5< j || demisize-i+5-cases.get(cases.size() - 1).getZlvl()*5 > j){
                        cases.get(cases.size() - 1).getTerrain().setConstructible(false);
                    }
                }else{
                    if(size+demisize-i-7+cases.get(cases.size() - 1).getZlvl()*5 < j || -demisize+i+6-cases.get(cases.size() - 1).getZlvl()*5 > j){
                        cases.get(cases.size() - 1).getTerrain().setConstructible(false);
                    }
                }

            }
        }
        updateCoast();
        updateElevation(1);
        updateElevation(2);
        updateElevation(3);
        addEnvironment();
    }
    public void correctError(int lvl){
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Case c = getCase(j, i);
                if (c == null)
                    continue;
                if (!(c.getTerrain() instanceof Elevation && c.getZlvl() == lvl - 1))
                    continue;
                ElevationType type = ElevationType.getElevationType(getElevationNbr(getNeighbourg(c),lvl));
                if(type == ElevationType.E13 || type == ElevationType.E14 || type == ElevationType.E15 || type == ElevationType.E16){
                    int nbr = r.nextInt(4);
                    Rock rock = new Rock(j,i,city,1, Rock.RockType.NORMAL,nbr);
                    c.setTerrain(rock);
                    rock.setMainCase(c);
                }
            }
        }
    }


    public void updateElevation(int lvl){
        correctError(lvl);
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Case c = getCase(j, i);
                if(c == null)
                    continue;
                if(!((c.getTerrain() instanceof Elevation && c.getZlvl() == lvl - 1) || (c.getZlvl() == lvl && (c.getTerrain() instanceof Empty || c.getTerrain() instanceof Meadow))))
                    continue;
                ElevationType type = ElevationType.getElevationType(getElevationNbr(getNeighbourg(c),lvl));
                if(type == ElevationType.NONE){
                    Terrain t = getTerrain((map[i][j] & 0b00001111) | 0b00010000,j,i,city,r);//force empty but keep environment
                    if(CaseInfo.compareEnv(map[i][j],CaseInfo.MEADOW)){
                        t = new Meadow(j,i,city,0);
                    }
                    c.setTerrain(t);
                    t.setMainCase(c);
                    c.setZlvl(lvl);
                }else if(type == ElevationType.ERROR) {
                    Terrain t = getTerrain((map[i][j] & 0b00001111) | 0b00010000,j,i,city,r);//force empty but keep environment
                    c.setTerrain(t);
                    t.setMainCase(c);
                    c.setZlvl(lvl-1);
                }else{
                    if(type == ElevationType.E13 || type == ElevationType.E14 || type == ElevationType.E15 || type == ElevationType.E16){
                        int nbr = r.nextInt(4);
                        Rock rock = new Rock(j,i,city,1, Rock.RockType.NORMAL,nbr);
                        c.setTerrain(rock);
                        rock.setMainCase(c);
                    }else {
                        int nbr = r.nextInt(4);
                        /*
                        if(CaseInfo.compareEnv(map[i][j], CaseInfo.OTHER)){

                        }*/

                        Elevation e = new Elevation(j, i, type, city, 1, CaseInfo.compareEnv(map[i][j], CaseInfo.OTHER), nbr);
                        c.setTerrain(e);
                        e.setMainCase(c);
                    }
                }
            }
        }
    }

    public int getElevationNbr(Case[] n, int lvl){
        int v = 0;
        for(int i=0;i<8;i++){
            v = v<<1;
            if(n[i] == null)
                v+=1;
            if(n[i] != null && ((n[i].getTerrain() instanceof Elevation && n[i].getZlvl() == lvl - 1)  || n[i].getZlvl() == lvl)){
                v+=1;
            }
        }
        return v;
    }

    public void updateCoast(){
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Case c = getCase(j, i);
                if(c == null)
                    continue;
                if(!(c.getTerrain() instanceof Empty || c.getTerrain() instanceof WaterCoast))
                    continue;
                CoastType type = CoastSelector.getCoastType(getCoastNbr(getNeighbourg(c)));
                if(type == CoastType.NONE)
                    continue;
                int nbr = r.nextInt(4);
                WaterCoast w = new WaterCoast(j, i, type, nbr, city);
                c.setTerrain(w);
                w.setMainCase(c);
            }
        }
    }
    public int getCoastNbr(Case[] n){
        int v = 0;
        for(int i=0;i<8;i++){
            v = v<<1;
            if(n[i] != null && n[i].getTerrain() instanceof Water){
                v+=1;
            }
        }
        return v;
    }

    public Case[] getNeighbourg(Case c){
        Case[] n = new Case[8];
        int x = c.getxPos();
        int y = c.getyPos();
        n[0] = getCase(x-1,y-1);
        n[1] = getCase(x,y-1);
        n[2] = getCase(x+1,y-1);
        n[3] = getCase(x+1,y);
        n[4] = getCase(x+1,y+1);
        n[5] = getCase(x,y+1);
        n[6] = getCase(x-1,y+1);
        n[7] = getCase(x-1,y);
        return n;
    }

    public ArrayList<Case> getNotNullCases() {
        return notNullCases;
    }

    public void populateMap() {

        /*
        createSea(15, 15, 9, 7);
        createForest(30, 30, 4, 6);
        createElevation(5, 30, 5, 5, 1);
        createElevation(13, 30, 5, 5, 2);

        createElevation(35, 10, 10, 10, 2);
        createElevation(37, 12, 4, 4, 1);

        createMinerals(30, 25, 3, 3);

        createMeadow(20, 5, 10, 7);

        */
    }

    public void createMeadow(int xs, int ys, int xlen, int ylen) {
        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);
                Meadow m = new Meadow(i, j, city, 0);
                c.setTerrain(m);
            }
        }
    }

    public void createForest(int xs, int ys, int xlen, int ylen) {
        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);
                Random r = new Random();
                int nbr = r.nextInt(96);
                Tree tree = new Tree(i, j, nbr, city, 0);
                c.setObject(tree);
                trees.add(c);
            }
        }
    }

    public void createMinerals(int xs, int ys, int xlen, int ylen) {
        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);
                Random r = new Random();
                int nbr = r.nextInt(8);
                Rock rock = new Rock(i, j, city, 1, Rock.RockType.COPPER, nbr);
                rock.setMainCase(c);
                c.setTerrain(rock);
                coppers.add(c);
            }
        }
    }

    public void createElevation(int xs, int ys, int xlen, int ylen, int size) {

        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);

                Random r = new Random();
                int nbr = 0;
                if (i == xs) {
                    Elevation w;
                    if (j == ys)
                        w = new Elevation(i, j, Direction.NORTH, nbr, city, null, size);
                    else if (j == ys + ylen - 1)
                        w = new Elevation(i, j, Direction.WEST, nbr, city, null, size);
                    else
                        w = new Elevation(i, j, Direction.NORTH_WEST, nbr, city, null, size);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (i == xs + xlen - 1) {
                    Elevation w;

                    if (j == ys)
                        w = new Elevation(i, j, Direction.EAST, nbr, city, null, size);
                    else if (j == ys + ylen - 1)
                        w = new Elevation(i, j, Direction.SOUTH, nbr, city, null, size);
                    else
                        w = new Elevation(i, j, Direction.SOUTH_EAST, nbr, city, null, size, r.nextBoolean());
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (j == ys) {
                    Elevation w;
                    if (i == xs + xlen - 1)
                        w = new Elevation(i, j, Direction.EAST, nbr, city, null, size);
                    else
                        w = new Elevation(i, j, Direction.NORTH_EAST, nbr, city, null, size);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (j == ys + ylen - 1) {
                    Elevation w = new Elevation(i, j, Direction.SOUTH_WEST, nbr, city, null, size, r.nextBoolean());
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else {
                    Empty e = new Empty(1, city);
                    e.setxPos(i);
                    e.setyPos(j);
                    c.setTerrain(e);
                    e.setMainCase(c);
                    c.setZlvl(c.getZlvl() + size);
                }


            }
        }

    }


    public void createSea(int xs, int ys, int xlen, int ylen) {


        for (int i = xs; i < xs + xlen; i++) {
            for (int j = ys; j < ys + ylen; j++) {
                Case c = getCase(i, j);

                Random r = new Random();
                int nbr = r.nextInt(4);
                if (i == xs) {
                    WaterCoast w;
                    if (j == ys)
                        w = new WaterCoast(i, j, false, Direction.SOUTH, nbr, city);
                    else if (j == ys + ylen - 1)
                        w = new WaterCoast(i, j, false, Direction.EAST, nbr, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.SOUTH_EAST, nbr, city);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (i == xs + xlen - 1) {
                    WaterCoast w;

                    if (j == ys)
                        w = new WaterCoast(i, j, false, Direction.WEST, nbr, city);
                    else if (j == ys + ylen - 1)
                        w = new WaterCoast(i, j, false, Direction.NORTH, nbr, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.NORTH_WEST, nbr, city);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (j == ys) {
                    WaterCoast w;
                    if (i == xs + xlen - 1)
                        w = new WaterCoast(i, j, false, Direction.WEST, nbr, city);
                    else
                        w = new WaterCoast(i, j, false, Direction.SOUTH_WEST, nbr, city);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else if (j == ys + ylen - 1) {
                    WaterCoast w = new WaterCoast(i, j, false, Direction.NORTH_EAST, nbr, city);
                    c.setTerrain(w);
                    w.setMainCase(c);
                } else {
                    Water w = new Water(i, j, city);
                    c.setTerrain(w);
                    w.setMainCase(c);
                }


            }
        }


    }

    public int getSize() {
        return size;
    }


    public ArrayList<Case> getCoppers() {
        return coppers;
    }

    public ArrayList<Case> getSilvers() {
        return silvers;
    }

    public ArrayList<Case> getTrees() {
        return trees;
    }

    public ArrayList<Case> getMeadows() {
        return meadows;
    }

    public Case getCase(int x, int y) {
        if (x >= 0 && y >= 0 && x < size && y < size)
            return cases.get(size * y + x);

        return null;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < lenght; i++) {
            for (int j = 0; j < lenght; j++) {
                str.append(" ").append(cases.get(j + i * lenght).getObject().getBuildingType().ordinal());
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

    public Case[] getCaseSorted() {
        return caseSorted;
    }

    public void setCaseSorted(Case[] caseSorted) {
        this.caseSorted = caseSorted;
    }

    public Case getEndCase() {
        return endCase;
    }
}



