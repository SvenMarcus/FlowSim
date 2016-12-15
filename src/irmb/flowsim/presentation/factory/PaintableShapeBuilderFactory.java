package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.builder.PaintableShapeBuilder;

/**
 * Created by Sven on 14.12.2016.
 */
public interface PaintableShapeBuilderFactory {
    PaintableShapeBuilder makeShapeBuilder(String type);
}
