package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.simulation.UniformGrid;

/**
 * Created by sven on 23.03.17.
 */
public abstract class GridNodeStyle implements Comparable<GridNodeStyle> {
    protected double min;
    protected double max;
    protected final int priority;

    protected GridNodeStyle(int priority) {
        this.priority = priority;
    }

    public void setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    public abstract void paintGridNode(Painter painter, CoordinateTransformer transformer, UniformGrid grid, int x, int y);

    public int compareTo(GridNodeStyle style) {
        return Integer.compare(priority, style.priority);
    }

}
