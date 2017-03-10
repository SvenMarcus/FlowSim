package irmb.flowsim.simulation;

import irmb.flowsim.util.ObservableImpl;

/**
 * Created by sven on 09.03.17.
 */
public abstract class LBMSolver extends ObservableImpl<String> {
    public abstract void solve();

    public abstract void pause();
}
