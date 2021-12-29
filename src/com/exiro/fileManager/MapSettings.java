package com.exiro.fileManager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MapSettings {
    int[] threshold;
    int minMeadow,maxMeadow,maxFish,minForest,maxForest;
    int resolution;
    float factor;
    int size;
    int seed=0;

    public MapSettings(int[] threshold, int minMeadow, int maxMeadow, int maxFish, int minForest, int maxForest,float factor,int resolution,int size) {
        this.threshold = threshold;
        this.minMeadow = minMeadow;
        this.maxMeadow = maxMeadow;
        this.maxFish = Math.min(maxFish, threshold[0]);
        this.minForest = minForest;
        this.maxForest = maxForest;
        this.factor = factor;
        this.resolution = resolution;
        this.size = size;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    static public void SaveSettings(MapSettings settings, String path){
        try (PrintWriter out = new PrintWriter(path)) {
            for(int t : settings.threshold){
                out.print(t+";");
            }
            out.print(":");
            out.print(settings.minMeadow+";");
            out.print(settings.maxMeadow+";");
            out.print(settings.maxFish+";");
            out.print(settings.minForest+";");
            out.print(settings.maxForest+";");
            out.print(settings.factor+";");
            out.print(settings.resolution+";");
            out.print(settings.size+";");
            out.print(settings.seed);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static public MapSettings loadSettings(String path) {
        String content = null;
        try {
            content = Files.readAllLines(Paths.get(path)).get(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int[] t = new int[content.split(":")[0].split(";").length];
        int i=0;
        for(String s : content.split(":")[0].split(";")) {
            t[i] = Integer.parseInt(s);
            i++;
        }
        String[] others = content.split(":")[1].split(";");
        MapSettings setting = new MapSettings(t,Integer.parseInt(others[0]),Integer.parseInt(others[1]),Integer.parseInt(others[2]),Integer.parseInt(others[3]),Integer.parseInt(others[4]),Float.parseFloat(others[5]),Integer.parseInt(others[6]),Integer.parseInt(others[7]));
        if(others.length == 9)
            setting.setSeed(Integer.parseInt(others[8]));
        return setting;
    }

    public static MapSettings getDefaultSettings(int size){
        return new MapSettings(new int[]{25,69,80,95,100},40,60,18,70,79,0.55f,5,size);
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public float getFactor() {
        return factor;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    public int[] getThreshold() {
        return threshold;
    }

    public void setThreshold(int[] threshold) {
        this.threshold = threshold;
    }

    public int getMinMeadow() {
        return minMeadow;
    }

    public void setMinMeadow(int minMeadow) {
        this.minMeadow = minMeadow;
    }

    public int getMaxMeadow() {
        return maxMeadow;
    }

    public void setMaxMeadow(int maxMeadow) {
        this.maxMeadow = maxMeadow;
    }

    public int getMaxFish() {
        return maxFish;
    }

    public void setMaxFish(int maxFish) {
        this.maxFish = maxFish;
    }

    public int getMinForest() {
        return minForest;
    }

    public void setMinForest(int minForest) {
        this.minForest = minForest;
    }

    public int getMaxForest() {
        return maxForest;
    }

    public void setMaxForest(int maxForest) {
        this.maxForest = maxForest;
    }
}
