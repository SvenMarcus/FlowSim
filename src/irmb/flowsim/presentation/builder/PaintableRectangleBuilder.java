package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableRectangle;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableRectangleBuilder extends PaintableShapeBuilder {

    private PaintableRectangle rectangle;
    private int pointsAdded;

    public PaintableRectangleBuilder(PaintableFactory factory) {
        super(factory);
        rectangle = (PaintableRectangle) factory.makeShape("Rectangle");
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
    public Paintable getShape() {
        return rectangle;
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
