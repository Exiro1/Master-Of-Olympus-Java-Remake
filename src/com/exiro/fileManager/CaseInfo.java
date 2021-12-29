package com.exiro.fileManager;

public enum CaseInfo {


    WATER(0b11110000), LVL0(0b00010000), LVL1(0b00100000), LVL2(0b00110000), LVL3(0b01000000), LVL4(0b01010000),
    FISH(0b00000001), FOREST(0b00000010), OTHER(0b00000001), MEADOW(0b00000100);

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

    public int getData() {
        return data;
    }
}
