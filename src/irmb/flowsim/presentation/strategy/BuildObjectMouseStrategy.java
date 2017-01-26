package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 05.01.2017.
 */
public class BuildObjectMouseStrategy extends MouseStrategy {

    private PaintableShapeBuilderFactory factory;

    private int pointsAdded = 0;
    private GraphicView graphicView;
    private List<PaintableShape> shapeList;
    private CoordinateTransformer transformer;
    private PaintableShapeBuilder shapeBuilder;
    private AddPaintableShapeCommand addPaintableShapeCommand;
    private CommandQueue commandQueue;

    public BuildObjectMouseStrategy(CommandQueue commandQueue, GraphicView graphicView, List<PaintableShape> shapeList, PaintableShapeBuilder builder, CoordinateTransformer transformer) {
        this.commandQueue = commandQueue;
        this.shapeBuilder = builder;
        this.graphicView = graphicView;
        this.shapeList = shapeList;
        this.transformer = transformer;
    }

    @Override
    public void onLeftClick(double x, double y) {
        if (hasShapeBuilder()) {
            Point point = transformer.transformToWorldPoint(new Point(x, y));
            addPointToShape(point.getX(), point.getY());
            addShapeToList();
            if (pointsAdded >= 2) {
                graphicView.update();
                resetBuilderWhenFinished();
            }
        }
    }

    @Override
    public void onMouseMove(double x, double y) {
        Point point = transformer.transformToWorldPoint(new Point(x, y));
        if (hasShapeBuilder()) {
            if (pointsAdded == 1) {
                addPointToShape(point.getX(), point.getY());
            } else if (pointsAdded > 1)
                shapeBuilder.setLastPoint(point);
            if (pointsAdded > 0)
                graphicView.update();
        }
    }

    @Override
    public void onMouseDrag(double x, double y) {
    }

    @Override
    public void onWheelClick(double x, double y) {
    }

    @Override
    public void onRightClick() {
        notifyObservers(new StrategyEventArgs(STRATEGY_STATE.FINISHED));
        if (hasShapeBuilder()) {
            if (pointsAdded > 2) {
                shapeBuilder.removeLastPoint();
                addCommand(addPaintableShapeCommand);
            } else if (addPaintableShapeCommand != null)
                addPaintableShapeCommand.undo();
            graphicView.update();
            shapeBuilder = null;
        }
    }

    @Override
    public void onMouseRelease() {

    }

    private void addShapeToList() {
        addPaintableShapeCommand = new AddPaintableShapeCommand(shapeBuilder.getShape(), shapeList);
        addPaintableShapeCommand.execute();
    }

    private void addPointToShape(double x, double y) {
        shapeBuilder.addPoint(new Point(x, y));
        pointsAdded++;
    }

    private boolean hasShapeBuilder() {
        return shapeBuilder != null;
    }

    private void resetBuilderWhenFinished() {
        if (shapeBuilder.isObjectFinished()) {
            shapeBuilder = null;
            addCommand(addPaintableShapeCommand);
            notifyObservers(new StrategyEventArgs(STRATEGY_STATE.FINISHED));
        }
    }

    private void addCommand(Command command) {
        commandQueue.add(command);
    }
}
