package irmb.flowsim.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.command.Command;
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
    private GraphicView graphicView;
    private CommandQueue commandQueue;
    private String mouseButton = "";


    public MoveMouseStrategy(CommandQueue commandQueue, GraphicView graphicView, List<PaintableShape> shapeList) {
        this.shapeList = shapeList;
        this.graphicView = graphicView;
        this.commandQueue = commandQueue;
    }

    @Override
    public void onLeftClick(double x, double y) {
        clickedPoint = new Point(x, y);
        for (PaintableShape p : shapeList) {
            if (p.isPointOnBoundary(clickedPoint, 1)) {
                moveShapeCommand = new MoveShapeCommand(p.getShape());
            }
        }
    }

    @Override
    public void onMouseMove(double x, double y) {

    }

    @Override
    public void onMouseDrag(double x, double y) {
        if (moveShapeCommand != null) {
            moveShape(x, y);
            graphicView.update();
        }
    }

    @Override
    public void onWheelClick(double x, double y) {

    }

    private void moveAllShapes(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();
        for (PaintableShape p : shapeList)
            p.getShape().moveBy(dx, dy);
    }

    private void moveShape(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        moveShapeCommand.setDelta(dx, dy);
        moveShapeCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    @Override
    public void onRightClick() {

    }

    @Override
    public void onMouseRelease() {
        if (moveShapeCommand != null)
            addCommand(moveShapeCommand);
        moveShapeCommand = null;
    }

    private void addCommand(Command command) {
        commandQueue.add(command);
    }
}
