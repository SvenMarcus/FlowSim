package irmb.flowsim.simulation.jflowsim.adapters;

import irmb.flowsim.simulation.LBMSolver;
import numerics.lbm.navierstokes.LBMNavierStokesSolver;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by sven on 09.03.17.
 */
public class JFlowSimNavierStokesSolverAdapter extends LBMSolver implements Observer {

    private final LBMNavierStokesSolver solver;

    public JFlowSimNavierStokesSolverAdapter(LBMNavierStokesSolver solver) {
        this.solver = solver;
        solver.addObserver(this);
    }

    @Override
    public void solve() {
        solver.startSimulation();
    }

    @Override
    public void pause() {
        solver.interrupt();
    }

    @Override
    public void update(Observable o, Object arg) {
        notifyObservers("");
    }
}
