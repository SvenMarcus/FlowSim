package irmb.flowsim.presentation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
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
    private CoordinateTransformer transformer = new CoordinateTransformerImpl();
    private Point clickedPoint;
    private MouseButton mouseButton;

    public GraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandQueue commandQueue, List<PaintableShape> shapeList) {
        this.factory = strategyFactory;
        this.commandQueue = commandQueue;
        this.shapeList = shapeList;
        this.commandQueue.addObserver(this);
        strategy = factory.makeStrategy("Move");
        transformer.setWorldBounds(new Point(-10, 10), new Point(10, -10));
    }

    private MoveMouseStrategy makeMouseMoveStrategy() {
        return new MoveMouseStrategy(commandQueue, graphicView, shapeList);
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
        graphicView.setCoordinateTransformer(transformer);
    }

    public void handleLeftClick(double x, double y) {
        mouseButton = MouseButton.LEFT;
        clickedPoint = new Point(x, y);
        Point p = transformer.transformToWorldPoint(clickedPoint);
        strategy.onLeftClick(p.getX(), p.getY());
    }

    public void handleRightClick() {
        strategy.onRightClick();
    }

    public void handleMouseMove(double x, double y) {
        Point p = transformer.transformToWorldPoint(new Point(x, y));
        strategy.onMouseMove(p.getX(), p.getY());
    }

    public void handleMouseDrag(double x, double y) {
        Point p = transformer.transformToWorldPoint(new Point(x, y));
        strategy.onMouseDrag(p.getX(), p.getY());
        if (mouseButton == MouseButton.MIDDLE) {
            moveViewWindow(x, y);
            graphicView.update();
        }
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

    public void handleMiddleClick(double x, double y) {
        mouseButton = MouseButton.MIDDLE;
        clickedPoint = new Point(x, y);
        Point p = transformer.transformToWorldPoint(clickedPoint);
        strategy.onWheelClick(p.getX(), p.getY());

    }

    private void moveViewWindow(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        transformer.moveViewWindow(dx, dy);

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    @Override
    public void update(Observable o, Object arg) {
        graphicView.update();
    }
}
