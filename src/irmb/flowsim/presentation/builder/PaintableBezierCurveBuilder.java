package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableBezierCurve;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by sven on 19.03.17.
 */
public class PaintableBezierCurveBuilder extends PaintableShapeBuilder {
    private final BezierCurve bezierCurve;
    private PaintableBezierCurve paintable;

    public PaintableBezierCurveBuilder(ShapeFactory factory) {
        super(factory);
        bezierCurve = (BezierCurve) factory.makeShape("Bezier");
    }

    @Override
    public void addPoint(Point point) {
        bezierCurve.addPoint(point);
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = new PaintableBezierCurve(bezierCurve);
        return paintable;
    }

    @Override
    public boolean isObjectFinished() {
        return false;
    }

    @Override
    public void setLastPoint(Point point) {
        bezierCurve.setLastPoint(point);
    }

    @Override
    public void removeLastPoint() {
        bezierCurve.removeLastPoint();
    }

}
