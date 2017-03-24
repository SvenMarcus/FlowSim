package irmb.flowsim.simulation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.simulation.visualization.GridNodeStyle;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulation extends Simulation implements Observer<String> {
    private final UniformGrid grid;
    private final LBMSolver solver;
    private ColorFactory colorFactory;
    private double min;
    private double max;
    private GridMapper gridMapper;
    private boolean firstRun = true;

    private List<GridNodeStyle> styleList = new ArrayList<>();

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver, ColorFactory colorFactory) {
        this.grid = grid;
        this.solver = solver;
        this.colorFactory = colorFactory;
        solver.addObserver(this);
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        getInitialMinMax();
        double currentMin = min;
        double currentMax = max;

        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        Point topLeft = transformer.transformToPointOnScreen(grid.getTopLeft());
        double width = transformer.scaleToScreenLength(grid.getWidth());
        double height = transformer.scaleToScreenLength(grid.getHeight());
        painter.paintRectangle(topLeft.getX(), topLeft.getY(), width, height);

//        for (int y = 0; y < grid.getVerticalNodes(); y++)
//            for (int x = 0; x < grid.getHorizontalNodes(); x++) {
//                adjustMinMax(x, y);
//                for (GridNodeStyle style : styleList) {
////                    style.setMinMax(currentMin, currentMax);
//                    style.paintGridNode(painter, transformer, grid, x, y, currentMin, currentMax);
//                }
//            }

        int x, y;
        for (int i = 0; i < grid.getHorizontalNodes() * grid.getVerticalNodes(); i++) {
            x = i % grid.getHorizontalNodes();
            y = i / grid.getHorizontalNodes();
            adjustMinMax(x, y);
            for (GridNodeStyle style : styleList)
                style.paintGridNode(painter, transformer, grid, x, y, currentMin, currentMax);
//            double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
//            double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
//            Point origin = transformer.transformToPointOnScreen(grid.getTopLeft());
//            if (grid.isSolid(x, y)) {
//                painter.setColor(new Color(0, 0, 0));
//            } else {
//                double velocity = grid.getVelocityAt(x, y);
//                painter.setColor(colorFactory.makeColorForValue(currentMin, currentMax, velocity));
//            }
//            painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() - grid.getHeight() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
        }
    }

    public void paintGridNode(Painter painter, CoordinateTransformer transformer, int x, int y, double min, double max) {
        double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
        double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
        Point origin = transformer.transformToPointOnScreen(grid.getTopLeft());
        if (grid.isSolid(x, y)) {
            painter.setColor(new Color(0, 0, 0));
        } else {
            double velocity = grid.getVelocityAt(x, y);
            painter.setColor(colorFactory.makeColorForValue(min, max, velocity));
        }
        painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() - grid.getHeight() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
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

    public void addPlotStyle(GridNodeStyle gridNodeStyle) {
        styleList.add(gridNodeStyle);
        styleList.sort(Comparator.naturalOrder());
    }

    @Override
    public void removePlotStyle(GridNodeStyle gridNodeStyle) {
        styleList.remove(gridNodeStyle);
    }
}
