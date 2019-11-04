package de.lj.tronsnakebackend.service.game;

public class Square {

    private String color;
    private boolean blocked;
    private int index;

    public Square(int index) {
        this.index = index;
        this.color = "white";
    }

    public int getIndex() {
        return index;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}