package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableLine extends PaintableShape {


    private Point maxPoint;
    private Point minPoint;

    private Line line;

    public PaintableLine(Line line) {
        this.line = line;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        Point start = transformer.transformToPointOnScreen(line.getFirst());
        Point end = transformer.transformToPointOnScreen(line.getSecond());
        painter.paintLine(start.getX(), start.getY(), end.getX(), end.getY());
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        maxPoint = line.getFirst().getX() < line.getSecond().getX() ? line.getSecond() : line.getFirst();
        minPoint = line.getFirst().getX() < line.getSecond().getX() ? line.getFirst() : line.getSecond();

        if (point.getX() > maxPoint.getX())
            return getDistance(point, maxPoint) <= radius;

        Point intersection = getIntersectionPoint(point);
        double dist = getDistance(point, intersection);
        return dist <= radius;
    }

    @Override
    public Shape getShape() {
        return line;
    }

    private Point getIntersectionPoint(Point point) {
        double gradient = getGradient();
        double reverseGradient = -1. / gradient;
        double YIntercept = line.getFirst().getY() - gradient * line.getFirst().getX();
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
        return (line.getSecond().getY() - line.getFirst().getY()) /
                (line.getSecond().getX() - line.getFirst().getX());
    }

}
