package com.exiro.terrainGenerator;

public class PerlinNoise {


    Grid gradGrid;

    int twoDSpace;
    int twoDSquareSize;

    public PerlinNoise(int size){
        gradGrid = new Grid(size,size);
        gradGrid.generateRandomGrid();
    }


    public int[][] getMap(int size,int resolution,int gradSize, float factor){
        gradGrid = new Grid(gradSize,gradSize);
        gradGrid.generateRandomGrid();
        twoDSquareSize = resolution;
        twoDSpace = size*twoDSquareSize;
        factor *= ((float)twoDSpace)/((float) gradSize);

        int[][] map = new int[size][size];

        float[][] lol = new float[twoDSpace][twoDSpace];
        float max =-100;
        float min = 100;
        for (int i = 0; i < twoDSpace; i++) {
            for (int j = 0; j < twoDSpace; j++) {
                lol[i][j] = (((fractal(((float)i)/factor,((float)j)/factor,100,100,3)+1)/2f));
                if(lol[i][j]>max)
                    max = lol[i][j];
                if(lol[i][j]<min)
                    min = lol[i][j];
            }
        }
        int max2=-100,min2=100;



        for(int l=0;l<twoDSpace/twoDSquareSize;l++) {
            for (int k = 0; k < twoDSpace/twoDSquareSize; k++) {
                int v = 0;
                for (int i = k * twoDSquareSize; i < (k+1)*twoDSquareSize; i++) {
                    for (int j = l * twoDSquareSize; j < (l+1)*twoDSquareSize; j++) {
                        v += (int) ((100) * ((lol[i][j] - min) / max));
                    }
                }
                v/= (twoDSquareSize*twoDSquareSize);
                if(v>max2)
                    max2 = v;
                if(v<min2)
                    min2 = v;

                map[l][k] = v;
            }
        }

        return map;
    }



    public int[][] applyTreshold(int[][] map,int[] threshold){

        int max2=-100,min2=100;
        int[][] newmap = new int[map.length][map[0].length];

        for(int l=0;l<twoDSpace/twoDSquareSize;l++) {
            for (int k = 0; k < twoDSpace / twoDSquareSize; k++) {
                if(map[k][l]>max2)
                    max2 = map[k][l];
                if(map[k][l]<min2)
                    min2 = map[k][l];
            }
        }
        for(int l=0;l<twoDSpace/twoDSquareSize;l++) {
            for (int k = 0; k < twoDSpace / twoDSquareSize; k++) {
                int v = (int) (((map[l][k]-min2)*100f)/max2);
                int res=0;//blue (eau)
                int value = 0;
                for(int thres : threshold){
                    value++;
                    if(v>thres) {
                        res = value;
                    }
                }
                newmap[l][k] = res;
            }
        }
        return newmap;
    }

    public float lerp(float a0,float a1, float w){
        return (1.0f - w)*a0 + w*a1;
    }

    public float dotGridGradient(int ix, int iy, float x, float y) {
        float dx = x - (float)ix;
        float dy = y - (float)iy;
        return (dx*gradGrid.getnodeAt(iy,ix).getX() + dy*gradGrid.getnodeAt(iy,ix).getY());
    }

    public float perlin(float x, float y) {

        // Determine grid cell coordinates
        int x0 = (int) Math.floor(x);
        int x1 = x0 + 1;
        int y0 = (int) Math.floor(y);
        int y1 = y0 + 1;

        // Determine interpolation weights
        // Could also use higher order polynomial/s-curve here
        float sx = x - (float)x0;
        float sy = y - (float)y0;

        // Interpolate between grid point gradients
        float n0, n1, ix0, ix1, value;
        n0 = dotGridGradient(x0, y0, x, y);
        n1 = dotGridGradient(x1, y0, x, y);
        ix0 = lerp(n0, n1, sx);
        n0 = dotGridGradient(x0, y1, x, y);
        n1 = dotGridGradient(x1, y1, x, y);
        ix1 = lerp(n0, n1, sx);
        value = lerp(ix0, ix1, sy);

        return value;
    }

    public float fractal(float x, float y, int freq1,int freq2, int harm){

        float v = 0;
        float a = 1;
        for(int i=1;i<harm+2;i++){
            float f1 = ((float)freq1)/((float)i);
            float f2 = ((float)freq2)/((float)i);
            v+=perlin(x /f1, y /f2) * a;
            a/=2;
        }
        return v;
    }

    float fade(float t) {
        return  (t*t*t*(t*(t*6.0f - 15.0f) + 10.0f));
    }
}
