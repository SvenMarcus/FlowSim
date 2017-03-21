package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.factory.PaintableShapeFactory;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by sven on 21.03.17.
 */
public class MultiPointShapeBuilder extends PaintableShapeBuilder {


    private final PolyLine polyLine;
    private final PaintableShapeFactory paintableShapeFactory;
    private PaintableShape paintable;

    public MultiPointShapeBuilder(PolyLine polyLine, PaintableShapeFactory paintableShapeFactory) {
        this.polyLine = polyLine;
        this.paintableShapeFactory = paintableShapeFactory;
    }

    @Override
    public void addPoint(Point point) {
        polyLine.addPoint(point);
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = paintableShapeFactory.makePaintableShape(polyLine);
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
