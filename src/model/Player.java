package model;

import java.io.Serializable;

public class Player implements Serializable {
    private String name;
    private int position = 0;

    public Player(String name) { this.name = name; }

    public String getName() { return name; }
    public int getPosition() { return position; }
    public void move(int steps) { position += steps; }
    public void setPosition(int position) {
        this.position = position;}
}
