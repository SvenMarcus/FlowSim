package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.simulation.UniformGrid;

/**
 * Created by sven on 23.03.17.
 */
public abstract class GridNodeStyle implements Comparable<GridNodeStyle> {
    protected final int priority;

    protected UniformGrid grid;

    protected GridNodeStyle(int priority) {
        this.priority = priority;
    }

    public void setGrid(UniformGrid grid) {
        this.grid = grid;
    }

    public abstract void paintGridNode(Painter painter, int xIndex, int yIndex, double min, double max, double vx, double vy, double viewX, double viewY, double viewDelta, double gridHeight);

    public abstract void paintGridNode(Painter painter, CoordinateTransformer transformer);

    public int compareTo(GridNodeStyle style) {
        return Integer.compare(priority, style.priority);
    }

}
