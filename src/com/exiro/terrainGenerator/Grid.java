package com.exiro.terrainGenerator;

import java.util.ArrayList;
import java.util.Random;

public class Grid {


    ArrayList<Node> nodes;
    int x,y;


    public Grid(int x,int y){
        nodes = new ArrayList<>();
        this.x = x;
        this.y = y;
    }

    public void generateRandomGrid(){
        nodes.clear();
        Random r = new Random();
        for(int i=0;i<x;i++){
            for(int j=0;j<y;j++){
                nodes.add(Node.getNodeBy2Ran(r));
            }
        }
    }

    public Node getnodeAt(int x,int y){
        return nodes.get(x+this.x*y);
    }


}



class Node{

    float x,y;

    public Node(float x,float y) {
        this.x = x;
        this.y = y;
    }
    static Node getNodeByCos(Random r){
        double theta = r.nextFloat()*2*Math.PI;
        float x = (float) Math.cos(theta);
        float y = (float) Math.sin(theta);
        return new Node(x,y);
    }
    static Node getNodeBy2Ran(Random r){
        double a0 = r.nextFloat()*2-1;
        double a1 = r.nextFloat()*2-1;
        double dist = Math.sqrt(a0*a0 + a1*a1);
        float x = (float) (a0/dist);
        float y = (float) (a1/dist);
        return new Node(x,y);
    }

    public float getY() {
        return y;
    }

    public float getX() {
        return x;
    }
}
