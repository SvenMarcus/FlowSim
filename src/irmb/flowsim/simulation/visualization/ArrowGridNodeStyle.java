package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.simulation.UniformGrid;

/**
 * Created by sven on 23.03.17.
 */
public class ArrowGridNodeStyle implements GridNodeStyle {


    private double min;
    private double max;
    private UniformGrid grid;
    private int offset;

    public ArrowGridNodeStyle(UniformGrid grid, int offset) {
        this.grid = grid;
        this.offset = offset;
    }

    @Override
    public void setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void paintGridNode(Painter painter, CoordinateTransformer transformer, int x, int y) {
        if (max != min && x % offset == 0 && y % offset == 0) {
            double vx = grid.getHorizontalVelocityAt(x, y);
            double vy = grid.getVerticalVelocityAt(x, y);
            double viewDelta = transformer.scaleToScreenLength(grid.getDelta());
            double scale = viewDelta / (max - min);
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
}
