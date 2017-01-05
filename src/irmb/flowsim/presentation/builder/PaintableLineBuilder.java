package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableLine;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableLineBuilder extends PaintableShapeBuilder {

    private Line line;
    private int pointsAdded;
    private PaintableShape paintable;

    public PaintableLineBuilder(ShapeFactory factory) {
        super(factory);
        this.line = (Line) factory.makeShape("Line");
    }

    @Override
    public void addPoint(Point point) {
        if (pointsAdded == 0)
            line.setFirst(point);
        else if (pointsAdded == 1)
            line.setSecond(point);
        pointsAdded++;
    }

    @Override
    public PaintableShape getShape() {
        if (paintable == null)
            paintable = new PaintableLine(line);
        return paintable;
    }

    @Override
    public boolean isObjectFinished() {
        return pointsAdded >= 2;
    }

    @Override
    public void setLastPoint(Point point) {
        if (pointsAdded == 1) line.setFirst(point);
        else if (pointsAdded >= 2) line.setSecond(point);
    }

    @Override
    public void removeLastPoint() {

    }
}
