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

    public LBMChannelFlowSimulation(UniformGrid grid, ColorFactory colorFactory) {
        this.grid = grid;
        this.colorFactory = colorFactory;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        for (int i = 0; i < grid.getVerticalNodes(); i++)
            for (int j = 0; j < grid.getHorizontalNodes(); j++) {
                double velocity = grid.getVelocityAt(i, j);
                painter.setColor(colorFactory.makeColorForValue(0, 5, velocity));
            }
    }

    @Override
    public void run() {

    }

    @Override
    public void pause() {

    }
}
