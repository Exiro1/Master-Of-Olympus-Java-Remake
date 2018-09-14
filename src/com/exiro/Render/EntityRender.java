package com.exiro.Render;


import com.exiro.BuildingList.BuildingType;
import com.exiro.BuildingList.House;
import com.exiro.BuildingList.Stock;
import com.exiro.BuildingList.WaterWell;
import com.exiro.ConstructionList.Road;
import com.exiro.MoveRelated.Path;
import com.exiro.MoveRelated.RoadMap;
import com.exiro.Object.Case;
import com.exiro.Object.CityMap;
import com.exiro.Object.ObjectClass;
import com.exiro.SystemCore.GameManager;
import com.exiro.Utils.Point;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

//dessine le batiment selectionner pour construire
public class EntityRender {


    public static ArrayList<ObjectClass> toBuild = new ArrayList<>();
    public static BufferedImage img;
    public static int x, y, width, heigth, size;
    public static BuildingType Btype;
    static public ObjectClass defaultObject;

    public static void setEntityRender(BuildingType type) {
        defaultObject = getDefault(type);
        img = type.getImg();
        heigth = defaultObject.getHeight();
        width = defaultObject.getWidth();
        size = defaultObject.getSize();

        BufferedImage target = new BufferedImage(img.getWidth(), img.getHeight(), java.awt.Transparency.TRANSLUCENT);
        // Get the images graphics
        Graphics2D g = target.createGraphics();
        // Set the Graphics composite to Alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) 0.6));

        // Draw the image into the prepared reciver image
        g.drawImage(img, null, 0, 0);

        // let go of all system resources in this Graphics
        g.dispose();
        // Return the image
        img = target;
        toBuild.clear();
        Btype = type;
        //Exemple

        ObjectClass obj = null;
        obj = getDefault(defaultObject.getBuildingType());
        obj.setXB(0);
        obj.setYB(0);
        toBuild.add(obj);
    }

    public static ObjectClass getDefault(BuildingType type) {
        switch (type) {
            case EMPTY:
                break;
            case ROAD:
                return Road.DEFAULT();
            case BLOCKABLE_ROAD:
                break;
            case HOUSE:
                return House.DEFAULT();
            case WATERWELL:
                return WaterWell.DEFAULT();
            case STOCK:
                return Stock.DEFAULT();
        }
        return null;
    }

    public static void addBuilding(Point p) {
        CityMap map = GameManager.currentCity.getMap();

        Path path = GameManager.currentCity.getPathManager().getPathTo(map.getCase(toBuild.get(0).getXB(), toBuild.get(0).getYB()), map.getCase((int) p.x, (int) p.y), RoadMap.FreeState.NON_BLOCKING);
        if (path != null) {
            toBuild.clear();
            for (Case c : path.getPath()) {
                ObjectClass obj = null;
                obj = getDefault(defaultObject.getBuildingType());
                obj.setXB(c.getxPos());
                obj.setYB(c.getyPos());
                toBuild.add(obj);
            }
        }
    }

    public static Point getStartPoint() {
        return new Point(x, y);
    }

    public static void buildAll() {

        for (ObjectClass obj : toBuild) {
            obj.build(obj.getXB(), obj.getYB()); // construit tout
        }
        EntityRender.setEntityRender(defaultObject.getBuildingType());


    }


}
