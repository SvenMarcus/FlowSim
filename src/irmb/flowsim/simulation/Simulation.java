package irmb.flowsim.simulation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.util.Observable;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by sven on 03.03.17.
 */
public abstract class Simulation extends Observable<String> implements Paintable {

    public abstract void run();

    public abstract void pause();
}
