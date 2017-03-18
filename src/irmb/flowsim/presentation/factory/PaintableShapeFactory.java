package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.Shape;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by sven on 18.03.17.
 */
public interface PaintableShapeFactory {
    PaintableShape makePaintableShape(Shape shape);
}
