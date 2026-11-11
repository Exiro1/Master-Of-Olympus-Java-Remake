package com.exiro.systemCore;

import com.exiro.object.City;
import com.exiro.object.Player;
import com.exiro.render.GameFrame;
import com.exiro.render.layout.GameInfo;
import com.exiro.render.layout.GameWindow;
import com.exiro.render.layout.InterfaceRender;
import com.exiro.render.layout.MenuBar;
import com.exiro.soundManager.SoundManager;

/**
 * The local game client: the window, the local player and the city on screen.
 * Game state lives in {@link World}; objects reach it through their own {@link City}, never
 * through this class, so nothing depends on which city is being viewed.
 */
public class GameManager {

    private static GameManager gm;
    private final World world;
    // the city shown on screen, see setCurrentCity
    private City currentCity;
    GameFrame frame;
    GameWindow GameView;
    MenuBar menuBar;
    Player player;
    private final InterfaceRender it;
    private final GameInfo gi;
    Simulation simulation;
    GameLoop gameLoop;

    public GameManager(World world, Player player, City currentCity) {
        this.world = world;
        this.player = player;
        this.currentCity = currentCity;
        gm = this;
        frame = new GameFrame("Master of Olympus", this);
        this.GameView = frame.getWindow();
        this.menuBar = frame.getMenu();
        this.gi = frame.getGi();
        this.it = frame.getIt();
    }

    public World getWorld() {
        return world;
    }

    public GameLoop getGameLoop() {
        return gameLoop;
    }

    public void setGameLoop(GameLoop gameLoop) {
        this.gameLoop = gameLoop;
    }

    public Simulation getSimulation() {
        return simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public static GameManager getInstance() {
        return gm;
    }


    public TimeManager getTimeManager() {
        return world.getTimeManager();
    }

    public GameFrame getFrame() {
        return frame;
    }

    public void setFrame(GameFrame frame) {
        this.frame = frame;
    }

    public GameWindow getGameView() {
        return GameView;
    }

    public void setGameView(GameWindow gameView) {
        GameView = gameView;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    public void setMenuBar(MenuBar menuBar) {
        this.menuBar = menuBar;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public City getCurrentCity() {
        return currentCity;
    }

    /**
     * Shows another city. Every city keeps being simulated; this only changes what the window
     * displays and where the build tool acts.
     */
    public void setCurrentCity(City city) {
        if (city == null || city == currentCity)
            return;
        currentCity = city;
        if (GameView != null)
            GameView.cityChanged();
    }

    /**
     * Shows the local player's next city (wraps around).
     */
    public void nextCity() {
        java.util.List<City> cities = player.getPlayerCities();
        if (cities.isEmpty())
            return;
        int i = cities.indexOf(currentCity);
        setCurrentCity(cities.get((i + 1) % cities.size()));
    }
}
