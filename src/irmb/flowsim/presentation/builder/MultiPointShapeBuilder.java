package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.MultiPointShape;
import irmb.flowsim.presentation.factory.PaintableShapeFactory;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by sven on 21.03.17.
 */
public class MultiPointShapeBuilder extends PaintableShapeBuilder {


    private final MultiPointShape shape;
    private final PaintableShapeFactory paintableShapeFactory;
    private PaintableShape paintable;

    public MultiPointShapeBuilder(MultiPointShape shape, PaintableShapeFactory paintableShapeFactory) {
        this.shape = shape;
        this.paintableShapeFactory = paintableShapeFactory;
    }

    @Override
    public void addPoint(Point point) {
        shape.addPoint(point);
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = paintableShapeFactory.makePaintableShape(shape);
        return paintable;
    }

    @Override
    public boolean isObjectFinished() {
        return false;
    }

    @Override
    public void setLastPoint(Point point) {
        shape.setLastPoint(point);
    }

    @Override
    public void removeLastPoint() {
        shape.removeLastPoint();
    }

    @Override
    public boolean isObjectPaintable() {
        return shape.getPointList().size() >= 2;
    }
}
