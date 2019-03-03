package edu.ucsd.cse110.dogegotchi.sprite;

/**
 * Represents a 2D Euclidean coordinate.
 */
public class Coord {
    private final int x;

    private final int y;

    public Coord(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
