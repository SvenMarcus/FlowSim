package irmb.flowsim.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sven on 03.03.17.
 */
public class SimulationGraphicViewPresenter extends GraphicViewPresenter {

    private final SimulationFactory simulationFactory;
    private Simulation simulation;

    public SimulationGraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandQueue commandQueue, List<PaintableShape> shapeList, CoordinateTransformer transformer, SimulationFactory simulationFactory) {
        super(strategyFactory, commandQueue, shapeList, transformer);
        this.simulationFactory = simulationFactory;
    }

    public void addSimulation() {
        simulation = simulationFactory.makeSimulation();
        simulation.addObserver(args -> graphicView.update());
        graphicView.update();
    }

    @Override
    public List<Paintable> getPaintableList() {
        ArrayList<Paintable> paintables = new ArrayList<>(shapeList);
        paintables.add(simulation);
        return paintables;
    }

    public void runSimulation() {
        if (simulation != null)
            simulation.run();
    }

    public void pauseSimulation() {
        if (simulation != null)
            simulation.pause();
    }
}
