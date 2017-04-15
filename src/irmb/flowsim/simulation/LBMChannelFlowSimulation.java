package irmb.flowsim.simulation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
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
    private double min;
    private double max;
    private GridMapper gridMapper;
    private boolean firstRun = true;

    private List<GridNodeStyle> styleList = new ArrayList<>();

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver) {
        this.grid = grid;
        this.solver = solver;
        solver.addObserver(this);
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        paintSurroundingRectangle(painter, transformer);
        for (GridNodeStyle style : styleList)
            style.paintGridNode(painter, transformer);
    }

    private void paintSurroundingRectangle(Painter painter, CoordinateTransformer transformer) {
        Point topLeft = transformer.transformToPointOnScreen(grid.getTopLeft());
        double width = transformer.scaleToScreenLength(grid.getWidth());
        double height = transformer.scaleToScreenLength(grid.getHeight());
        painter.paintRectangle(topLeft.getX(), topLeft.getY(), width, height);

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
        gridNodeStyle.setGrid(grid);
    }

    @Override
    public void removePlotStyle(GridNodeStyle gridNodeStyle) {
        styleList.remove(gridNodeStyle);
    }
}
