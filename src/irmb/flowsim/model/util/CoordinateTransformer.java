package irmb.flowsim.model.util;

import irmb.flowsim.model.Point;

/**
 * Created by Sven on 10.01.2017.
 */
public interface CoordinateTransformer {
    Point transformToPointOnScreen(Point point);

    Point transformToWorldPoint(Point point);

    void setWorldBounds(double minX, double maxX, double minY, double maxY);

    void setViewBounds(double minX, double maxX, double minY, double maxY);
}
