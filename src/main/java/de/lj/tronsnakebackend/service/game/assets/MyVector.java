package de.lj.tronsnakebackend.service.game.assets;

public class MyVector {
    private int x;
    private int y;

    public MyVector(int x, int y){
        this.x = x;
        this.y = y;
    }

    // addition of Vectors
    public MyVector plus(MyVector vector) {
        int x = this.x + vector.getX();
        int y = this.y + vector.getY();
        return new MyVector(x, y);
    }

    // substraction of Vectors
    public MyVector minus(MyVector vector) {
        int x = this.x - vector.getX();
        int y = this.y - vector.getY();
        return new MyVector(x, y);
    }

    public MyVector scalarMult(int scalar) {
        return new MyVector(scalar * getX(), scalar * getY());
    }

    // if object can be casted to a Vector then return if it has the same x and y value
    // else it is not a Vector and thus cant be equal
    @Override
    public boolean equals(Object obj) {
        MyVector p;
        try {
            p = (MyVector) obj;
        } catch(ClassCastException ex) {
            return false;
        }
        return p.getX() == this.x && p.getY() == this.y;
    }

    // rotates a Vector around Point(0,0)
    public MyVector rotate(double angle) {
        int x = (int) Math.round(this.x * Math.cos(angle) - this.y * Math.sin(angle));
        int y = (int) Math.round(this.x * Math.sin(angle) + this.y * Math.cos(angle));
        return new MyVector(x, y);
    }

    // rotates a Vector around a given rotation Spot
    public MyVector rotate(double angle, MyVector rotationSpot) {
        MyVector movedToOrigin = this.minus(rotationSpot);
        MyVector rotatedAroundOrigin = movedToOrigin.rotate(angle);

        return this.plus(rotatedAroundOrigin);
    }

    public double length() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    @Override
    public String toString() {
        return "{" + x + ", " + y + "}";
    }

    // Getter
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
