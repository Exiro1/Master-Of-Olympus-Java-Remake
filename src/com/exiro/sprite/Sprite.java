package com.exiro.sprite;

import com.exiro.object.City;
import com.exiro.object.ObjectClass;
import com.exiro.render.IsometricRender;
import com.exiro.utils.Point;

import java.awt.*;
import java.awt.image.*;

public abstract class Sprite extends ObjectClass {


    int frameNumber;
    int width, height;
    float x;
    float y;
    float x1, y1;
    int offsetX, offsetY;
    int index = 0;
    double timeSinceLastFrame = 0;
    BufferedImage currentFrame;
    double timeBetweenFrame = 0.2f;
    City c;

    public Sprite(String filePath, int bitID, int localId, int frameNumber, City c) {
        super(true, null, filePath, 1, bitID, localId);
        this.c = c;
        this.frameNumber = frameNumber;
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


    public abstract void process(double deltaTime);

    @Override
    public void Render(Graphics g, int camX, int camY) {
        Point p = IsometricRender.TwoDToIsoTexture(new Point(getX(), (getY())), getWidth(), getHeight(), 1);
        g.drawImage(getCurrentFrame(), camX + (int) p.getX() + getOffsetX(), camY + (int) p.getY() + getOffsetY(), null);
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
