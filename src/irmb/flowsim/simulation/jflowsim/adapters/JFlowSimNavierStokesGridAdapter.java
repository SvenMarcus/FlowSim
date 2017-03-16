package irmb.flowsim.simulation.jflowsim.adapters;

import irmb.flowsim.model.Point;
import irmb.flowsim.simulation.UniformGrid;
import numerics.lbm.navierstokes.LBMNavierStokesGrid;
import numerics.utilities.GridNodeType;
import numerics.utilities.Scalar;

/**
 * Created by sven on 09.03.17.
 */
public class JFlowSimNavierStokesGridAdapter extends UniformGrid {
    private LBMNavierStokesGrid grid;

    public JFlowSimNavierStokesGridAdapter(LBMNavierStokesGrid grid) {
        this.grid = grid;
    }

    @Override
    public void setHeight(double length) {
        grid.setWidth(length);
    }

    @Override
    public double getHeight() {
        return grid.getWidth();
    }

    @Override
    public void setWidth(double width) {
        grid.setLength(width);
    }

    @Override
    public double getWidth() {
        return grid.getLength();
    }

    @Override
    public void setHorizontalNodes(int horizontalNodes) {
        grid.nx = horizontalNodes;
    }

    @Override
    public int getHorizontalNodes() {
        return grid.nx;
    }

    @Override
    public void setVerticalNodes(int verticalNodes) {
        grid.ny = verticalNodes;
    }

    @Override
    public int getVerticalNodes() {
        return grid.ny;
    }

    @Override
    public double getVelocityAt(int x, int y) {
        return grid.getScalar(x, y, Scalar.V);
    }

    @Override
    public Point getTopLeft() {
        return new Point(grid.getMinX(), grid.getMaxY());
    }

    @Override
    public void setDelta(double delta) {
        grid.dx = delta;
    }

    @Override
    public double getDelta() {
        return grid.dx;
    }

    @Override
    public double getDeltaY() {
        return grid.dx;
    }

    @Override
    public void setDeltaY(double deltaY) {
        grid.dx = deltaY;
    }

    @Override
    public void resetSolidNodes() {
        for (int x = 0; x < grid.nx; x++) {
            for (int y = 0; y < grid.ny; y++) {
                if (grid.getType(x, y) == GridNodeType.SOLID) {
                    grid.setType(x, y, GridNodeType.FLUID);
                }
            }
        }
    }

    @Override
    public void setSolid(int x, int y) {
        grid.setType(x, y, GridNodeType.SOLID);
    }

    @Override
    public boolean isPointInside(Point point) {
        return !(point.getX() < grid.getMinX() || point.getX() > grid.getMaxX() || point.getY() < grid.getMinY() || point.getY() > grid.getMaxY());
    }

    @Override
    public boolean isSolid(int x, int y) {
        return grid.getType(x, y) == GridNodeType.SOLID;
    }

    public LBMNavierStokesGrid getJFlowSimGrid() {
        return grid;
    }
}
