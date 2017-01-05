package irmb.flowsim.model;

/**
 * Created by Sven on 14.12.2016.
 */
public class Line implements Shape {

    private Point first;
    private Point second;

    public Line() {
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
        double firstX = getFirst().getX() + dx;
        double firstY = getFirst().getY() + dy;
        double secondX = getSecond().getX() + dx;
        double secondY = getSecond().getY() + dy;
        setFirst(new Point(firstX, firstY));
        setSecond(new Point(secondX, secondY));
    }
}
