package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.Shape;

/**
 * Created by Sven on 14.12.2016.
 */
public interface ShapeFactory {
    Shape makeShape(String type);
}
