package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLineBuilder extends PaintableShapeBuilder {

    private PolyLine polyLine;
    private PaintableShape paintable;

    public PaintablePolyLineBuilder(ShapeFactory factory) {
        super(factory);
        polyLine = (PolyLine) factory.makeShape("PolyLine");
    }

    @Override
    public void addPoint(Point point) {
        polyLine.addPoint(point);
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = new PaintablePolyLine(polyLine);
        return paintable;
    }

    @Override
    public boolean isObjectFinished() {
        return false;
    }

    @Override
    public void setLastPoint(Point point) {
        polyLine.setLastPoint(point);
    }

    @Override
    public void removeLastPoint() {
        polyLine.removeLastPoint();
    }
}
