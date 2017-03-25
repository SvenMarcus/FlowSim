package irmb.flowsim.view.graphics;

import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;

import java.util.List;

/**
 * Created by sven on 19.03.17.
 */
public class PaintableBezierCurve extends PaintableShape {
    private BezierCurve bezierCurve;
    private Point first;
    private Point second;

    public PaintableBezierCurve(BezierCurve bezierCurve) {
        this.bezierCurve = bezierCurve;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        painter.setColor(Color.BLACK);
        recursivePaint(painter, transformer, bezierCurve.getPointList());

        for (Point point : bezierCurve.getPointList()) {
            Point viewPoint = transformer.transformToPointOnScreen(point);
            painter.paintPoint(viewPoint.getX(), viewPoint.getY());
        }
    }

    private void recursivePaint(Painter painter, CoordinateTransformer transformer, List<Point> pointList) {
        Point first = pointList.get(0);
        Point last = pointList.get(pointList.size() - 1);
        Point viewFirst = transformer.transformToPointOnScreen(first);
        Point viewLast = transformer.transformToPointOnScreen(last);
        if (getDistance(viewFirst, viewLast) <= 5) {
            painter.paintLine(viewFirst.getX(), viewFirst.getY(), viewLast.getX(), viewLast.getY());
        } else {
            List<Point> left, right;
            List<Point> casteljauSublist = bezierCurve.calculateCasteljau(pointList, 0.5);
            left = casteljauSublist.subList(0, casteljauSublist.size() / 2 + 1);
            right = casteljauSublist.subList(casteljauSublist.size() / 2, casteljauSublist.size());
            recursivePaint(painter, transformer, left);
            recursivePaint(painter, transformer, right);
        }
    }


    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        if (getDefinedPoint(point, radius) != null)
            return true;
        int numPoints = 100;
        for (int i = 0; i < numPoints - 1; i++) {
            double t1, t2;
            t1 = i / (double) (numPoints - 1);
            t2 = (i + 1) / (double) (numPoints - 1);
            first = bezierCurve.calculatePointWithBernstein(t1);
            second = bezierCurve.calculatePointWithBernstein(t2);
            double minX = first.getX() < second.getX() ? first.getX() : second.getX();
            double maxX = first.getX() > second.getX() ? first.getX() : second.getX();
            if (point.getX() >= minX && point.getX() <= maxX) {
                double distanceToLine = getDistanceToLine(first, second, point);
                if (distanceToLine <= radius)
                    return true;
            }
        }
        return false;
    }

    @Override
    public Shape getShape() {
        return bezierCurve;
    }

    @Override
    public Point getDefinedPoint(Point point, double radius) {
        List<Point> pointList = bezierCurve.getPointList();
        for (Point p : pointList) {
            if (getDistance(p, point) <= radius)
                return p;
        }
        return null;
    }
}
