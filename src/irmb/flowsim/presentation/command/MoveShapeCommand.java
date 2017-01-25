package irmb.flowsim.presentation.command;

import irmb.flowsim.model.Shape;

/**
 * Created by Sven on 02.01.2017.
 */
public class MoveShapeCommand implements Command {

    private final Shape shape;
    private double dx;
    private double dy;
    private boolean calledExecute;
    private double totalDx;
    private double totalDy;

    public MoveShapeCommand(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void execute() {
        shape.moveBy(dx, dy);
        totalDx += dx;
        totalDy += dy;
        calledExecute = true;
    }

    @Override
    public void undo() {
        if (calledExecute) {
            shape.moveBy(-totalDx, -totalDy);
            calledExecute = false;
        }
    }

    @Override
    public void redo() {
        if (!calledExecute) {
            shape.moveBy(totalDx, totalDy);
            calledExecute = true;
        }
    }

    public void setDelta(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
