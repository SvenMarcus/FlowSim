package irmb.flowsim.simulation;

import irmb.flowsim.model.*;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;


/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulation extends Simulation implements Observer<String> {
    private final UniformGrid grid;
    private final LBMSolver solver;
    private final ColorFactory colorFactory;
    private double min;
    private double max;
    private GridMapper gridMapper;
    private boolean firstRun = true;
    private String plotStyle = "";
    private int offset;

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver, ColorFactory colorFactory) {
        this.grid = grid;
        this.solver = solver;
        solver.addObserver(this);
        this.colorFactory = colorFactory;
        plotStyle = "Arrow";
        offset = 5;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        getInitialMinMax();
        double currentMin = min;
        double currentMax = max;

        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
        double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
        Point origin = transformer.transformToPointOnScreen(grid.getTopLeft());

        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++) {
                adjustMinMax(x, y);
                if (!plotStyle.equals("Arrow")) {
                    paintColor(painter, currentMin, currentMax, dxScreen, dyScreen, origin, y, x);
                } else {
                    paintArrow(painter, transformer, currentMin, currentMax, y, x);
                }
            }
    }

    private void paintColor(Painter painter, double currentMin, double currentMax, double dxScreen, double dyScreen, Point origin, int y, int x) {
        if (grid.isSolid(x, y)) {
            painter.setColor(new Color(0, 0, 0));
        } else {
            double velocity = grid.getVelocityAt(x, y);
            painter.setColor(colorFactory.makeColorForValue(currentMin, currentMax, velocity));
        }
        painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() - grid.getHeight() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
    }

    private void paintArrow(Painter painter, CoordinateTransformer transformer, double currentMin, double currentMax, int y, int x) {
        if (currentMax != currentMin && x % offset == 0 && y % offset == 0) {
            double vx = grid.getHorizontalVelocityAt(x, y);
            double vy = grid.getVerticalVelocityAt(x, y);
            double viewDelta = transformer.scaleToScreenLength(grid.getDelta());
            double scale = 2 * viewDelta / (currentMax - currentMin);
            Point topLeft = transformer.transformToPointOnScreen(grid.getTopLeft());
            double x0 = topLeft.getX() + x * viewDelta - vx * scale;
            double y0 = topLeft.getY() + y * viewDelta - vy * scale;
            double x1 = topLeft.getX() + x * viewDelta + vx * scale;
            double y1 = topLeft.getY() + y * viewDelta + vy * scale;
            double dx = x1 - x0;
            double dy = y1 - y0;
            double Xt1 = -dx * .25;
            double Yt1 = -dy * .25;
            double Xt2 = -dy * .25;
            double Yt2 = dx * .25;
            double xC = x1 + Xt2 + Xt1;
            double yC = y1 + Yt2 + Yt1;
            double Xt3 = dy * .25;
            double Yt3 = -dx * .25;
            double xD = x1 + Xt3 + Xt1;
            double yD = y1 + Yt3 + Yt1;
            painter.paintLine(x0, y0, x1, y1);
            painter.paintLine(x1, y1, xC, yC);
            painter.paintLine(x1, y1, xD, yD);
        }
    }

    private void getInitialMinMax() {
        if (firstRun) {
            min = grid.getVelocityAt(0, 0);
            max = grid.getVelocityAt(0, 0);
            for (int y = 0; y < grid.getVerticalNodes(); y++)
                for (int x = 0; x < grid.getHorizontalNodes(); x++)
                    adjustMinMax(x, y);
            firstRun = false;
        }
    }

    private void adjustMinMax(int x, int y) {
        double v = grid.getVelocityAt(x, y);
        if (v < min)
            min = v;
        if (v > max)
            max = v;
    }

    @Override
    public void run() {
        solver.solve();
    }

    @Override
    public void pause() {
        solver.pause();
    }

    @Override
    public void setShapes(List<PaintableShape> shapeList) {
        if (gridMapper == null)
            gridMapper = new GridMapper(grid);
        gridMapper.mapShapes(shapeList);
    }

    @Override
    public void update(String args) {
        notifyObservers(null);
    }

    public void addPlotStyle(String style, int offset) {
        plotStyle = style;
        this.offset = offset;
    }
}
