package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public interface Paintable {
    void paint(Painter g);

    boolean isPointOnBoundary(Point point, double radius);
}
