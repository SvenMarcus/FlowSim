package irmb.flowsim.view.factory;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableRectangle;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableFactoryImpl implements PaintableFactory {
    @Override
    public Paintable makeShape(String type) {
        switch (type) {
            case "Line":
                return new PaintableLine(new Line());
            case "Rectangle":
                return new PaintableRectangle(new Rectangle());
            case "PolyLine":
                return new PaintablePolyLine(new PolyLine());
            default:
                return null;
        }
    }
}
