package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.builder.PaintableLineBuilder;
import irmb.flowsim.presentation.builder.PaintablePolyLineBuilder;
import irmb.flowsim.presentation.builder.PaintableRectangleBuilder;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableShapeBuilderFactoryImpl implements PaintableShapeBuilderFactory {
    private ShapeFactory factory;

    public PaintableShapeBuilderFactoryImpl(ShapeFactory factory) {
        this.factory = factory;
    }

    @Override
    public PaintableShapeBuilder makeShapeBuilder(String type) {
        switch (type) {
            case "Line":
                return new PaintableLineBuilder(factory);
            case "Rectangle":
                return new PaintableRectangleBuilder(factory);
            case "PolyLine":
                return new PaintablePolyLineBuilder(factory);
            default:
                return null;
        }
    }
}
