package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;

/**
 * Created by sven on 23.03.17.
 */
public class ArrowGridNodeStyle extends GridNodeStyle {

    private int offset;
    private boolean firstRun = true;
    private double max;
    private double min;
    private double currentMin;
    private double currentMax;

    public ArrowGridNodeStyle(int offset) {
        super(1);
        this.offset = offset;
    }

    @Override
    public void paintGridNode(Painter painter, CoordinateTransformer transformer) {
        painter.setColor(Color.BLACK);
        double viewDelta, viewX, viewY;
        viewDelta = transformer.scaleToScreenLength(grid.getDelta());
        Point topLeft = transformer.transformToPointOnScreen(grid.getTopLeft());
        viewX = topLeft.getX();
        viewY = topLeft.getY();

        getInitialMinMax();
        currentMin = min;
        currentMax = max;

        min = Double.MAX_VALUE;
        max = Double.MIN_VALUE;

        int size = grid.getHorizontalNodes() * grid.getVerticalNodes();
        int x, y;
        for (int i = 0; i < size; i++) {
            y = i / grid.getHorizontalNodes();
            x = i % grid.getHorizontalNodes();
            adjustMinMax(x, y);
            if (currentMax != currentMin && x % offset == 0 && y % offset == 0) {
                painter.setColor(Color.BLACK);
                double scale = viewDelta / (currentMax - currentMin);
                double vx = grid.getHorizontalVelocityAt(x, y);
                double vy = grid.getVerticalVelocityAt(x, y);
                double x0 = viewX + x * viewDelta - vx * scale;
                double y0 = viewY + y * viewDelta - vy * scale;
                double x1 = viewX + x * viewDelta + vx * scale;
                double y1 = viewY + y * viewDelta + vy * scale;
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

    public boolean equals(Object o) {
        try {
            ArrowGridNodeStyle other = (ArrowGridNodeStyle) o;
            return other.offset == offset;
        } catch (Exception e) {
            return false;
        }
    }

}
