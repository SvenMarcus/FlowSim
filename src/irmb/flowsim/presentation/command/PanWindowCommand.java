package irmb.flowsim.presentation.command;

import irmb.flowsim.model.util.CoordinateTransformer;

/**
 * Created by sven on 24.01.17.
 */
public class PanWindowCommand implements Command {

    private CoordinateTransformer transformer;
    private double dx;
    private double dy;
    private double totalDx;
    private double totalDy;
    private boolean calledExecute;

    public PanWindowCommand(CoordinateTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void execute() {
        transformer.moveViewWindow(dx, dy);
        totalDx += dx;
        totalDy += dy;
        calledExecute = true;
    }

    @Override
    public void undo() {
        if (calledExecute) {
            transformer.moveViewWindow(-totalDx, -totalDy);
            calledExecute = false;
        }
    }

    @Override
    public void redo() {
        if (!calledExecute) {
            transformer.moveViewWindow(totalDx, totalDy);
            calledExecute = true;
        }
    }

    public void setDelta(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
