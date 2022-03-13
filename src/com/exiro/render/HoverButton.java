package com.exiro.render;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.utils.Point;

import java.awt.*;

public class HoverButton extends Button{


    TileImage c1,c2,c3,c4,top,bot,left,right;



    public HoverButton(int x, int y, int w, int h, ButtonType type,int ID) {
        super(x, y, w, h, type, ID);
        c1 = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,355);
        c2 = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,357);
        c3 = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,359);
        c4 = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,361);
        top = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,356);
        bot = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,360);
        left = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,362);
        right = ImageLoader.getImage(ImageLoader.FilePath.ZEUS_INTERFACE,1,358);
    }

    boolean hovering = true;


    public void renderBG(Graphics g, int offx, int offy){
        int x = this.x+offx;
        int y = this.y+offy;

        int nbrw = (int) Math.ceil((float) (w - c1.getW() - c2.getW()) / top.getW());
        int nbrh = (int) Math.ceil((float) (h - c1.getH() - c3.getH()) / left.getH());

        g.drawImage(c1.getImg(), x, y, null);
        for (int i = 1; i <= nbrw; i++) {
            g.drawImage(top.getImg(), x + i * top.getW(), y, null);
        }
        g.drawImage(c2.getImg(), x + w - c2.getW(), y, null);

        for (int j = 1; j <= nbrh; j++) {
            g.drawImage(left.getImg(), x, y + j * left.getH(), null);

            g.drawImage(right.getImg(), x + w - right.getW(), y + j * left.getH(), null);
        }
        g.drawImage(c3.getImg(), x + w - c3.getW(), y + h - c3.getH(), null);
        for (int i = 1; i <= nbrw; i++) {
            g.drawImage(bot.getImg(), x + i * top.getW(), y + h - c3.getH(), null);
        }
        g.drawImage(c4.getImg(), x, y + h - c3.getH(), null);
    }


    @Override
    public void Render(Graphics g, int offx, int offy, Point lastP) {
        super.Render(g,offx,offy, lastP);
        if(isHovering((int) lastP.x-offx, (int) lastP.y-offy)){
            renderBG(g,offx,offy);
        }

    }

    public boolean isHovering(int xc, int yc) {
        if (xc > x && xc < x + w && yc > y && yc < y + h) {
            return true;
        }
        return false;
    }




}
