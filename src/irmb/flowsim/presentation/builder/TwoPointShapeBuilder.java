package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.*;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintableRectangle;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by sven on 18.03.17.
 */
public class TwoPointShapeBuilder extends PaintableShapeBuilder {


    private final TwoPointShape shape;
    private final String type;
    private int pointsAdded;

    public TwoPointShapeBuilder(ShapeFactory factory, String type) {
        super(factory);
        this.type = type;
        this.shape = (TwoPointShape) factory.makeShape(type);
    }

    @Override
    public void addPoint(Point point) {
        if (pointsAdded == 0)
            shape.setFirst(point);
        else if (pointsAdded == 1)
            shape.setSecond(point);
        pointsAdded++;
    }

    @Override
    public PaintableShape getShape() {
        if (type.equals("Line"))
            return new PaintableLine((Line) shape);
        else return new PaintableRectangle((Rectangle) shape);
    }

    @Override
    public boolean isObjectFinished() {
        return pointsAdded == 2;
    }

    @Override
    public void setLastPoint(Point lastPoint) {
        if (pointsAdded == 1)
            shape.setFirst(lastPoint);
        else
            shape.setSecond(lastPoint);
    }

    @Override
    public void removeLastPoint() {
        if (pointsAdded == 1) {
            shape.setFirst(null);
            pointsAdded--;
        } else if (pointsAdded == 2) {
            shape.setSecond(null);
            pointsAdded--;
        }
    }
}
