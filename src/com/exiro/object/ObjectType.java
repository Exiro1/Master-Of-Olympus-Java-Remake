package com.exiro.object;

import com.exiro.buildingList.House;
import com.exiro.buildingList.agriculture.*;
import com.exiro.buildingList.army.Armory;
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
import java.util.function.Function;

public enum ObjectType {


    //Buildings
    HOUSE("Maison", true, false, "Zeus_General", 2, 8, 14, House::new),
    WATERWELL("Puit", true, false, "Zeus_General", 2, 9, 0, WaterWell::new),
    STOCK("Stock", true, false, "Zeus_General", 3, 3, 22, StoreHouse::new),
    GRANARY("Grenier", true, false, "Zeus_General", 4, 3, 28, Granary::new),
    FARM("Ferme", true, false, "Zeus_General", 3, 4, 12, Farm::new),
    SMALLHOLDING("Métairie", true, false, "Zeus_General", 2, 4, 90, SmallHolding::new),
    SHEEPFOLD("Bergerie", true, false, "Zeus_General", 2, 4, 55, Sheepfold::new),
    DAIRY("Laiterie", true, false, "Zeus_General", 2, 4, 68, Dairy::new),
    HUNTINGHOUSE("Maison de chasse", false, true, "Zeus_General", 2, 4, 31, HuntingHouse::new),
    FISHERY("Pecherie", true, false, "Zeus_General", 2, 4, 47, Fishery::new),
    SAFETY("Sécurité", true, false, "Zeus_General", 2, 10, 0, Safety::new),
    AGORA("Agora", true, false, "Zeus_General", 2, 3, 15, Agora::new),
    COLLEGE("Collège", true, false, "Zeus_General", 3, 2, 0, College::new),
    GYMNASIUM("Gymnase", true, false, "Zeus_General", 3, 2, 25, Gymnasium::new),
    THEATERSCHOOL("Ecole de théatre", true, false, "Zeus_General", 3, 2, 58, TheaterSchool::new),
    PODIUM("Podium", true, false, "Zeus_General", 2, 2, 83, Podium::new),
    THEATER("Théatre", true, false, "Zeus_General", 5, 2, 108, Theater::new),
    TAX("Maison des impots", true, false, "Zeus_General", 2, 10, 45, TaxBuilding::new),
    SECURITY_STATION("Poste de sécurité", true, false, "Zeus_General", 2, 10, 64, SecurityStation::new),


    //Indusrty
    SAWMILL("Scierie", true, false, "Zeus_General", 2, 5, 0, Sawmill::new),
    MARBLE_QUARRY("Carriere de marbre", true, false, "Zeus_General", 2, 5, 11, MarbleFactory::new),
    MINT("Atelier de monnayage", true, false, "Zeus_General", 2, 5, 20, Mint::new),
    FOUNDRY("Fonderie", true, false, "Zeus_General", 2, 5, 35, Foundry::new),
    GUILD("Guilde d'artisant", true, false, "Zeus_General", 2, 5, 48, Guild::new),
    OLIVE_PRESS("Pressoir à olives", true, false, "Zeus_General", 2, 5, 59, OlivePress::new),
    WINERY("Pressoir à vin", true, false, "Zeus_General", 2, 5, 72, Winery::new),
    SCULPTURE_STUDIO("Atelier de sculpture", true, false, "Zeus_General", 2, 5, 85, SculptureStudio::new),

    //agora shop
    AGORAEMPTY("Emplacement libre", true, false, "Zeus_General", 2, 3, 15, EmptyShop::new),
    AGORAFOOD("Epicerie", true, false, "Zeus_General", 2, 3, 0, FoodShop::new),
    AGORAWOOL("Marchand de laine", true, false, "Zeus_General", 2, 3, 2, WoolShop::new),
    AGORAOIL("Marchand d'huile d'olive", true, false, "Zeus_General", 2, 3, 4, OilShop::new),

    //ARMY
    ARMORY("Armurerie", true, false, "Zeus_General", 2, 6, 21, Armory::new),

    //Terrain
    WATERTERRAIN("Eau", true, false, "Zeus_Terrain", 1, 1, 163, null),
    WATERTCOAST("Cote maritime", true, false, "Zeus_Terrain", 1, 1, 171, null),
    EMPTY("Rien", false, true, "Zeus_Terrain", 1, 1, 1, null),
    EARTHQUAKE("Tremblement de terre", true, false, "Zeus_Terrain", 1, 1, 243, null),
    ROCK("Pierre", true, false, "Zeus_Terrain", 1, 1, 333, null),
    ELEVATION("Elevation", true, false, "Zeus_Terrain", 1, 7, 14, null),
    MEADOW("Prairie", false, true, "Zeus_Terrain", 1, 4, 120, null),

    //Construction
    ROAD("Route", false, true, "Zeus_General", 1, 1, 54, Road::new),
    BLOCKABLE_ROAD("Stop", true, true, "Zeus_General", 1, 10, 75, BlockingRoad::new),
    RUBBLE("Décombre", true, false, "Zeus_Terrain", 1, 2, 20, Rubble::new),

    //Environment
    TREE("Arbre", true, true, "Zeus_Terrain", 1, 4, 0, null),

    //Other
    OLIVETREE("Olivier", true, true, "Zeus_General", 1, 4, 0, OliveTree::new),
    VINE("Vigne", true, true, "Zeus_General", 1, 4, 6, Vine::new),

    //SPECIAL
    ERASE("Destruction", true, true, "Zeus_Terrain", 1, 2, 36, Destruction::new),

    //SPRITE
    SHEEP("Mouton", false, true, "SprMain", 1, 0, 3182, SheepBuildingBind::new),
    GOAT("Chevre", false, true, "SprMain", 1, 0, 11227, GoatBuildingBind::new);

    // creates the object placed by the build tool; null for terrain, which is never placed
    private final Function<City, ObjectClass> factory;
    private final String name;
    private final boolean block, walk;
    private final int heigth, width;
    private final int size;
    private final String path;
    private BufferedImage img;
    private int bitmapID, localID;

    ObjectType(String name, boolean block, boolean walk, String path, int size, int bitmapID, int localID, Function<City, ObjectClass> factory) {
        this.factory = factory;
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

    /**
     * A new, unbuilt object of this type belonging to {@code city}.
     */
    public ObjectClass getDefault(City city) {
        return factory == null ? null : factory.apply(city);
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

    public boolean isWalk() {
        return walk;
    }

    public int getSize() {
        return size;
    }
}
