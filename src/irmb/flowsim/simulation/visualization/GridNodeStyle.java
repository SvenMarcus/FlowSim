package irmb.flowsim.simulation.visualization;

import irmb.flowsim.presentation.Painter;

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

    public abstract void paintGridNode(Painter painter, int xIndex, int yIndex, double min, double max, double vx, double vy, double viewX, double viewY, double viewDelta, double gridHeight);

    public int compareTo(GridNodeStyle style) {
        return Integer.compare(priority, style.priority);
    }

}
