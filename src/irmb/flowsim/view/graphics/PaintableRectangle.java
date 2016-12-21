package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableRectangle extends Rectangle implements Paintable {

    private double maxX;
    private double minX;
    private double maxY;
    private double minY;


    public PaintableRectangle() {
    }

    @Override
    public void paint(Painter painter) {
        double minX = getFirst().getX() < getSecond().getX() ? getFirst().getX() : getSecond().getX();
        double minY = getFirst().getY() < getSecond().getY() ? getFirst().getY() : getSecond().getY();
        double width = Math.abs(getFirst().getX() - getSecond().getX());
        double height = Math.abs(getFirst().getY() - getSecond().getY());
        painter.paintRectangle(minX, minY, width, height);
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        if (getFirst().getX() < getSecond().getX()) {
            maxX = getSecond().getX();
            minX = getFirst().getX();
        } else {
            maxX = getFirst().getX();
            minX = getSecond().getX();
        }
        if (getFirst().getY() < getSecond().getY()) {
            maxY = getSecond().getY();
            minY = getFirst().getY();
        } else {
            maxY = getFirst().getY();
            minY = getSecond().getY();
        }
        return isInBoundingBox(point, radius) && !isInside(point, radius);
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

    private boolean isInside(Point point, double radius) {
        return point.getY() > minY + radius && point.getY() < maxY - radius && point.getX() > minX + radius && point.getX() < maxX - radius;
    }

    private boolean isInBoundingBox(Point point, double radius) {
        return isInXBounds(point, radius) && isInYBounds(point, radius);
    }

    private boolean isInYBounds(Point point, double radius) {
        return point.getY() >= minY - radius && point.getY() <= maxY + radius;
    }

    private boolean isInXBounds(Point point, double radius) {
        return point.getX() >= minX - radius && point.getX() <= maxX + radius;
    }
}
