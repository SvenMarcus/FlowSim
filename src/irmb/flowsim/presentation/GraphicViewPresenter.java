package irmb.flowsim.presentation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import irmb.flowsim.presentation.command.PanWindowCommand;
import irmb.flowsim.presentation.command.ZoomCommand;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter {

    private GraphicView graphicView;
    private MouseStrategyFactory factory;

    private List<PaintableShape> shapeList;
    private CommandQueue commandQueue;


    private MouseStrategy strategy;
    private CoordinateTransformer transformer = new CoordinateTransformerImpl();
    private Point clickedPoint;
    private MouseButton mouseButton;
    private PanWindowCommand panWindowCommand;

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
        mouseButton = MouseButton.LEFT;
        clickedPoint = new Point(x, y);
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
        if (mouseButton == MouseButton.MIDDLE) {
            moveViewWindow(x, y);
            graphicView.update();
        }
    }

    public void handleMouseRelease() {
        strategy.onMouseRelease();
        if (panWindowCommand != null)
            commandQueue.add(panWindowCommand);
        panWindowCommand = null;
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

    private void makeStrategy(String objectType) {
        strategy = factory.makeStrategy(objectType);
        strategy.addObserver((arg) -> {
            if (arg.getState() == STRATEGY_STATE.FINISHED) {
                makeStrategy("Move");
            }
            if (arg.getCommand() != null)
                commandQueue.add(arg.getCommand());
            graphicView.update();
        });
    }

    public List<PaintableShape> getPaintableList() {
        return shapeList;
    }

    public void handleMiddleClick(double x, double y) {
        mouseButton = MouseButton.MIDDLE;
        clickedPoint = new Point(x, y);
        strategy.onWheelClick(x, y);
    }

    private void moveViewWindow(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        if (panWindowCommand == null)
            panWindowCommand = new PanWindowCommand(transformer);
        panWindowCommand.setDelta(dx, dy);
        panWindowCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }


    public void handleScroll(double x, double y, int delta) {
        Point worldPoint = transformer.transformToWorldPoint(new Point(x, y));
        Zoom zoom = delta > 0 ? Zoom.IN : Zoom.OUT;
        ZoomCommand command = new ZoomCommand(transformer);
        command.setZoomFactor(0.05 * zoom.direction());
        command.setZoomPoint(worldPoint.getX(), worldPoint.getY());
        command.execute();
        commandQueue.add(command);
        graphicView.update();
    }
}
