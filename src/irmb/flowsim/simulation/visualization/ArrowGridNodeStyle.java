package irmb.flowsim.simulation.visualization;

import irmb.flowsim.presentation.Painter;

/**
 * Created by sven on 23.03.17.
 */
public class ArrowGridNodeStyle extends GridNodeStyle {


    private double min;
    private double max;
    private int offset;

    public ArrowGridNodeStyle(int offset) {
        super(1);
        this.offset = offset;
    }

    @Override
    public void setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void paintGridNode(Painter painter, int xIndex, int yIndex, double min, double max, double vx, double vy, double viewX, double viewY, double viewDelta, double gridHeight) {
        if (max != min && xIndex % offset == 0 && yIndex % offset == 0) {
            double scale = viewDelta / (max - min);
            double x0 = viewX + xIndex * viewDelta - vx * scale;
            double y0 = viewY + yIndex * viewDelta - vy * scale;
            double x1 = viewX + xIndex * viewDelta + vx * scale;
            double y1 = viewY + yIndex * viewDelta + vy * scale;
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

    public boolean equals(Object o) {
        try {
            ArrowGridNodeStyle other = (ArrowGridNodeStyle) o;
            return other.min == min && other.max == max && other.offset == offset;
        } catch (Exception e) {
            return false;
        }
    }

}
