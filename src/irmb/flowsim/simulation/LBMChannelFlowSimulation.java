package irmb.flowsim.simulation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.util.Observer;


/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulation extends Simulation implements Observer<String> {
    private UniformGrid grid;
    private LBMSolver solver;
    private ColorFactory colorFactory;
    private double min;
    private double max;

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver, ColorFactory colorFactory) {
        this.grid = grid;
        this.solver = solver;
        solver.addObserver(this);
        this.colorFactory = colorFactory;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        getMinMax();
        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++) {
                double velocity = grid.getVelocityAt(x, y);
                painter.setColor(colorFactory.makeColorForValue(min, max, velocity));
                Point origin = transformer.transformToPointOnScreen(grid.getOrigin());
                double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
                double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
                painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
            }
    }

    private void getMinMax() {
        min = grid.getVelocityAt(0, 0);
        max = grid.getVelocityAt(0, 0);
        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++)
                adjustMinMax(x, y);
    }

    private void adjustMinMax(int x, int y) {
        if (grid.getVelocityAt(x, y) < min)
            min = grid.getVelocityAt(x, y);
        else if (grid.getVelocityAt(x, y) > max)
            max = grid.getVelocityAt(x, y);
    }

    @Override
    public void run() {
        solver.solve();
    }

    @Override
    public void pause() {
        solver.pause();
    }

    @Override
    public void update(String args) {
        notifyObservers(null);
    }
}
