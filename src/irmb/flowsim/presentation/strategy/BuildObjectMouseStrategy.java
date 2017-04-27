package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

import static irmb.flowsim.presentation.strategy.StrategyState.FINISHED;
import static irmb.flowsim.presentation.strategy.StrategyState.UPDATE;

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
        Point point = getWorldPoint(x, y);
        addPointToShape(point);
        if (shapeBuilder.isObjectPaintable()) {
            addShapeToList();
            notifyObserverWithMatchingArgs();
        }
    }

    private void addPointToShape(Point point) {
        shapeBuilder.addPoint(point);
        pointsAdded++;
    }

    private void addShapeToList() {
        addPaintableShapeCommand = new AddPaintableShapeCommand(shapeBuilder.getShape(), shapeList);
        addPaintableShapeCommand.execute();
    }

    private void notifyObserverWithMatchingArgs() {
        StrategyState state = shapeBuilder.isObjectFinished() ? FINISHED : UPDATE;
        StrategyEventArgs args = makeStrategyEventArgs(state);
        if (state == FINISHED)
            args.setCommand(addPaintableShapeCommand);
        notifyObservers(args);
    }

    @Override
    public void onRightClick(double x, double y) {
        StrategyEventArgs args = makeStrategyEventArgs(FINISHED);
        if (shapeBuilder.isObjectPaintable())
            if (!shapeBuilder.isObjectFinished())
                finishObject(args);
            else
                addPaintableShapeCommand.undo();
        notifyObservers(args);
    }

    private void finishObject(StrategyEventArgs args) {
        shapeBuilder.removeLastPoint();
        args.setCommand(addPaintableShapeCommand);
    }

    @Override
    public void onMouseMove(double x, double y) {
        adjustShapeOnMouseMove(x, y);
        if (shapeBuilder.isObjectPaintable())
            notifyObservers(makeStrategyEventArgs(UPDATE));
    }

    private void adjustShapeOnMouseMove(double x, double y) {
        Point point = getWorldPoint(x, y);
        if (firstMove()) {
            addShapeToList();
            addPointToShape(point);
        } else if (shapeBuilder.isObjectPaintable())
            shapeBuilder.setLastPoint(point);
    }

    private boolean firstMove() {
        return pointsAdded == 1;
    }

    private Point getWorldPoint(double x, double y) {
        return transformer.transformToWorldPoint(new Point(x, y));
    }

    private StrategyEventArgs makeStrategyEventArgs(StrategyState state) {
        return new StrategyEventArgs(state);
    }


}
