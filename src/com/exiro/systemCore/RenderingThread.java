package com.exiro.systemCore;

import com.exiro.render.GameFrame;

public class RenderingThread implements Runnable{

    boolean continu = true;

    final long deltaTimeResearched = (long) ((1f / 144f) * 1000f);

    private final GameFrame frame;
    private final GameManager gm;
    private double deltaTime;
    private double fps;

    public RenderingThread(GameManager gm) {
        this.frame = gm.frame;
        this.gm = gm;
        gm.setRenderingThread(this);
    }


    @Override
    public void run() {
        while (continu) {
            long startTime = System.currentTimeMillis();

            gm.GameView.repaint();
            gm.frame.getGi().repaint();
            gm.frame.getIt().repaint();
            float toWait = System.currentTimeMillis() - startTime;

            try {
                Thread.sleep(Math.max(deltaTimeResearched - (int) toWait, 0));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            float a = System.currentTimeMillis() - startTime;
            deltaTime = Math.min(a / 1000.0f, deltaTimeResearched / 1000.0f);

            fps = 1f/(a / 1000.0f);
        }
    }

    public double getFps() {
        return fps;
    }
}
