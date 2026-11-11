package com.exiro.systemCore;

import com.exiro.object.City;
import com.exiro.object.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The whole game state, without any UI: every player, their cities and the shared calendar.
 * {@link Simulation} advances all of it each tick, whichever city is on screen, so cities keep
 * running while you look at another one (and a future server could run it without a window).
 */
public class World {

    private final List<Player> players = new ArrayList<>();
    private final TimeManager timeManager = new TimeManager(0, 0, 0);

    public void addPlayer(Player p) {
        players.add(p);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    /**
     * Every city of every player.
     */
    public List<City> getCities() {
        List<City> cities = new ArrayList<>();
        for (Player p : players)
            cities.addAll(p.getPlayerCities());
        return cities;
    }

    public TimeManager getTimeManager() {
        return timeManager;
    }
}
