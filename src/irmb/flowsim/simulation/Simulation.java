package irmb.flowsim.simulation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by sven on 03.03.17.
 */
public interface Simulation extends Paintable{

    void run();

    void pause();
}
