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
        painter.paintLine(Math.round(start.getX()), Math.round(start.getY()), Math.round(end.getX()), Math.round(end.getY()));
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        double maxX = line.getFirst().getX() < line.getSecond().getX() ? line.getSecond().getX() : line.getFirst().getX();
        double minX = line.getFirst().getX() < line.getSecond().getX() ? line.getFirst().getX() : line.getSecond().getX();
        if (point.getX() >= minX - radius && point.getX() <= maxX + radius)
            return getDistanceToLine(line.getFirst(), line.getSecond(), point) <= radius;
        return false;
    }


    @Override
    public Shape getShape() {
        return line;
    }

    @Override
    public Point getDefinedPoint(Point point, double radius) {
        Point first = line.getFirst();
        Point second = line.getSecond();
        if (getDistance(point, first) <= radius)
            return first;
        else if (getDistance(point, second) <= radius)
            return second;
        return null;
    }
}
