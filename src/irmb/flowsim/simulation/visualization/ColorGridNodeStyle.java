package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;

/**
 * Created by sven on 23.03.17.
 */
public class ColorGridNodeStyle extends GridNodeStyle {

    private ColorFactory colorFactory;
    private boolean firstRun = true;
    private double min;
    private double max;
    private double currentMin;
    private double currentMax;


    public ColorGridNodeStyle(ColorFactory colorFactory) {
        super(0);
        this.colorFactory = colorFactory;
    }

    @Override
    public void paintGridNode(Painter painter, CoordinateTransformer transformer) {
        double viewDelta, viewX, viewY;
        viewDelta = transformer.scaleToScreenLength(grid.getDelta());
        Point topLeft = transformer.transformToPointOnScreen(grid.getTopLeft());
        viewX = topLeft.getX();
        viewY = topLeft.getY();

        getInitialMinMax();
        double currentMin = min;
        double currentMax = max;

        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        for (int y = 0; y < grid.getVerticalNodes(); y++)
            for (int x = 0; x < grid.getHorizontalNodes(); x++) {
                adjustMinMax(x, y);
                double velocity = grid.getVelocityAt(x, y);
                painter.setColor(colorFactory.makeColorForValue(currentMin, currentMax, velocity));
                painter.fillRectangle(viewX + x * viewDelta, viewY - grid.getHeight() + y * viewDelta, Math.ceil(viewDelta), Math.ceil(viewDelta));
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
}
