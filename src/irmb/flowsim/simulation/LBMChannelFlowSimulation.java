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

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver, ColorFactory colorFactory) {
        this.grid = grid;
        this.solver = solver;
        solver.addObserver(this);
        this.colorFactory = colorFactory;
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
                if (grid.isSolid(x, y)) {
                    painter.setColor(new Color(0, 0, 0));
                } else {
                    adjustMinMax(x, y);
                    double velocity = grid.getVelocityAt(x, y);
                    painter.setColor(colorFactory.makeColorForValue(currentMin, currentMax, velocity));
                }
                painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() - grid.getHeight() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
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
        else if (v > max)
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

}
