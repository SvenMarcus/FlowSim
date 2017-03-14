package irmb.flowsim.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter {

    protected GraphicView graphicView;
    private MouseStrategyFactory factory;

    protected List<PaintableShape> shapeList;
    protected CommandQueue commandQueue;


    protected MouseStrategy strategy;
    private CoordinateTransformer transformer;

    public GraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandQueue commandQueue, List<PaintableShape> shapeList, CoordinateTransformer transformer) {
        this.factory = strategyFactory;
        this.commandQueue = commandQueue;
        this.shapeList = shapeList;
        this.commandQueue.addObserver((args) -> graphicView.update());
        makeStrategy("Move");
        this.transformer = transformer;
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
        graphicView.setCoordinateTransformer(transformer);
    }

    public void handleLeftClick(double x, double y) {
        strategy.onLeftClick(x, y);
    }

    public void handleRightClick(double x, double y) {
        strategy.onRightClick(x, y);
    }

    public void handleMouseMove(double x, double y) {
        strategy.onMouseMove(x, y);
    }

    public void handleMouseDrag(double x, double y) {
        strategy.onMouseDrag(x, y);
    }

    public void handleMouseRelease() {
        strategy.onMouseRelease();
    }

    public void handleMiddleClick(double x, double y) {
        strategy.onMiddleClick(x, y);
    }

    public void handleScroll(double x, double y, int delta) {
        strategy.onScroll(x, y, delta);
    }

    public void undo() {
        commandQueue.undo();
    }

    public void redo() {
        commandQueue.redo();
    }

    public void beginPaint(String objectType) {
        makeStrategy(objectType);
    }

    protected void makeStrategy(String objectType) {
        strategy = factory.makeStrategy(objectType);
        addStrategyObserver();
    }

    protected void addStrategyObserver() {
        strategy.addObserver((arg) -> {
            if (arg.getState() == STRATEGY_STATE.FINISHED)
                makeStrategy("Move");
            if (arg.getCommand() != null)
                commandQueue.add(arg.getCommand());
            graphicView.update();
        });
    }

    public List<Paintable> getPaintableList() {
        return new ArrayList<>(shapeList);
    }

}
