package irmb.flowsim.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;

/**
 * Created by Sven on 13.12.2016.
 */
public interface GraphicView {
    void update();

    void setCoordinateTransformer(CoordinateTransformer transformer);
}
