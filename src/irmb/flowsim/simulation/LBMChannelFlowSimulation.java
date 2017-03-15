package irmb.flowsim.simulation;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;


/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulation extends Simulation implements Observer<String> {
    private UniformGrid grid;
    private LBMSolver solver;
    private ColorFactory colorFactory;
    private double min;
    private double max;

    public LBMChannelFlowSimulation(UniformGrid grid, LBMSolver solver, ColorFactory colorFactory) {
        this.grid = grid;
        this.solver = solver;
        solver.addObserver(this);
        this.colorFactory = colorFactory;
    }

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {
        getMinMax();
        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++) {
                double velocity = grid.getVelocityAt(x, y);
                painter.setColor(colorFactory.makeColorForValue(min, max, velocity));
                Point origin = transformer.transformToPointOnScreen(grid.getOrigin());
                double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
                double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
                painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
            }
    }

    private void getMinMax() {
        min = grid.getVelocityAt(0, 0);
        max = grid.getVelocityAt(0, 0);
        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++)
                adjustMinMax(x, y);
    }

    private void adjustMinMax(int x, int y) {
        if (grid.getVelocityAt(x, y) < min)
            min = grid.getVelocityAt(x, y);
        else if (grid.getVelocityAt(x, y) > max)
            max = grid.getVelocityAt(x, y);
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
        if (shapeList.size() == 0) return;
        Shape shape = shapeList.get(0).getShape();
        Line line = (Line) shape;
        grid.resetSolidNodes();
//        int x0 = (int) Math.floor(line.getFirst().getX() / grid.getDelta());
//        int y0 = (int) Math.floor(line.getFirst().getY() / grid.getDelta());
//        int x1 = (int) Math.floor(line.getSecond().getX() / grid.getDelta());
//        int y1 = (int) Math.floor(line.getSecond().getY() / grid.getDelta());
        int x0 = (int) line.getFirst().getX();
        int y0 = (int) line.getFirst().getY();
        int x1 = (int) line.getSecond().getX();
        int y1 = (int) line.getSecond().getY();
        bresenham(x0, y0, x1, y1);
    }

    private void bresenham(int xstart, int ystart, int xend, int yend) {
        int x, y, t, dx, dy, incx, incy, pdx, pdy, ddx, ddy, es, el, err;

        dx = xend - xstart;
        dy = yend - ystart;

        incx = dx < 0 ? -1 : 1;
        incy = dy < 0 ? -1 : 1;
        if (dx < 0) dx = -dx;
        if (dy < 0) dy = -dy;

        if (dx > dy) {
            pdx = incx;
            pdy = 0;
            ddx = incx;
            ddy = incy;
            es = dy;
            el = dx;
        } else {
            pdx = 0;
            pdy = incy;
            ddx = incx;
            ddy = incy;
            es = dx;
            el = dy;
        }

        x = xstart;
        y = ystart;
        err = el / 2;
        grid.setSolid(x, y);
        for (t = 0; t < el; ++t) {
            err -= es;
            if (err < 0) {
                err += el;
                x += ddx;
                y += ddy;
            } else {
                x += pdx;
                y += pdy;
            }
            grid.setSolid(x, y);
        }
    }


    @Override
    public void update(String args) {
        notifyObservers(null);
    }
}
