package com.exiro.fileManager;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class FontLoader {


    static HashMap<Integer, Font> AssetsMap;
    static HashMap<Integer, Integer> AssetsMapUse;


    public static void initLoader() {
        AssetsMap = new HashMap<>();
        AssetsMapUse = new HashMap<>();
    }


    public static void unloadImage(String font) {
        String hash = font;
        int globalID = hash.hashCode();
        AssetsMapUse.replace(globalID, AssetsMapUse.get(globalID) - 1);
        if (AssetsMapUse.get(globalID) == 0) {
            AssetsMap.remove(globalID);
            AssetsMapUse.remove(globalID);
        }
    }


    public static Font getFont(String font) {

        String hash = font;
        int globalID = hash.hashCode();
        if (!AssetsMap.containsKey(globalID)) {
            File f = new File("Assets/Fonts/" + font);
            try {
                AssetsMap.put(globalID, Font.createFont(Font.PLAIN, f));
                AssetsMapUse.put(globalID, 1);
            } catch (FontFormatException | IOException e) {
                e.printStackTrace();
            }
        } else {
            AssetsMapUse.replace(globalID, AssetsMapUse.get(globalID) + 1);
        }
        return AssetsMap.get(globalID);
    }

}
