package com.exiro.render;

import com.exiro.render.layout.*;
import com.exiro.systemCore.GameManager;

import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class GameFrame extends JFrame {


    private GameWindow window;
    private MenuBar menuBar;
    private InterfaceRender it;
    private GameInfo gi;
    private final GameManager gm;

    public static int FHEIGHT = 1080;
    public static int FWIDTH = 1920;

    public GameFrame(String title, GameManager gm) {
        super(title);
        this.gm = gm;
        setSize(FWIDTH, FHEIGHT);


        window = new GameWindow(gm);
        it = new InterfaceRender(gm);
        gi = new GameInfo(gm);

        this.setLayout(new GameLayout());

        this.add(window, GameLayout.GAMEVIEW);
        this.add(it, GameLayout.INTERFACE);
        this.add(gi, GameLayout.TOOLBAR);


        setTitle("Master of Olympus Remake");
        setLocationRelativeTo(null);
        setFocusable(true);
        requestFocus();
        setExtendedState(JFrame.MAXIMIZED_BOTH | JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        it.initGraphics();

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

            }

            @Override
            public void focusLost(FocusEvent e) {
                //        System.out.println("lost");
                requestFocusInWindow();
            }
        });
        requestFocusInWindow();
        addKeyListener(new Keyboard(this));
        addMouseListener(new MouseManager(this));
    }


    public MenuBar getMenu() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public GameWindow getWindow() {
        return window;
    }

    public void setWindow(GameWindow window) {
        this.window = window;
    }

    public InterfaceRender getIt() {
        return it;
    }

    public void setIt(InterfaceRender it) {
        this.it = it;
    }

    public GameInfo getGi() {
        return gi;
    }

    public void setGi(GameInfo gi) {
        this.gi = gi;
    }
}
