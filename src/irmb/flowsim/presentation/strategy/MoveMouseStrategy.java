package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.command.MoveShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 05.01.2017.
 */
public class MoveMouseStrategy extends MouseStrategy {


    private MoveShapeCommand moveShapeCommand;
    private Point clickedPoint;
    private List<PaintableShape> shapeList;
    private CoordinateTransformer transformer;


    public MoveMouseStrategy(List<PaintableShape> shapeList, CoordinateTransformer transformer) {
        this.shapeList = shapeList;
        this.transformer = transformer;
    }

    @Override
    public void onLeftClick(double x, double y) {
        clickedPoint = new Point(x, y);
        Point point = transformer.transformToWorldPoint(clickedPoint);
        double tolerance = transformer.scaleToWorldLength(3);
        for (PaintableShape p : shapeList)
            if (p.isPointOnBoundary(point, tolerance))
                moveShapeCommand = new MoveShapeCommand(p.getShape());
    }

    @Override
    public void onMouseMove(double x, double y) {

    }

    @Override
    public void onMouseDrag(double x, double y) {
        if (moveShapeCommand != null) {
            moveShape(x, y);
            notifyObservers(new StrategyEventArgs(STRATEGY_STATE.UPDATE));
        }
    }

    @Override
    public void onWheelClick(double x, double y) {
    }

    private void moveShape(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        double viewDx = transformer.scaleToWorldLength(dx);
        double viewDy = -transformer.scaleToWorldLength(dy);
        moveShapeCommand.setDelta(viewDx, viewDy);
        moveShapeCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    @Override
    public void onRightClick() {
    }

    @Override
    public void onMouseRelease() {
        if (moveShapeCommand != null) {
            StrategyEventArgs args = new StrategyEventArgs(STRATEGY_STATE.UPDATE);
            args.setCommand(moveShapeCommand);
            notifyObservers(args);
        }
        moveShapeCommand = null;
    }
}
