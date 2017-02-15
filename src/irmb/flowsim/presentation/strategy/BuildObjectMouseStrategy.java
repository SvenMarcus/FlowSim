package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 05.01.2017.
 */
public class BuildObjectMouseStrategy extends MouseStrategy {

    private int pointsAdded = 0;
    private PaintableShapeBuilder shapeBuilder;
    private AddPaintableShapeCommand addPaintableShapeCommand;

    public BuildObjectMouseStrategy(List<PaintableShape> shapeList, CoordinateTransformer transformer, PaintableShapeBuilder builder) {
        super(shapeList, transformer);
        this.shapeBuilder = builder;
    }

    @Override
    public void onLeftClick(double x, double y) {
        Point point = transformer.transformToWorldPoint(new Point(x, y));
        addPointToShape(point.getX(), point.getY());
        addShapeToList();
        if (pointsAdded >= 2)
            notifyObserverWithMatchingArgs();
    }

    @Override
    public void onMouseMove(double x, double y) {
        Point point = transformer.transformToWorldPoint(new Point(x, y));
        if (pointsAdded == 1)
            addPointToShape(point.getX(), point.getY());
        else if (pointsAdded > 1)
            shapeBuilder.setLastPoint(point);
        if (pointsAdded > 0)
            notifyObservers(makeStrategyEventArgs(STRATEGY_STATE.UPDATE));
    }

    @Override
    public void onRightClick() {
        StrategyEventArgs args = makeStrategyEventArgs(STRATEGY_STATE.FINISHED);
        if (pointsAdded > 2) {
            shapeBuilder.removeLastPoint();
            args.setCommand(addPaintableShapeCommand);
        } else if (addPaintableShapeCommand != null)
            addPaintableShapeCommand.undo();
        notifyObservers(args);
    }

    private StrategyEventArgs makeStrategyEventArgs(STRATEGY_STATE state) {
        return new StrategyEventArgs(state);
    }

    private void addShapeToList() {
        addPaintableShapeCommand = new AddPaintableShapeCommand(shapeBuilder.getShape(), shapeList);
        addPaintableShapeCommand.execute();
    }

    private void addPointToShape(double x, double y) {
        shapeBuilder.addPoint(new Point(x, y));
        pointsAdded++;
    }

    private void notifyObserverWithMatchingArgs() {
        StrategyEventArgs args;
        if (shapeBuilder.isObjectFinished()) {
            args = makeStrategyEventArgs(STRATEGY_STATE.FINISHED);
            args.setCommand(addPaintableShapeCommand);
        } else
            args = makeStrategyEventArgs(STRATEGY_STATE.UPDATE);
        notifyObservers(args);
    }
}
