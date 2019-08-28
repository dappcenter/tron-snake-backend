package de.lj.tronsnakebackend.service.game;

public class Square {

    private String color;
    private int index;

    public Square(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public boolean isBlocked() {
        return color != null;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }
}