package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

import java.util.List;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLine extends PaintableShape {

    private final PolyLine polyLine;
    private Point minPoint;
    private double segmentGradient;

    public PaintablePolyLine(PolyLine polyLine) {
        this.polyLine = polyLine;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        List<Point> pointList = polyLine.getPointList();
        for (int i = 0; i < pointList.size() - 1; i++) {
            Point current = transformer.transformToPointOnScreen(pointList.get(i));
            Point next = transformer.transformToPointOnScreen(pointList.get(i + 1));
            painter.paintLine((int) Math.round(current.getX()), (int) Math.round(current.getY()), (int) Math.round(next.getX()), (int) Math.round(next.getY()));
        }
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        List<Point> pointList = polyLine.getPointList();
        for (int i = 0; i < pointList.size() - 1; i++) {
            minPoint = pointList.get(i).getX() < pointList.get(i + 1).getX() ? pointList.get(i) : pointList.get(i + 1);
            segmentGradient = getGradient(i);
            Point intersection = getIntersectionPoint(point, i);
            double dist = getDistance(point, intersection);
            if (dist <= radius)
                return true;
        }
        return false;
    }

    @Override
    public Shape getShape() {
        return polyLine;
    }

    @Override
    public Point getDefinedPoint(Point point, double radius) {
        return null;
    }

    private Point getIntersectionPoint(Point point, int i) {
        double reverseGradient = -1. / segmentGradient;
        List<Point> pointList = polyLine.getPointList();
        double YIntercept = pointList.get(i).getY() - segmentGradient * pointList.get(i).getX();
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
        List<Point> pointList = polyLine.getPointList();
        return (pointList.get(i + 1).getY() - pointList.get(i).getY()) /
                (pointList.get(i + 1).getX() - pointList.get(i).getX());
    }
}
