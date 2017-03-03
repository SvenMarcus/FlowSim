package irmb.flowsim.view.graphics;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;

/**
 * Created by sven on 03.03.17.
 */
public interface Paintable {
    void paint(Painter g, CoordinateTransformer transformer);
}
