package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class PaintableShape {

    public abstract void paint(Painter g, CoordinateTransformer transformer);

    public abstract boolean isPointOnBoundary(Point point, double radius);

    public abstract Shape getShape();
}
