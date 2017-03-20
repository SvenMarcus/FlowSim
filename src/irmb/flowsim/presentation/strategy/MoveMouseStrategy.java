package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
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
        Point worldPoint = transformer.transformToWorldPoint(clickedPoint);
        PaintableShape paintableShape = getPaintableShapeAt(worldPoint);
        if (paintableShape != null) {
            double tolerance = transformer.scaleToWorldLength(radius);
            Point definedPoint = paintableShape.getDefinedPoint(worldPoint, tolerance);
            if (definedPoint != null)
                moveShapeCommand = new MoveShapeCommand(definedPoint);
            else
                moveShapeCommand = new MoveShapeCommand(paintableShape.getShape());
        }
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

        double worldDx = transformer.scaleToWorldLength(dx);
        double worldDy = -transformer.scaleToWorldLength(dy);
        moveShapeCommand.setDelta(worldDx, worldDy);
        moveShapeCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    @Override
    public void onRightClick(double x, double y) {
        Point worldPoint = transformer.transformToWorldPoint(new Point(x, y));
        PaintableShape shape = getPaintableShapeAt(worldPoint);
        StrategyEventArgs args = new StrategyEventArgs(STRATEGY_STATE.UPDATE);
        RemovePaintableShapeCommand command = new RemovePaintableShapeCommand(shapeList, shape);
        command.execute();
        args.setCommand(command);
        notifyObservers(args);
    }

    private PaintableShape getPaintableShapeAt(Point worldPoint) {
        double tolerance = transformer.scaleToWorldLength(radius);
        for (PaintableShape p : shapeList)
            if (p.isPointOnBoundary(worldPoint, tolerance))
                return p;
        return null;
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

    public void setToleranceRadius(int radius) {
        this.radius = radius;
    }
}
