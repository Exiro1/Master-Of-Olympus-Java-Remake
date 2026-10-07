package com.exiro.systemCore;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.locks.LockSupport;

/**
 * Single game loop, replacing the old GameThread + RenderingThread pair.
 *
 * Before, the logic thread mutated the city while the Swing event thread painted it and applied
 * mouse input (building, deleting), so three threads shared the same lists. This loop runs every
 * frame on the Swing event thread instead: first the simulation catches up in fixed steps, then
 * the panels are painted. Input events are handled on that same thread between frames, so all
 * game state is single-threaded and no locking is needed.
 *
 * This thread only paces frames; it never touches game state itself.
 */
public class GameLoop implements Runnable {

    /** Simulation step in seconds. Logic always advances by exactly this amount. */
    public static final double TICK = 1.0 / 60.0;
    /** Upper bound on catch-up steps per frame, so a slow frame can't snowball. */
    private static final int MAX_TICKS_PER_FRAME = 5;
    private static final long FRAME_NANOS = 1_000_000_000L / 144;

    private final GameManager gm;
    private final Simulation simulation;
    private volatile boolean running = true;

    // Touched only on the event thread.
    private long lastFrame = -1;
    private double accumulator = 0;
    private long statsWindowStart;
    private int framesInWindow, ticksInWindow;
    private double fps, tps;

    private final Runnable frame = this::frame;

    public GameLoop(GameManager gm, Simulation simulation) {
        this.gm = gm;
        this.simulation = simulation;
        gm.setGameLoop(this);
    }

    @Override
    public void run() {
        while (running) {
            long start = System.nanoTime();
            try {
                SwingUtilities.invokeAndWait(frame);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            } catch (InvocationTargetException e) {
                // Keep the game running if one frame throws; the old logic thread just died.
                e.getCause().printStackTrace();
            }
            long remaining = FRAME_NANOS - (System.nanoTime() - start);
            if (remaining > 0)
                LockSupport.parkNanos(remaining);
        }
    }

    public void stop() {
        running = false;
    }

    /** One frame, on the Swing event thread. */
    private void frame() {
        long now = System.nanoTime();
        if (lastFrame < 0) {
            lastFrame = now;
            statsWindowStart = now;
        }
        accumulator += (now - lastFrame) / 1e9;
        lastFrame = now;

        int ticks = 0;
        while (accumulator >= TICK && ticks < MAX_TICKS_PER_FRAME) {
            simulation.tick(TICK);
            accumulator -= TICK;
            ticks++;
        }
        if (ticks == MAX_TICKS_PER_FRAME)
            accumulator = 0; // too far behind: drop the backlog instead of spiralling

        paint(gm.getGameView());
        paint(gm.getFrame().getGi());
        paint(gm.getFrame().getIt());

        framesInWindow++;
        ticksInWindow += ticks;
        long window = now - statsWindowStart;
        if (window >= 1_000_000_000L) {
            fps = framesInWindow * 1e9 / window;
            tps = ticksInWindow * 1e9 / window;
            framesInWindow = 0;
            ticksInWindow = 0;
            statsWindowStart = now;
        }
    }

    private static void paint(JComponent c) {
        if (c != null && c.isShowing())
            c.paintImmediately(0, 0, c.getWidth(), c.getHeight());
    }

    /** Frames painted per second. */
    public double getFps() {
        return fps;
    }

    /** Simulation steps per second (60 when the game keeps up). */
    public double getTps() {
        return tps;
    }
}
