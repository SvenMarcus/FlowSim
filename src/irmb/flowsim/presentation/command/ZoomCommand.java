package irmb.flowsim.presentation.command;

import irmb.flowsim.model.util.CoordinateTransformer;

/**
 * Created by sven on 13.02.17.
 */
public class ZoomCommand implements Command {
    private CoordinateTransformer transformer;
    private double zoomFactor;
    private double x;
    private double y;

    public ZoomCommand(CoordinateTransformer transformer) {
        this.transformer = transformer;
    }

    public void setZoomFactor(double zoomFactor) {
        this.zoomFactor = zoomFactor;
    }

    public void setZoomPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void execute() {
        transformer.zoomWindow(zoomFactor, x, y);
    }

    @Override
    public void undo() {
        transformer.zoomWindow(-zoomFactor, x, y);
    }

    @Override
    public void redo() {
        execute();
    }
}
