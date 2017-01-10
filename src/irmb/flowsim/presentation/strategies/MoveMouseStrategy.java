package irmb.flowsim.presentation.strategies;

import irmb.flowsim.model.Point;
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

    public MoveMouseStrategy(CommandQueue commandQueue, GraphicView graphicView, List<PaintableShape> shapeList) {
        this.shapeList = shapeList;
        this.graphicView = graphicView;
        this.commandQueue = commandQueue;
    }

    @Override
    public void onLeftClick(double x, double y) {
        for (PaintableShape p : shapeList) {
            clickedPoint = new Point(x, y);
            if (p.isPointOnBoundary(new Point(x, y), 3)) {
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
