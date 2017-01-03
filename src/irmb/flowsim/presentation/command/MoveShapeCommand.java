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
    private boolean calledUndo;

    public MoveShapeCommand(Shape shape) {
        this.shape = shape;
    }

    @Override
    public void execute() {
        shape.moveBy(dx, dy);
        calledExecute = true;
    }

    @Override
    public void undo() {
        if (calledExecute) {
            shape.moveBy(-dx, -dy);
            calledUndo = true;
            calledExecute = false;
        }
    }

    @Override
    public void redo() {
        if (calledUndo) {
            execute();
            calledUndo = false;
        }
    }

    public void setDelta(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
