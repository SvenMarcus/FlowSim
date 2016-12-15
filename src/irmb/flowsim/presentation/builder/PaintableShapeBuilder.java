package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class PaintableShapeBuilder {

    protected final PaintableFactory factory;

    public PaintableShapeBuilder(PaintableFactory factory) {
        this.factory = factory;
    }

    public abstract void addPoint(Point point);

    public abstract Paintable getShape();

    public abstract boolean isObjectFinished();
}
