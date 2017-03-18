package irmb.flowsim.model;

/**
 * Created by Sven on 14.12.2016.
 */
public class Point implements Shape {
    private double x;
    private double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
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

    public String toString() {
        return "x: " + x + "; y: " + y;
    }

    @Override
    public void moveBy(double dx, double dy) {
        x += dx;
        y += dy;
    }

    @Override
    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}
