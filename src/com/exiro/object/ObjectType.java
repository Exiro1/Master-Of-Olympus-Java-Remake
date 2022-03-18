package com.exiro.object;

import com.exiro.buildingList.House;
import com.exiro.buildingList.agriculture.*;
import com.exiro.buildingList.culture.*;
import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.WaterWell;
import com.exiro.buildingList.delivery.agorashop.EmptyShop;
import com.exiro.buildingList.delivery.agorashop.FoodShop;
import com.exiro.buildingList.delivery.agorashop.OilShop;
import com.exiro.buildingList.delivery.agorashop.WoolShop;
import com.exiro.buildingList.industry.*;
import com.exiro.buildingList.palace.TaxBuilding;
import com.exiro.buildingList.security.Safety;
import com.exiro.buildingList.security.SecurityStation;
import com.exiro.buildingList.stockage.Granary;
import com.exiro.buildingList.stockage.StoreHouse;
import com.exiro.constructionList.*;
import com.exiro.constructionList.SmallHoldingFruit.OliveTree;
import com.exiro.constructionList.SmallHoldingFruit.Vine;
import com.exiro.fileManager.ImageLoader;
import com.exiro.terrainList.*;

import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

public enum ObjectType {


    //Buildings
    HOUSE("Maison", true, false, "Zeus_General", 2, 8, 14, House.class),
    WATERWELL("Puit", true, false, "Zeus_General", 2, 9, 0, WaterWell.class),
    STOCK("Stock", true, false, "Zeus_General", 3, 3, 22, StoreHouse.class),
    GRANARY("Grenier", true, false, "Zeus_General", 4, 3, 28, Granary.class),
    FARM("Ferme", true, false, "Zeus_General", 3, 4, 12, Farm.class),
    SMALLHOLDING("Métairie", true, false, "Zeus_General", 2, 4, 90, SmallHolding.class),
    SHEEPFOLD("Bergerie", true, false, "Zeus_General", 2, 4, 55, Sheepfold.class),
    DAIRY("Laiterie", true, false, "Zeus_General", 2, 4, 68, Dairy.class),
    HUNTINGHOUSE("Maison de chasse", false, true, "Zeus_General", 2, 4, 31, HuntingHouse.class),
    FISHERY("Pecherie", true, false, "Zeus_General", 2, 4, 47, Fishery.class),
    SAFETY("Sécurité", true, false, "Zeus_General", 2, 10, 0, Safety.class),
    AGORA("Agora", true, false, "Zeus_General", 2, 3, 15, Agora.class),
    COLLEGE("Collège", true, false, "Zeus_General", 3, 2, 0, College.class),
    GYMNASIUM("Gymnase", true, false, "Zeus_General", 3, 2, 25, Gymnasium.class),
    THEATERSCHOOL("Ecole de théatre", true, false, "Zeus_General", 3, 2, 58, TheaterSchool.class),
    PODIUM("Podium", true, false, "Zeus_General", 2, 2, 83, Podium.class),
    THEATER("Théatre", true, false, "Zeus_General", 5, 2, 108, Theater.class),
    TAX("Maison des impots", true, false, "Zeus_General", 2, 10, 45, TaxBuilding.class),
    SECURITY_STATION("Poste de sécurité", true, false, "Zeus_General", 2, 10, 64, SecurityStation.class),


    //Indusrty
    SAWMILL("Scierie", true, false, "Zeus_General", 2, 5, 0, Sawmill.class),
    MARBLE_QUARRY("Carriere de marbre", true, false, "Zeus_General", 2, 5, 11, MarbleFactory.class),
    MINT("Atelier de monnayage", true, false, "Zeus_General", 2, 5, 20, Mint.class),
    FOUNDRY("Fonderie", true, false, "Zeus_General", 2, 5, 35, Foundry.class),
    GUILD("Guilde d'artisant", true, false, "Zeus_General", 2, 5, 48, Guild.class),
    OLIVE_PRESS("Pressoir à olives", true, false, "Zeus_General", 2, 5, 59, OlivePress.class),
    WINERY("Pressoir à vin", true, false, "Zeus_General", 2, 5, 72, Winery.class),
    SCULPTURE_STUDIO("Atelier de sculpture", true, false, "Zeus_General", 2, 5, 85, SculptureStudio.class),

    //agora shop
    AGORAEMPTY("Emplacement libre", true, false, "Zeus_General", 2, 3, 15, EmptyShop.class),
    AGORAFOOD("Epicerie", true, false, "Zeus_General", 2, 3, 0, FoodShop.class),
    AGORAWOOL("Marchand de laine", true, false, "Zeus_General", 2, 3, 2, WoolShop.class),
    AGORAOIL("Marchand d'huile d'olive", true, false, "Zeus_General", 2, 3, 4, OilShop.class),


    //Terrain
    WATERTERRAIN("Eau", true, false, "Zeus_Terrain", 1, 1, 163, Water.class),
    WATERTCOAST("Cote maritime", true, false, "Zeus_Terrain", 1, 1, 171, WaterCoast.class),
    EMPTY("Rien", false, true, "Zeus_Terrain", 1, 1, 1, Empty.class),
    EARTHQUAKE("Tremblement de terre", true, false, "Zeus_Terrain", 1, 1, 243, Earthquake.class),
    ROCK("Pierre", true, false, "Zeus_Terrain", 1, 1, 333, Rock.class),
    ELEVATION("Elevation", true, false, "Zeus_Terrain", 1, 7, 14, Elevation.class),
    MEADOW("Prairie", false, true, "Zeus_Terrain", 1, 4, 120, Meadow.class),

    //Construction
    ROAD("Route", false, true, "Zeus_General", 1, 1, 54, Road.class),
    BLOCKABLE_ROAD("Stop", true, true, "Zeus_General", 1, 10, 75, BlockingRoad.class),
    RUBBLE("Décombre", true, false, "Zeus_Terrain", 1, 2, 20, Rubble.class),

    //Environment
    TREE("Arbre", true, true, "Zeus_Terrain", 1, 4, 0, Tree.class),

    //Other
    OLIVETREE("Olivier", true, true, "Zeus_General", 1, 4, 0, OliveTree.class),
    VINE("Vigne", true, true, "Zeus_General", 1, 4, 6, Vine.class),

    //SPECIAL
    ERASE("Destruction", true, true, "Zeus_Terrain", 1, 2, 36, Destruction.class),

    //SPRITE
    SHEEP("Mouton", false, true, "SprMain", 1, 0, 3182, SheepBuildingBind.class),
    GOAT("Chevre", false, true, "SprMain", 1, 0, 11227, GoatBuildingBind.class);

    private final Class c;
    private final String name;
    private final boolean block, walk;
    private final int heigth, width;
    private final int size;
    private final String path;
    private BufferedImage img;
    private int bitmapID, localID;

    ObjectType(String name, boolean block, boolean walk, String path, int size, int bitmapID, int localID, Class c) {
        this.c = c;
        this.size = size;
        this.name = name;
        this.path = path;
        this.walk = walk;
        img = ImageLoader.getImage(path, bitmapID, localID).getImg();
        this.bitmapID = bitmapID;
        this.localID = localID;
        heigth = img.getHeight();
        width = img.getWidth();

        this.block = block;
    }

    public ObjectClass getDefault() {
        try {
            return (ObjectClass) c.getConstructor().newInstance();
        } catch (InstantiationException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int getBitmapID() {
        return bitmapID;
    }

    public void setBitmapID(int bitmapID) {
        this.bitmapID = bitmapID;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    public String getPath() {
        return path;
    }

    public boolean isBlocking() {
        return this.block;
    }

    public BufferedImage getImg() {
        return img;
    }

    public void setImg(BufferedImage img) {
        this.img = img;
    }

    public int getHeigth() {
        return heigth;
    }

    public int getWidth() {
        return width;
    }

    public String getName() {
        return name;
    }

    public boolean isBlock() {
        return block;
    }

    public Class getC() {
        return c;
    }

    public boolean isWalk() {
        return walk;
    }

    public int getSize() {
        return size;
    }
}
