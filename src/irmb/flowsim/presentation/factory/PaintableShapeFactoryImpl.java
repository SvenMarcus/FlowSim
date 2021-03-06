package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.*;
import irmb.flowsim.view.graphics.*;

/**
 * Created by sven on 18.03.17.
 */
public class PaintableShapeFactoryImpl implements PaintableShapeFactory {
    @Override
    public PaintableShape makePaintableShape(Shape shape) {
        if (shape instanceof Line)
            return new PaintableLine((Line) shape);
        else if (shape instanceof Rectangle)
            return new PaintableRectangle((Rectangle) shape);
        else if (shape instanceof BezierCurve)
            return new PaintableBezierCurve((BezierCurve) shape);
        else if (shape instanceof PolyLine)
            return new PaintablePolyLine((PolyLine) shape);
        else return null;
    }
}
