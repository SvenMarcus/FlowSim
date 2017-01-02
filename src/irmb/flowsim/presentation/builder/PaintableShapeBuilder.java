package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class PaintableShapeBuilder {

    protected final ShapeFactory factory;

    public PaintableShapeBuilder(ShapeFactory factory) {
        this.factory = factory;
    }

    public abstract void addPoint(Point point);

    public abstract PaintableShape getShape();

    public abstract boolean isObjectFinished();

    public abstract void setLastPoint(Point lastPoint);

    public abstract void removeLastPoint();
}
