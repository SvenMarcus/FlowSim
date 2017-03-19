package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.Shape;
import irmb.flowsim.model.TwoPointShape;
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
                return new PaintablePolyLineBuilder(factory);
            default:
                return null;
        }
    }
}
