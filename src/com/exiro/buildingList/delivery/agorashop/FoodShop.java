package com.exiro.buildingList.delivery.agorashop;

import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.object.Case;
import com.exiro.object.ObjectType;
import com.exiro.object.Resource;
import com.exiro.sprite.BuildingSprite;

import java.util.ArrayList;

public class FoodShop extends AgoraShopBuilding {


    public FoodShop() {
        super(AgoraShop.FOOD, ObjectType.AGORAFOOD);
        resourcesPossible = new ArrayList<>();
        resourcesPossible.add(Resource.CHEESE);
        resourcesPossible.add(Resource.FISH);
        resourcesPossible.add(Resource.CARROT);
        resourcesPossible.add(Resource.CORN);
        resourcesPossible.add(Resource.SEA_URCHIN);
        resourcesPossible.add(Resource.MEAT);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> cases = getPlace(xPos, yPos, 0, 0, city);
        if (cases != null) {
            Agora agora = (Agora) cases.get(0).getObject();
            this.agora = agora;
            if (agora.addShop(this)) {
                BuildingSprite bs = new BuildingSprite("Zeus_General", 3, shop.getId() + 1, 1, city, this);
                bs.setOffsetX(25);
                bs.setOffsetY(-3);
                addSprite(bs);

                setXB(getxPos());
                setYB(getyPos());
                city.addBuilding(this);
                city.addObj(this);

                for (Case c : cases) {
                    c.setOccuped(true);
                    c.setObject(this);
                    c.setMainCase(false);
                }
                city.getMap().getCase(xPos, yPos).setMainCase(true);
                this.setMainCase(city.getMap().getCase(xPos, yPos));
                return true;
            }
        }

        return false;
    }


}
