package com.exiro.depacking;

public class Pixel {

    int r, g, b, a, argb;

    public Pixel(int r, int g, int b, int a, int argb) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.argb = argb;
    }

    public int getArgb() {
        return argb;
    }

    public void setArgb(int argb) {
        this.argb = argb;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
