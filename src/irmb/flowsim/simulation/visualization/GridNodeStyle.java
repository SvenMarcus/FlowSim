package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by sven on 23.03.17.
 */
public interface GridNodeStyle {

    void setMinMax(double min, double max);

    void paintGridNode(Painter painter, CoordinateTransformer transformer, int x, int y);

}
