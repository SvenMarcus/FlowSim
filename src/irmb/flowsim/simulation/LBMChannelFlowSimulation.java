package irmb.flowsim.simulation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;

/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulation extends Simulation {
    private UniformGrid grid;
    private ColorFactory colorFactory;
    private double min;
    private double max;

    public LBMChannelFlowSimulation(UniformGrid grid, ColorFactory colorFactory) {
        this.grid = grid;
        this.colorFactory = colorFactory;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        getMinMax();
        for (int i = 0; i < grid.getVerticalNodes(); i++)
            for (int j = 0; j < grid.getHorizontalNodes(); j++) {
                double velocity = grid.getVelocityAt(i, j);
                painter.setColor(colorFactory.makeColorForValue(min, max, velocity));
                painter.fillRectangle(grid.getOrigin().getX() + i, grid.getOrigin().getY() + j, grid.getDeltaX(), grid.getDeltaY());
            }
    }

    private void getMinMax() {
        min = grid.getVelocityAt(0, 0);
        max = grid.getVelocityAt(0, 0);
        for (int i = 0; i < grid.getVerticalNodes(); i++)
            for (int j = 0; j < grid.getHorizontalNodes(); j++)
                adjustMinMax(i, j);
    }

    private void adjustMinMax(int i, int j) {
        if (grid.getVelocityAt(i, j) < min)
            min = grid.getVelocityAt(i, j);
        else if (grid.getVelocityAt(i, j) > max)
            max = grid.getVelocityAt(i, j);
    }

    @Override
    public void run() {

    }

    @Override
    public void pause() {

    }
}
