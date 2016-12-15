package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintablePolyLine;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLineBuilder extends PaintableShapeBuilder {

    private PaintablePolyLine polyLine;

    public PaintablePolyLineBuilder(PaintableFactory factory) {
        super(factory);
        polyLine = (PaintablePolyLine) factory.makeShape("PolyLine");
    }

    @Override
    public void addPoint(Point point) {
        polyLine.addPoint(point);
    }

    @Override
    public Paintable getShape() {
        return polyLine;
    }

    @Override
    public boolean isObjectFinished() {
        return false;
    }

    @Override
    public void setLastPoint(Point point) {
        polyLine.setLastPoint(point);
    }
}
