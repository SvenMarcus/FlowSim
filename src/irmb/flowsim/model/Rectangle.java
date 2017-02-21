package irmb.flowsim.model;

/**
 * Created by Sven on 14.12.2016.
 */
public class Rectangle implements Shape {

    private Point first;
    private Point second;

    public Rectangle() {
    }

    public void setFirst(Point first) {
        this.first = first;
    }

    public void setSecond(Point second) {
        this.second = second;
    }

    public Point getFirst() {
        return first;
    }

    public Point getSecond() {
        return second;
    }

    @Override
    public void moveBy(double dx, double dy) {
        first.setX(first.getX() + dx);
        first.setY(first.getY() + dy);
        second.setX(second.getX() + dx);
        second.setY(second.getY() + dy);
    }
}
