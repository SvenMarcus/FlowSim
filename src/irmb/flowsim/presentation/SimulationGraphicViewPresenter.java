package irmb.flowsim.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.simulation.visualization.*;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sven on 03.03.17.
 */
public class SimulationGraphicViewPresenter extends GraphicViewPresenter {

    private final SimulationFactory simulationFactory;
    private Simulation simulation;
    private GridNodeStyleFactory gridNodeStyleFactory = new GridNodeStyleFactory();
    private Map<PlotStyle, GridNodeStyle> plotStyleMap = new HashMap<>();

    public SimulationGraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandQueue commandQueue, List<PaintableShape> shapeList, CoordinateTransformer transformer, SimulationFactory simulationFactory) {
        super(strategyFactory, commandQueue, shapeList, transformer);
        this.simulationFactory = simulationFactory;
    }

    protected void attachObserverToCommandQueue() {
        commandQueue.addObserver(args -> {
            updateGraphicViewAndSimulation();
        });
    }

    private void updateGraphicViewAndSimulation() {
        graphicView.update();
        if (simulation != null)
            simulation.setShapes(shapeList);
    }

    protected void addStrategyObserver() {
        strategy.addObserver((arg) -> {
            if (arg.getState() == STRATEGY_STATE.FINISHED)
                makeStrategy("Move");
            if (arg.getCommand() != null)
                commandQueue.add(arg.getCommand());
            updateGraphicViewAndSimulation();
        });
    }

    public void addSimulation() {
        simulation = simulationFactory.makeSimulation();
        simulation.addObserver(args -> graphicView.update());
        simulation.setShapes(shapeList);
        for (GridNodeStyle style : plotStyleMap.values())
            simulation.addPlotStyle(style);
        graphicView.update();
    }

    @Override
    public List<Paintable> getPaintableList() {
        ArrayList<Paintable> paintables = new ArrayList<>(shapeList);
        if (simulation != null)
            paintables.add(0, simulation);
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

    public void clearAll() {
        super.clearAll();
        if (simulation != null)
            simulation.setShapes(shapeList);
    }

    public void addPlotStyle(PlotStyle plotStyle) {
        GridNodeStyle gridNodeStyle = gridNodeStyleFactory.makeGridNodeStyle(plotStyle);
        if (simulation != null)
            simulation.addPlotStyle(gridNodeStyle);
        plotStyleMap.put(plotStyle, gridNodeStyle);
    }

    public void removePlotStyle(PlotStyle plotStyle) {
        if (simulation != null)
            simulation.removePlotStyle(plotStyleMap.get(plotStyle));
        plotStyleMap.remove(plotStyle);
    }
}
