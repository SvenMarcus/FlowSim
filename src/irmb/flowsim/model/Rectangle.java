package irmb.flowsim.model;

/**
 * Created by Sven on 14.12.2016.
 */
public class Rectangle {

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

    protected Point getFirst() {
        return first;
    }

    protected Point getSecond() {
        return second;
    }

}
