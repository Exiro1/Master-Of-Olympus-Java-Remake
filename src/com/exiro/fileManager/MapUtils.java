package com.exiro.fileManager;

import com.exiro.terrainGenerator.PerlinNoise;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MapUtils {


    static public int[][] createMap(int seed, MapSettings settings){
        boolean start = false;
        PerlinNoise pn = new PerlinNoise(50);
        ArrayList<Point> meadow = new ArrayList<>();
        int size = (int) (settings.size*Math.sqrt(2));
        int[][] map = pn.normalize(pn.getMap(size,settings.getResolution(),130,settings.getFactor(),seed));
        int[][] finalMap = pn.applyTreshold(map, settings.threshold);
        int demisize = finalMap.length/2;
        for(int l=0;l<finalMap.length;l++) {
            for (int k = 0; k < finalMap.length; k++) {

                if(l<demisize){
                    if(demisize+l < k || demisize-l > k){
                        continue;
                    }
                }else{
                    if(size+demisize-l < k || -demisize+l > k){
                        continue;
                    }
                }

                finalMap[l][k] = finalMap[l][k] << 4;
                if(finalMap[l][k] == 0)
                    finalMap[l][k] = 0b11110000;
                if (map[l][k] > settings.minForest && map[l][k] < settings.maxForest) {
                    finalMap[l][k] = finalMap[l][k] | CaseInfo.FOREST.getData();
                }

                if (map[l][k] > settings.minMeadow && map[l][k] < settings.maxMeadow && pn.getRan().nextInt(1000) == 500 && meadow.size() < 4) {
                    meadow.add(new Point(k, l));
                }

                if (map[l][k] > 0 && map[l][k] < settings.maxFish && pn.getRan().nextInt(400) == 200) {
                    finalMap[l][k] = finalMap[l][k] | CaseInfo.FISH.getData();
                }

                if(CaseInfo.compareTerrain(finalMap[l][k],CaseInfo.LVL0) && !CaseInfo.compareEnv(finalMap[l][k],CaseInfo.FOREST) && !start){
                    finalMap[l][k] = finalMap[l][k] | CaseInfo.OTHER.getData();
                    start = true;
                }

            }
        }

        //generate Meadow
        for(Point p : meadow){
            for(int l=p.y-12;l< p.y+12;l++) {
                for (int k = p.x-12; k < p.x+12; k++) {
                    if (k >= 0 && l >= 0 && k < map.length && l < map.length){
                        if (map[l][k] > map[p.y][p.x] - 4 && map[l][k] < map[p.y][p.x] + 4) {
                            finalMap[l][k] = finalMap[l][k] | CaseInfo.MEADOW.getData();
                        }
                    }
                }
            }
        }

        return finalMap;
    }



    static public void SaveMap(int[][] map, String path){
        int size = 8+map.length*map.length;

        Byte[] data = new Byte[size];

        int psize = (int) (map.length/Math.sqrt(2));

        data[0] =  (byte) ((map.length >> 8) & 0xFF);
        data[1] = (byte) (map.length & 0xFF);
        data[2] =  (byte) ((psize >> 8) & 0xFF);
        data[3] = (byte) (psize & 0xFF);
        data[4] = 0;
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;
        int i=8;
        for(int l=0;l<map.length;l++) {
            for (int k = 0; k < map.length; k++) {
                data[i] = (byte) map[l][k];
            }
        }
    }



}
