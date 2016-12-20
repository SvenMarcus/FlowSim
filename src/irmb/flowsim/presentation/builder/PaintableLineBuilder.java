package irmb.flowsim.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.graphics.PaintableLine;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableLineBuilder extends PaintableShapeBuilder {

    private PaintableLine line;
    private int pointsAdded;

    public PaintableLineBuilder(PaintableFactory factory) {
        super(factory);
        this.line = (PaintableLine) factory.makeShape("Line");
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
    public Paintable getShape() {
        return line;
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
