package irmb.flowsim.presentation.strategy;


import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.MouseButton;
import irmb.flowsim.presentation.Zoom;
import irmb.flowsim.presentation.command.PanWindowCommand;
import irmb.flowsim.presentation.command.ZoomCommand;
import irmb.flowsim.util.ObservableImpl;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 05.01.2017.
 */
public abstract class MouseStrategy extends ObservableImpl<StrategyEventArgs> {

    protected List<PaintableShape> shapeList;
    protected CoordinateTransformer transformer;
    private Point clickedPoint;
    private MouseButton mouseButton;
    private PanWindowCommand panWindowCommand;

    public MouseStrategy(List<PaintableShape> shapeList, CoordinateTransformer transformer) {
        this.shapeList = shapeList;
        this.transformer = transformer;
    }

    public void onLeftClick(double x, double y) {
        mouseButton = MouseButton.LEFT;
    }

    public abstract void onMouseMove(double x, double y);

    public void onMouseDrag(double x, double y) {
        if (mouseButton == MouseButton.MIDDLE)
            panWindow(x, y);
    }

    private void panWindow(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        if (panWindowCommand == null)
            panWindowCommand = new PanWindowCommand(transformer);
        panWindowCommand.setDelta(dx, dy);
        panWindowCommand.execute();
        notifyObservers(new StrategyEventArgs(StrategyState.UPDATE));

        clickedPoint.setX(x);
        clickedPoint.setY(y);
    }

    public void onMiddleClick(double x, double y) {
        clickedPoint = new Point(x, y);
        mouseButton = MouseButton.MIDDLE;
    }

    public abstract void onRightClick(double x, double y);

    public void onMouseRelease() {
        if (panWindowCommand != null) {
            StrategyEventArgs args = new StrategyEventArgs(StrategyState.UPDATE);
            args.setCommand(panWindowCommand);
            notifyObservers(args);
        }
        panWindowCommand = null;
        mouseButton = null;
    }

    public void onScroll(double x, double y, double delta) {
        Point worldPoint = transformer.transformToWorldPoint(new Point(x, y));
        Zoom zoom = delta > 0 ? Zoom.IN : Zoom.OUT;
        ZoomCommand command = new ZoomCommand(transformer);
        command.setZoomFactor(0.05 * zoom.direction());
        command.setZoomPoint(worldPoint.getX(), worldPoint.getY());
        command.execute();
        StrategyEventArgs args = new StrategyEventArgs(StrategyState.UPDATE);
        args.setCommand(command);
        notifyObservers(args);
    }
}
