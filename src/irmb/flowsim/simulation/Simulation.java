package irmb.flowsim.simulation;

import irmb.flowsim.util.ObservableImpl;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by sven on 03.03.17.
 */
public abstract class Simulation extends ObservableImpl<String> implements Paintable {

    public abstract void run();

    public abstract void pause();
}
