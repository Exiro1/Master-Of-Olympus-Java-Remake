package com.exiro.Sprite;

import com.exiro.MoveRelated.Path;
import com.exiro.Object.City;
import com.exiro.Object.ObjectClass;
import com.exiro.Utils.Point;

import java.awt.*;
import java.awt.image.*;
import java.util.Map;

public abstract class Sprite extends ObjectClass {


    public boolean hasArrived = false;
    Direction dir = Direction.EST;
    int frameNumber;
    int width, height, size;
    int marge;
    double x, y;
    double x1, y1;
    int offsetX,offsetY;
    Path path;
    int index = 0;
    double timeSinceLastFrame = 0;
    BufferedImage currentFrame;
    City c;
    ObjectClass destination;

    public Sprite(String filePath, int size, int frameNumber, int widht, int height, int marge, City c, ObjectClass destination) {
        super(true, null, filePath, widht, height, 1);
        this.marge = marge;
        this.c = c;
        this.destination = destination;
        this.size = size;
        this.frameNumber = frameNumber;
        this.height = height;
        this.width = widht;
    }

    public static BufferedImage makeColorTransparent(BufferedImage im, final Color color) {
        ImageFilter filter = new RGBImageFilter() {
            // the color we are looking for... Alpha bits are set to opaque
            public int markerRGB = color.getRGB() | 0xFF000000;

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

        // Create a buffered image with transparency
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // Draw the image on to the buffered image
        Graphics2D bGr = bimage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        // Return the buffered image
        return bimage;
    }

    public void process(double deltaTime) {
        timeSinceLastFrame = timeSinceLastFrame + deltaTime;
        // System.out.println(timeSinceLastFrame);
        if (timeSinceLastFrame > 0.2f) {
            index++;
            timeSinceLastFrame = 0;
            if (index >= frameNumber) {
                index = 0;

            }
            currentFrame = getSpriteSet().get(dir)[index];

        }
        if ((path.getIndex() < path.getPath().size() - 1 && path.isOnCase(new Point(getX(), getY()), dir)) || (dir == Direction.EST && path.getIndex() < path.getPath().size() - 1)) {
            dir = path.next();
        } else if (path.getIndex() == path.getPath().size() - 1) {
            hasArrived = true;
        }
        if (dir == Direction.SUD_OUEST) {
            y = y + 1 * deltaTime;
        } else if (dir == Direction.NORD_OUEST) {
            x = x - 1 * deltaTime;
        } else if (dir == Direction.NORD_EST) {
            y = y - 1 * deltaTime;
        } else if (dir == Direction.SUD_EST) {
            x = x + 1 * deltaTime;
        }


    }


    abstract public Map<Direction, BufferedImage[]> getSpriteSet();

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


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }


    public void setX1(float x1) {
        this.x1 = x1;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(float y1) {
        this.y1 = y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
    }
}
