package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableRectangle extends PaintableShape {

    private double maxX;
    private double minX;
    private double maxY;
    private double minY;
    private Rectangle rectangle;


    public PaintableRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        Point first = rectangle.getFirst();
        Point second = rectangle.getSecond();

        minX = first.getX() < second.getX() ? first.getX() : second.getX();
        maxY = first.getY() > second.getY() ? first.getY() : second.getY();

        double width = Math.abs(first.getX() - second.getX());
        double height = Math.abs(first.getY() - second.getY());

        Point topLeftView = transformer.transformToPointOnScreen(new Point(minX, maxY));

        int widthView = (int) Math.round(transformer.scaleToScreenLength(width));
        int heightView = (int) Math.round(transformer.scaleToScreenLength(height));

        painter.paintRectangle((int) Math.round(topLeftView.getX()), (int) Math.round(topLeftView.getY()), widthView, heightView);
    }

    @Override
    public boolean isPointOnBoundary(Point point, double radius) {
        if (rectangle.getFirst().getX() < rectangle.getSecond().getX()) {
            maxX = rectangle.getSecond().getX();
            minX = rectangle.getFirst().getX();
        } else {
            maxX = rectangle.getFirst().getX();
            minX = rectangle.getSecond().getX();
        }
        if (rectangle.getFirst().getY() < rectangle.getSecond().getY()) {
            maxY = rectangle.getSecond().getY();
            minY = rectangle.getFirst().getY();
        } else {
            maxY = rectangle.getFirst().getY();
            minY = rectangle.getSecond().getY();
        }
        return isInBoundingBox(point, radius) && !isInside(point, radius);
    }

    @Override
    public Shape getShape() {
        return rectangle;
    }

    @Override
    public Point getDefinedPoint(Point point, double radius) {
        return null;
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
