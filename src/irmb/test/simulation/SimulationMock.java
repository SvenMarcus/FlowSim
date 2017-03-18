package irmb.test.simulation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by sven on 04.03.17.
 */
public class SimulationMock extends Simulation {
    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {

    }

    @Override
    public void run() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void setShapes(List<PaintableShape> shapeList) {

    }

    @Override
    public void notifyObservers(String args) {
        super.notifyObservers(args);
    }


}
