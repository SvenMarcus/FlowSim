package irmb.flowsim.view.factory;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.model.Shape;
import irmb.flowsim.presentation.factory.ShapeFactory;

/**
 * Created by Sven on 14.12.2016.
 */
public class ShapeFactoryImpl implements ShapeFactory {
    @Override
    public Shape makeShape(String type) {
        switch (type) {
            case "Line":
                return new Line();
            case "Rectangle":
                return new Rectangle();
            case "PolyLine":
                return new PolyLine();
            default:
                return null;
        }
    }
}
