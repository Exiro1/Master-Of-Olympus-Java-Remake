package com.exiro.sprite;

import com.exiro.object.Case;
import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.render.IsometricRender;
import com.exiro.render.interfaceList.Interface;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;

public abstract class Sprite extends ObjectClass {


    final boolean DEBUG = false;

    static int speedFactor;
    protected int frameNumber;
    protected int width, height;
    protected float x;
    protected float y;
    protected float x1, y1;
    protected int offsetX, offsetY;
    protected int baseh, basew;
    protected int index = 0;
    protected double timeSinceLastFrame = 0;
    protected BufferedImage currentFrame;
    protected double timeBetweenFrame = 0.2f;
    protected City c;
    protected boolean complex = false;
    Sticking sticking = null;
    ArrayList<Case> onWalking = new ArrayList<>();

    public Sprite(String filePath, int bitID, int localId, int frameNumber, City c, Sticking sticking) {
        super(true, null, filePath, 1, bitID, localId, 1, 1);
        this.c = c;
        this.frameNumber = frameNumber;
        this.sticking = sticking;
    }

    @Override
    public Interface getInterface() {
        return null;
    }

    public static BufferedImage makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public final int markerRGB = color.getRGB() | 0xFF000000;

            public final int filterRGB(int x, int y, int rgb) {
                if ((rgb | 0xFF000000) == markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x33000000;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(im.getSource(), filter);
        return toBufferedImage(Toolkit.getDefaultToolkit().createImage(ip));
    }


    public static BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), Transparency.TRANSLUCENT);
        bimage.getGraphics().drawImage(img, 0, 0, null);

        // Return the buffered image
        return bimage;
    }

    int size = 2;

    public Sprite(String filePath, int bitID, int localId, int frameNumber, City c) {
        super(true, null, filePath, 1, bitID, localId, 1, 1);
        this.c = c;
        this.frameNumber = frameNumber;
        baseh = getHeight();
        basew = getWidth();

    }

    @Override
    public void setMainCase(Case mainCase) {
        if (mainCase == this.mainCase)
            return;
        if (onWalking.size() >= size) {
            onWalking.get(0).getSprites().remove(this);
            onWalking.remove(0);
        }
        onWalking.add(mainCase);
        this.mainCase = mainCase;
        mainCase.getSprites().add(this);
    }

    public abstract void process(double deltaTime);

    @Override
    public void Render(Graphics g, int camX, int camY) {
        int z = 1;
        if (c.getMap().getCase(getXB(), getYB()) != null)
            z = c.getMap().getCase(getXB(), getYB()).getZlvl();


        //Point p = IsometricRender.TwoDToIsoTexture(new Point(getX() - z, (getY()) - z), getWidth(), getHeight(),1);
        if (complex) {
            Point p = IsometricRender.TwoDToIsoSprite(new Point(getX() - z, (getY()) - z), getWidth(), getHeight());
            if (DEBUG) {
                g.setColor(Color.RED);
                g.drawRect(camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), getWidth(), getHeight());
                g.drawString(getXB() + " " + getYB() + " " + getX() + " " + getY(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy());
                g.setColor(Color.BLACK);
            }
            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX() - getOffx(), camY + (int) p.getY() + getOffsetY() - getOffy(), null);
        } else {
            Point p = IsometricRender.TwoDToIsoSprite(new Point(getX() - z, (getY()) - z), getWidth(), getHeight());
            if (DEBUG) {
                g.setColor(Color.RED);
                g.drawRect(camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), getWidth(), getHeight());
                g.drawString(getXB() + " " + getYB(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY());
                g.setColor(Color.BLACK);
            }
            g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), null);
        }


    }

    @Override
    public ArrayList<Case> getPlace(int xPos, int yPos, int yLenght, int xLenght, City city) {
        return null;
    }

    public double getTimeBetweenFrame() {
        return timeBetweenFrame;
    }

    public void setTimeBetweenFrame(double timeBetweenFrame) {
        this.timeBetweenFrame = timeBetweenFrame;
    }

    public int getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
    }

    public void setOffsetX(int offsetX) {
        this.offsetX = offsetX;
    }

    public void setOffsetY(int offsetY) {
        this.offsetY = offsetY;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public double getTimeSinceLastFrame() {
        return timeSinceLastFrame;
    }

    public boolean isComplex() {
        return complex;
    }

    public void setComplex(boolean complex) {
        this.complex = complex;
    }

    public void setTimeSinceLastFrame(double timeSinceLastFrame) {
        this.timeSinceLastFrame = timeSinceLastFrame;
    }

    public City getC() {
        return c;
    }

    public void setC(City c) {
        this.c = c;
    }

    public int getOffsetX() {
        return offsetX;
    }

    public int getOffsetY() {
        return offsetY;
    }

    public BufferedImage getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(BufferedImage currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }


    public void setX1(float x1) {
        this.x1 = x1;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }


    public double getX1() {
        return x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

}
