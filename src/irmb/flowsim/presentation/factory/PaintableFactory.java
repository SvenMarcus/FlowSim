package irmb.flowsim.presentation.factory;

import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by Sven on 14.12.2016.
 */
public interface PaintableFactory {
    Paintable makeShape(String type);

}
