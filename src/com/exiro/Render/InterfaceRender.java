package com.exiro.Render;

import com.exiro.SystemCore.GameManager;

import javax.swing.*;
import java.awt.*;

public class InterfaceRender extends JPanel {
    private GameManager gm;

    public InterfaceRender(GameManager gm) {
        this.gm = gm;
    }

    public void paintComponent(Graphics g) {
        //  super.paintComponent(g);
        g.setColor(Color.CYAN);
        g.fillRect(0, 0, 320, getHeight());
    }


}
