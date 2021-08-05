package com.exiro.render;

import com.exiro.depacking.TileImage;
import com.exiro.fileManager.ImageLoader;
import com.exiro.render.interfaceList.TextInterface;

import java.awt.*;

public class ComplexButton extends Button {

    TileImage s, m, e;
    int mnbr;
    TextInterface textInterface;

    public ComplexButton(int x, int y, int w, int h, int bitid, int id1, int id2, int id3, ButtonType type, TextInterface text) {
        super(x, y, w, h, bitid, id1, type);
        s = ImageLoader.getImage("Zeus_Interface", bitid, id1);
        m = ImageLoader.getImage("Zeus_Interface", bitid, id2);
        e = ImageLoader.getImage("Zeus_Interface", bitid, id3);

        if (s != null && m != null && e != null)
            mnbr = (w - s.getW() - e.getW()) / m.getW();

        this.textInterface = text;
    }

    @Override
    public void Render(Graphics g, int offx, int offy) {
        int i = x + offx;

        g.drawImage(s.getImg(), i, y + offy, s.getW(), s.getH(), null);
        i += s.getW();
        for (int j = 0; j < mnbr; j++) {
            g.drawImage(m.getImg(), i, y + offy, m.getW(), m.getH(), null);
            i += m.getW();
        }
        g.drawImage(e.getImg(), i, y + offy, e.getW(), e.getH(), null);

        textInterface.Render(g, offx, offy);
    }
}
