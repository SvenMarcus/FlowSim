package irmb.flowsim.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.StrategyState;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.simulation.visualization.*;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by sven on 03.03.17.
 */
public class SimulationGraphicViewPresenter extends GraphicViewPresenter {

    private final SimulationFactory simulationFactory;
    private WeakReference<Simulation> simulationWeakReference;
    private GridNodeStyleFactory gridNodeStyleFactory = new GridNodeStyleFactory();
    private Map<PlotStyle, GridNodeStyle> plotStyleMap = new HashMap<>();

    private ArrayList<Paintable> paintables = new ArrayList<>();
    private boolean needsUpdate;
    private boolean running;

    public SimulationGraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandStack commandStack, List<PaintableShape> shapeList, CoordinateTransformer transformer, SimulationFactory simulationFactory) {
        super(strategyFactory, commandStack, shapeList);
        this.simulationFactory = simulationFactory;
    }

    protected void attachObserverToCommandStack() {
        commandStack.addObserver(args -> {
            updateGraphicViewAndSimulation();
        });
    }

    private void updateGraphicViewAndSimulation() {
        needsUpdate = true;
        graphicView.update();
        if (hasSimulation())
            simulationWeakReference.get().setShapes(shapeList);
    }

    private boolean hasSimulation() {
        return simulationWeakReference != null && simulationWeakReference.get() != null;
    }

    protected void addStrategyObserver() {
        strategy.addObserver((arg) -> {
            if (arg.getState() == StrategyState.FINISHED)
                makeStrategy("Move");
            if (arg.getCommand() != null)
                commandStack.add(arg.getCommand());
            updateGraphicViewAndSimulation();
        });
    }

    public void addSimulation() {
        needsUpdate = true;
        Simulation simulation = simulationFactory.makeSimulation();
        simulationWeakReference = new WeakReference<>(simulation);
        simulation.addObserver(args -> graphicView.update());
        simulation.setShapes(shapeList);
        running = false;
        for (GridNodeStyle style : plotStyleMap.values())
            simulation.addPlotStyle(style);
        graphicView.update();
    }

    @Override
    public List<Paintable> getPaintableList() {
        if (needsUpdate) {
            paintables = new ArrayList<>(shapeList);
            if (hasSimulation())
                paintables.add(0, simulationWeakReference.get());
        }
        return paintables;
    }

    public void runSimulation() {
        if (hasSimulation() && !running) {
            simulationWeakReference.get().run();
            running = true;
        }
    }

    public void pauseSimulation() {
        if (hasSimulation() && running) {
            simulationWeakReference.get().pause();
            running = false;
        }
    }

    public void clearAll() {
        super.clearAll();
        if (hasSimulation())
            simulationWeakReference.get().setShapes(shapeList);
    }

    public void togglePlotStyle(PlotStyle plotStyle) {
        if (!isPlotStyleActive(plotStyle))
            addPlotStyle(plotStyle);
        else
            removePlotStyle(plotStyle);
    }

    private boolean isPlotStyleActive(PlotStyle plotStyle) {
        return plotStyleMap.containsKey(plotStyle);
    }

    private void addPlotStyle(PlotStyle plotStyle) {
        GridNodeStyle gridNodeStyle = gridNodeStyleFactory.makeGridNodeStyle(plotStyle);
        plotStyleMap.put(plotStyle, gridNodeStyle);
        if (hasSimulation()) {
            simulationWeakReference.get().addPlotStyle(gridNodeStyle);
            graphicView.update();
        }
    }

    private void removePlotStyle(PlotStyle plotStyle) {
        if (hasSimulation()) {
            simulationWeakReference.get().removePlotStyle(plotStyleMap.get(plotStyle));
            graphicView.update();
        }
        plotStyleMap.remove(plotStyle);
    }

    public void removeSimulation() {
        pauseSimulation();
        simulationWeakReference.clear();
        graphicView.update();
    }
}
