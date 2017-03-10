package irmb.flowsim.simulation;

import irmb.flowsim.presentation.factory.ColorFactoryImpl;
import irmb.flowsim.simulation.jflowsim.adapters.JFlowSimNavierStokesGridAdapter;
import irmb.flowsim.simulation.jflowsim.adapters.JFlowSimNavierStokesSolverAdapter;
import numerics.BoundaryCondition;
import numerics.lbm.LBMNoSlipBC;
import numerics.lbm.LBMPressureBC;
import numerics.lbm.LBMVelocityBC;
import numerics.lbm.navierstokes.LBMNavierStokesGrid;
import numerics.lbm.navierstokes.LBMNavierStokesSolver;

/**
 * Created by sven on 09.03.17.
 */
public class SimulationFactoryImpl implements SimulationFactory {


    @Override
    public Simulation makeSimulation() {
        JFlowSimNavierStokesGridAdapter gridAdapter = makeGrid();
        LBMNavierStokesSolver solver = new LBMNavierStokesSolver(gridAdapter.getJFlowSimGrid());
        JFlowSimNavierStokesSolverAdapter solverAdapter = new JFlowSimNavierStokesSolverAdapter(solver);
        return new LBMChannelFlowSimulation(gridAdapter, solverAdapter, new ColorFactoryImpl());
    }

    private JFlowSimNavierStokesGridAdapter makeGrid() {
        LBMNavierStokesGrid grid = new LBMNavierStokesGrid(0.5, 0.2, 0.002);
        grid.setGravity(0.0, 0.0 /* m/s^2 */);
        grid.setViscosity(0.000001 /* m^2/s */);
        grid.setTimeStep(0.0001 /* s */);
        grid.updateParameters();

        // 2. boundary conditions
        double inflowVelo = 1.0; /* m/s */

        grid.addBC(new LBMPressureBC(grid, BoundaryCondition.EAST, 1.0));
        grid.addBC(new LBMVelocityBC(grid, BoundaryCondition.WEST, inflowVelo /* m/s */, 0.0));
        grid.addBC(new LBMNoSlipBC(grid, BoundaryCondition.NORTH));
        grid.addBC(new LBMNoSlipBC(grid, BoundaryCondition.SOUTH));

        // 3. initial conditions
        for (int i = 0; i < grid.nx; i++) {
            for (int j = 0; j < grid.ny; j++) {
                grid.init(i, j, 1. / 3., inflowVelo, 0.0);
            }
        }
        JFlowSimNavierStokesGridAdapter gridAdapter = new JFlowSimNavierStokesGridAdapter(grid);
        return gridAdapter;
    }
}