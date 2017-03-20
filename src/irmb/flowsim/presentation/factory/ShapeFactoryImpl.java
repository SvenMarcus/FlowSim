package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.*;
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
            case "Bezier":
                return new BezierCurve();
            default:
                return null;
        }
    }
}
