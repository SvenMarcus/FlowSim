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
}
