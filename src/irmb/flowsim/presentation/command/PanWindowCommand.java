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

    public PanWindowCommand(CoordinateTransformer transformer) {
        this.transformer = transformer;
    }

    @Override
    public void execute() {
        transformer.moveViewWindow(dx, dy);
        totalDx += dx;
        totalDy += dy;
    }

    @Override
    public void undo() {
        transformer.moveViewWindow(-totalDx, -totalDy);
        totalDx = totalDy = 0;
    }

    @Override
    public void redo() {
        transformer.moveViewWindow(dx, dy);
    }

    public void setDelta(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
