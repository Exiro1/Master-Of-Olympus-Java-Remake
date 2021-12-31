package com.exiro.fileManager;

public enum CaseInfo {


    WATER(0b11110000), LVL0(0b00010000), LVL1(0b00100000), LVL2(0b00110000), LVL3(0b01000000), LVL4(0b01010000),
    FISH(0b00000001), FOREST(0b00000010), OTHER(0b00000001), MEADOW(0b00000100),STARTENDCASE(0b00001000);

    int data;

    CaseInfo(int i) {
        data = i;
    }

    public static boolean compareEnv(int a,CaseInfo b){
        return (a & b.getData()) == b.getData();
    }
    public static boolean compareTerrain(int a,CaseInfo b){
        return (a & 0xF0) == b.getData();
    }

    public static int getLvl(int val){
        if(compareTerrain(val, WATER)){
            return -1;
        }else if(compareTerrain(val, LVL0)){
            return 0;
        }else if(compareTerrain(val, LVL1)){
            return 1;
        }else if(compareTerrain(val, LVL2)){
            return 2;
        }else if(compareTerrain(val, LVL3)){
            return 3;
        }else if(compareTerrain(val, LVL4)){
            return 4;
        }
        return -1;
    }

    public int getData() {
        return data;
    }
}
