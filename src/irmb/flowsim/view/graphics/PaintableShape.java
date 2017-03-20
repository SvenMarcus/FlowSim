package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class PaintableShape implements Paintable {

    public abstract boolean isPointOnBoundary(Point point, double radius);

    public abstract Shape getShape();

    public abstract Point getDefinedPoint(Point point, double radius);

    protected double getDistance(Point first, Point second) {
        double dx = Math.abs(first.getX() - second.getX());
        double dy = Math.abs(first.getY() - second.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    protected double getDistanceToLine(Point start, Point end, Point point) {
        double y2 = end.getY();
        double y1 = start.getY();
        double x0 = point.getX();
        double x2 = end.getX();
        double x1 = start.getX();
        double y0 = point.getY();
        double numerator = Math.abs((x1 - x0) * (y2 - y1) - (x1 - x2) * (y0 - y1));
        double denominator = getDistance(start, end);
        return numerator / denominator;
    }
}
