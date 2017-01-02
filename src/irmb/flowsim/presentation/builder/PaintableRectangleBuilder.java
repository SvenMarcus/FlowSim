package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableRectangle;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableRectangleBuilder extends PaintableShapeBuilder {

    private Rectangle rectangle;
    private int pointsAdded;
    private PaintableShape paintable;

    public PaintableRectangleBuilder(ShapeFactory factory) {
        super(factory);
        rectangle = (Rectangle) factory.makeShape("Rectangle");
    }

    @Override
    public void addPoint(Point point) {
        if (pointsAdded == 0)
            rectangle.setFirst(point);
        else if (pointsAdded == 1)
            rectangle.setSecond(point);
        pointsAdded++;
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = new PaintableRectangle(rectangle);
        return paintable;
    }

    @Override
    public boolean isObjectFinished() {
        return pointsAdded >= 2;
    }

    @Override
    public void setLastPoint(Point point) {
        if (pointsAdded == 1) rectangle.setFirst(point);
        else if (pointsAdded >= 2) rectangle.setSecond(point);
    }

    @Override
    public void removeLastPoint() {

    }
}
