package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.TwoPointShape;
import irmb.flowsim.model.MultiPointShape;
import irmb.flowsim.presentation.builder.*;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableShapeBuilderFactoryImpl implements PaintableShapeBuilderFactory {
    private ShapeFactory factory;
    private PaintableShapeFactory paintableShapeFactory;

    public PaintableShapeBuilderFactoryImpl(ShapeFactory factory, PaintableShapeFactory paintableShapeFactory) {
        this.factory = factory;
        this.paintableShapeFactory = paintableShapeFactory;
    }

    @Override
    public PaintableShapeBuilder makeShapeBuilder(String type) {
        switch (type) {
            case "Line":
                return new TwoPointShapeBuilder((TwoPointShape) factory.makeShape(type), paintableShapeFactory);
            case "Rectangle":
                return new TwoPointShapeBuilder((TwoPointShape) factory.makeShape(type), paintableShapeFactory);
            case "PolyLine":
                return new MultiPointShapeBuilder((MultiPointShape) factory.makeShape(type), paintableShapeFactory);
            case "Bezier":
                return new MultiPointShapeBuilder((MultiPointShape) factory.makeShape(type), paintableShapeFactory);
            default:
                return null;
        }
    }
}
