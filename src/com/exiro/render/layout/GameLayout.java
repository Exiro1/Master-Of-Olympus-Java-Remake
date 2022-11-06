package com.exiro.render.layout;

import com.exiro.render.GameFrame;

import java.awt.*;
import java.util.ArrayList;

public class GameLayout implements LayoutManager {

    public static final String TOOLBAR = "TOOLBAR";
    public static final String GAMEVIEW = "GAMEVIEW";
    public static final String INTERFACE = "INTERFACE";
    public static final int TOOLBAR_HEIGHT = 42;
    private final ArrayList<Component> toolbarComponents = new ArrayList<>();
    private final ArrayList<Component> GameComponents = new ArrayList<>();
    private final ArrayList<Component> InterfaceComponents = new ArrayList<>();

    @Override   // Called when JFrame.add() is used  //
    public void addLayoutComponent(String constraints, Component component) {

        switch (constraints) {
            case "TOOLBAR":
                toolbarComponents.add(component);
                break;
            case "GAMEVIEW":
                GameComponents.add(component);
                break;
            case "INTERFACE":
                InterfaceComponents.add(component);
                break;
        }
    }


    @Override   //  Sets the bounds for each component  //
    public void layoutContainer(Container parent) {
        //  Toolbar  //
        int currentWidth = 0;
        for (Component c : toolbarComponents) {
            int width = GameFrame.scalingW(1600);
            c.setBounds(currentWidth, 0, width, GameFrame.scalingH(TOOLBAR_HEIGHT));
            currentWidth = width + currentWidth;
        }

        //  Body  //
        int currentHeight = GameFrame.scalingH(TOOLBAR_HEIGHT);
        for (Component c : GameComponents) {
            int height = (int) ((1080 - TOOLBAR_HEIGHT)* GameFrame.FHRATIO);

            c.setBounds(0, currentHeight, GameFrame.scalingW(1600), height);
            currentHeight += height;
        }

        for (Component c : InterfaceComponents) {
            //int height = GameFrame.scalingW(1600);
            c.setBounds(GameFrame.scalingW(1600), 0,  GameFrame.scalingH(1920 - 1600), GameFrame.scalingH(1080));
            //currentHeight += height;
        }

    }


    @Override
    public Dimension minimumLayoutSize(Container parent) {
        return preferredLayoutSize(parent);
    }


    @Override   //  Called when JFrame.pack() is called  //
    public Dimension preferredLayoutSize(Container parent) {
        return new Dimension(1920, 1080);
    }


    @Override
    public void removeLayoutComponent(Component comp) {
    }

}
