package com.exiro.moveRelated;

import com.exiro.object.Case;
import com.exiro.sprite.Direction;
import com.exiro.utils.Point;

import java.util.ArrayList;

public class Path {

    Point limit;
    private ArrayList<Case> path;
    private Case start, end;
    private int Free;
    private int index = 0;

    public Path(ArrayList<Case> path) {
        this.path = path;

    }

    public Path() {
    }

    public Path getReversePath() {
        ArrayList<Case> reverse = new ArrayList<>();
        for (int i = path.size() - 1; i >= 0; i--) {
            reverse.add(path.get(i));
        }
        System.out.println(reverse);
        System.out.println(path.toString());
        return new Path(reverse);
    }

    public Direction next() {
        if (path.size() == 1) {
            return Direction.SOUTH_WEST;
        }

        Case last = path.get(index);
        index++;
        Case next = path.get(index);
        int x = last.getxPos() - next.getxPos();
        int y = last.getyPos() - next.getyPos();

        if (x == -1 && y == 0) {
            return Direction.SOUTH_EAST;
        } else if (x == 1 && y == 0) {
            return Direction.NORTH_WEST;
        } else if (x == 0 && y == 1) {
            return Direction.NORTH_EAST;
        } else if (x == 0 && y == -1) {
            return Direction.SOUTH_WEST;
        } else if (x == 1 && y == 1) {
            return Direction.NORTH;
        } else if (x == -1 && y == -1) {
            return Direction.SOUTH;
        } else if (x == -1 && y == +1) {
            return Direction.EAST;
        } else if (x == +1 && y == -1) {
            return Direction.WEST;
        }
        return Direction.EAST;
    }


    public boolean isOnCase(Point p, Direction direction) {
        if (direction == Direction.SOUTH_EAST && p.getX() > path.get(index).getxPos()) {
            return true;
        } else if (direction == Direction.NORTH_WEST && p.getX() < path.get(index).getxPos()) {
            return true;
        } else if (direction == Direction.NORTH_EAST && p.getY() < path.get(index).getyPos()) {
            return true;
        } else if (direction == Direction.SOUTH_WEST && p.getY() > path.get(index).getyPos()) {
            return true;
        } else if (direction == Direction.NORTH && p.getY() < path.get(index).getyPos() && p.getX() < path.get(index).getxPos()) {
            return true;
        } else if (direction == Direction.SOUTH && p.getY() > path.get(index).getyPos() && p.getX() > path.get(index).getxPos()) {
            return true;
        } else if (direction == Direction.EAST && p.getY() < path.get(index).getyPos() && p.getX() > path.get(index).getxPos()) {
            return true;
        } else if (direction == Direction.WEST && p.getY() > path.get(index).getyPos() && p.getX() < path.get(index).getxPos()) {
            return true;
        }
        return false;
    }


    public int getFree() {
        return Free;
    }

    public void setFree(int free) {
        Free = free;
    }

    public ArrayList<Case> getPath() {
        return path;
    }

    public void setPath(ArrayList<Case> path) {
        this.path = path;
    }

    public Case getStart() {
        return start;
    }

    public void setStart(Case start) {
        this.start = start;
    }

    public Case getEnd() {
        return end;
    }

    public void setEnd(Case end) {
        this.end = end;
    }

    public void addCases(Case c) {
        path.add(c);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Point getLimit() {
        return limit;
    }

    public void setLimit(Point limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Path{" +
                "path=" + path +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
