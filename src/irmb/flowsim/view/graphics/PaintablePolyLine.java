package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLine extends PolyLine implements Paintable {

    private Point minPoint;
    private double segmentGradient;

    public PaintablePolyLine() {
    }

    @Override
    public void paint(Painter painter) {
        for (int i = 0; i < getPointList().size() - 1; i++) {
            Point current = getPointList().get(i);
            Point next = getPointList().get(i + 1);
            painter.paintLine(current.getX(), current.getY(), next.getX(), next.getY());
        }
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        for (int i = 0; i < getPointList().size() - 1; i++) {
            minPoint = getPointList().get(i).getX() < getPointList().get(i + 1).getX() ? getPointList().get(i) : getPointList().get(i + 1);
            segmentGradient = getGradient(i);
            Point intersection = getIntersectionPoint(point, i);
            double dist = getDistance(point, intersection);
            if (dist <= radius)
                return true;
        }
        return false;
    }

    @Override
    public void moveBy(double dx, double dy) {
        for (Point p : getPointList()) {
            p.setX(p.getX() + dx);
            p.setY(p.getY() + dy);
        }
    }

    private Point getIntersectionPoint(Point point, int i) {
        double reverseGradient = -1. / segmentGradient;
        double YIntercept = getPointList().get(i).getY() - segmentGradient * getPointList().get(i).getX();
        double reverseYIntercept = point.getY() - reverseGradient * point.getX();
        double intersectX = (reverseYIntercept - YIntercept) / (segmentGradient + 1 / segmentGradient);
        double intersectY = getYCoord(intersectX);
        return new Point(intersectX, intersectY);
    }

    private double getDistance(Point point, Point pointOnLine) {
        double dx = Math.abs(point.getX() - pointOnLine.getX());
        double dy = Math.abs(point.getY() - pointOnLine.getY());
        return Math.sqrt(dx * dx + dy * dy);
    }

    private double getYCoord(double x) {
        double dx = getDeltaXToLineStart(x);
        return minPoint.getY() + dx * segmentGradient;
    }

    private double getDeltaXToLineStart(double x) {
        return Math.abs(minPoint.getX() - x);
    }

    private double getGradient(int i) {
        return (getPointList().get(i + 1).getY() - getPointList().get(i).getY()) /
                (getPointList().get(i + 1).getX() - getPointList().get(i).getX());
    }
}
