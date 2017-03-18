package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.model.Shape;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableRectangle;
import irmb.flowsim.view.graphics.PaintableShape;

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
        else if (shape instanceof PolyLine)
            return new PaintablePolyLine((PolyLine) shape);
        else return null;
    }
}
