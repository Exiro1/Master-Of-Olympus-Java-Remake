package com.exiro.buildingList.delivery.agorashop;

import com.exiro.buildingList.delivery.Agora;
import com.exiro.buildingList.delivery.AgoraShop;
import com.exiro.object.Case;
import com.exiro.object.ObjectType;

import java.util.ArrayList;

public class EmptyShop extends AgoraShopBuilding {


    public EmptyShop() {
        super(AgoraShop.EMPTY, ObjectType.AGORAEMPTY);
        this.setPopMax(0);
    }

    @Override
    public boolean build(int xPos, int yPos) {
        ArrayList<Case> cases = getPlace(xPos, yPos, 0, 0, city);
        if (cases != null) {
            Agora agora = (Agora) cases.get(0).getObject();
            this.agora = agora;
            if (agora.addShop(this)) {
                setxPos(xPos);
                setyPos(yPos);
                setXB(getxPos());
                setYB(getyPos());

                for (Case c : cases) {
                    c.setOccuped(true);
                    c.setObject(this);
                    c.setMainCase(false);
                }

                city.getMap().getCase(xPos, yPos).setMainCase(true);
                this.setMainCase(city.getMap().getCase(getxPos(), getyPos()));

                return true;
            }
        }

        return false;
    }

}
