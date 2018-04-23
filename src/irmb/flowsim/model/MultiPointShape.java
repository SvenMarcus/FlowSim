package irmb.flowsim.model;

import java.util.List;

/**
 * Created by sven on 27.04.17.
 */
public interface MultiPointShape extends Shape {
    void addPoint(Point point);

    void setLastPoint(Point point);

    void removeLastPoint();

    List<Point> getPointList();
}
