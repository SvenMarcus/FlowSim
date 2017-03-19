package irmb.flowsim.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 14.12.2016.
 */
public class PolyLine implements Shape {

    private List<Point> pointList = new LinkedList<>();

    public List<Point> getPointList() {
        return pointList;
    }

    public void addPoint(Point point) {
        pointList.add(point);
    }

    public void setLastPoint(Point point) {
        int size = pointList.size();
        if (size > 0) pointList.set(size - 1, point);
    }

    public void removeLastPoint() {
        if (pointList.size() > 0)
            pointList.remove(pointList.size() - 1);
    }


    public void moveBy(double dx, double dy) {
        for (Point p : getPointList()) {
            p.setX(p.getX() + dx);
            p.setY(p.getY() + dy);
        }
    }


    public void accept(ShapeVisitor visitor) {
        visitor.visit(this);
    }
}
