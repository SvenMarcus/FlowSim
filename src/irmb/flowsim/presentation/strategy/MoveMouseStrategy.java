package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.command.MoveShapeCommand;
import irmb.flowsim.presentation.command.RemovePaintableShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 05.01.2017.
 */
public class MoveMouseStrategy extends MouseStrategy {


    private int radius = 3;
    private MoveShapeCommand moveShapeCommand;
    private Point clickedPoint;
    private List<PaintableShape> shapeList;
    private CoordinateTransformer transformer;


    public MoveMouseStrategy(List<PaintableShape> shapeList, CoordinateTransformer transformer) {
        super(shapeList, transformer);
        this.shapeList = shapeList;
        this.transformer = transformer;
    }

    @Override
    public void onLeftClick(double x, double y) {
        super.onLeftClick(x, y);
        clickedPoint = new Point(x, y);
        prepareShapeMove(getPaintableShapeAt(x, y));
    }

    private void prepareShapeMove(PaintableShape paintableShape) {
        if (paintableShape != null) {
            double tolerance = getWorldLength(radius);
            Point worldPoint = getWorldPoint(clickedPoint);
            Point definedPoint = paintableShape.getDefinedPoint(worldPoint, tolerance);
            makeMoveShapeCommand(paintableShape, definedPoint);
        }
    }

    private void makeMoveShapeCommand(PaintableShape paintableShape, Point definedPoint) {
        Shape shape = definedPoint != null ? definedPoint : paintableShape.getShape();
        moveShapeCommand = new MoveShapeCommand(shape);
    }

    @Override
    public void onRightClick(double x, double y) {
        PaintableShape shape = getPaintableShapeAt(x, y);
        deleteShape(shape);
    }

    private void deleteShape(PaintableShape shape) {
        RemovePaintableShapeCommand command = makeRemoveCommand(shape);
        command.execute();
        notifyWithRemoveCommand(command);
    }

    private void notifyWithRemoveCommand(RemovePaintableShapeCommand command) {
        StrategyEventArgs args = new StrategyEventArgs(STRATEGY_STATE.UPDATE);
        args.setCommand(command);
        notifyObservers(args);
    }

    private RemovePaintableShapeCommand makeRemoveCommand(PaintableShape shape) {
        return new RemovePaintableShapeCommand(shapeList, shape);
    }

    @Override
    public void onMouseMove(double x, double y) {
    }

    @Override
    public void onMouseDrag(double x, double y) {
        super.onMouseDrag(x, y);
        if (moveShapeCommand != null) {
            moveShape(x, y);
            notifyObservers(new StrategyEventArgs(STRATEGY_STATE.UPDATE));
        }
    }

    private void moveShape(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        double worldDx = getWorldLength(dx);
        double worldDy = -getWorldLength(dy);
        moveShapeCommand.setDelta(worldDx, worldDy);
        moveShapeCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    @Override
    public void onMouseRelease() {
        super.onMouseRelease();
        if (moveShapeCommand != null) {
            StrategyEventArgs args = new StrategyEventArgs(STRATEGY_STATE.UPDATE);
            args.setCommand(moveShapeCommand);
            notifyObservers(args);
        }
        moveShapeCommand = null;
    }

    private PaintableShape getPaintableShapeAt(double x, double y) {
        Point worldPoint = getWorldPoint(new Point(x, y));
        double tolerance = transformer.scaleToWorldLength(radius);
        for (PaintableShape p : shapeList)
            if (p.isPointOnBoundary(worldPoint, tolerance))
                return p;
        return null;
    }

    private double getWorldLength(double radius) {
        return transformer.scaleToWorldLength(radius);
    }

    private Point getWorldPoint(Point clickedPoint) {
        return transformer.transformToWorldPoint(clickedPoint);
    }

    public void setToleranceRadius(int radius) {
        this.radius = radius;
    }
}
