package irmb.flowsim.presentation;

import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.MoveMouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter implements Observer {

    private GraphicView graphicView;
    private MouseStrategyFactory factory;

    private List<PaintableShape> shapeList;
    private CommandQueue commandQueue;


    private MouseStrategy strategy;

    public GraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandQueue commandQueue, List<PaintableShape> shapeList) {
        this.factory = strategyFactory;
        this.commandQueue = commandQueue;
        this.shapeList = shapeList;
        this.commandQueue.addObserver(this);
        strategy = factory.makeStrategy("Move");
    }

    private MoveMouseStrategy makeMouseMoveStrategy() {
        return new MoveMouseStrategy(commandQueue, graphicView, shapeList);
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
    }

    public void handleLeftClick(double x, double y) {
        strategy.onLeftClick(x, y);
    }

    public void handleRightClick() {
        strategy.onRightClick();
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

    public void undo() {
        commandQueue.undo();
    }

    public void redo() {
        commandQueue.redo();
    }

    public void beginPaint(String objectType) {
        strategy = factory.makeStrategy(objectType);
        strategy.addObserver((o, arg) -> strategy = makeMouseMoveStrategy());
    }

    public List<PaintableShape> getPaintableList() {
        return shapeList;
    }

    @Override
    public void update(Observable o, Object arg) {
        graphicView.update();
    }
}
