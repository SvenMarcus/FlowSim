package simulation;

import irmb.flowsim.simulation.LBMSolver;

/**
 * Created by sven on 09.03.17.
 */
public class SolverMock extends LBMSolver {
    @Override
    public void solve() {

    }

    @Override
    public void pause() {

    }

    public void notifyObservers(String args) {
        super.notifyObservers(args);
    }
}
