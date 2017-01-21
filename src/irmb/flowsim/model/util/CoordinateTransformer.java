package irmb.flowsim.model.util;

import irmb.flowsim.model.Point;

/**
 * Created by Sven on 10.01.2017.
 */
public interface CoordinateTransformer {
    Point transformToPointOnScreen(Point point);

    Point transformToWorldPoint(Point point);

    void setWorldBounds(Point topLeft, Point bottomRight);

    void setViewBounds(Point topLeft, Point bottomRight);

    void moveViewWindow(double dx, double dy);

    double scaleToScreenLength(double length);
}
