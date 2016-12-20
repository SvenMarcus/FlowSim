package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableLine extends Line implements Paintable {


    private Point maxPoint;
    private Point minPoint;


    public PaintableLine() {
    }

    @Override
    public void paint(Painter painter) {
        painter.paintLine(getFirst().getX(), getFirst().getY(), getSecond().getX(), getSecond().getY());
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        maxPoint = getFirst().getX() < getSecond().getX() ? getSecond() : getFirst();
        minPoint = getFirst().getX() < getSecond().getX() ? getFirst() : getSecond();

        if (point.getX() > maxPoint.getX())
            return getDistance(point, maxPoint) <= radius;

        Point intersection = getIntersectionPoint(point);
        double dist = getDistance(point, intersection);
        return dist <= radius;
    }

    private Point getIntersectionPoint(Point point) {
        double gradient = getGradient();
        double reverseGradient = -1. / gradient;
        double YIntercept = getFirst().getY() - gradient * getFirst().getX();
        double reverseYIntercept = point.getY() - reverseGradient * point.getX();
        double intersectX = (reverseYIntercept - YIntercept) / (gradient + 1 / gradient);
        double intersectY = getYCoord(intersectX);
        return new Point(intersectX, intersectY);
    }

    private double getDistance(Point point, Point pointOnLine) {
        double dx = Math.abs(point.getX() - pointOnLine.getX());
        double dy = Math.abs(point.getY() - pointOnLine.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double getYCoord(double x) {
        double gradient = getGradient();
        double dx = getDeltaXToLineStart(x);
        return minPoint.getY() + dx * gradient;
    }

    private double getDeltaXToLineStart(double x) {
        return Math.abs(minPoint.getX() - x);
    }

    private double getGradient() {
        return (getSecond().getY() - getFirst().getY()) /
                (getSecond().getX() - getFirst().getX());
    }
}
