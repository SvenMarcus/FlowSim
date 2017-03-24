package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.simulation.UniformGrid;

/**
 * Created by sven on 23.03.17.
 */
public class ColorGridNodeStyle extends GridNodeStyle {

    private ColorFactory colorFactory;
    private double min;
    private double max;


    public ColorGridNodeStyle(ColorFactory colorFactory) {
        super(0);
        this.colorFactory = colorFactory;
    }

    public void setMinMax(double min, double max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public void paintGridNode(Painter painter, CoordinateTransformer transformer, UniformGrid grid, int x, int y) {
        double dxScreen = transformer.scaleToScreenLength(grid.getDelta());
        double dyScreen = transformer.scaleToScreenLength(grid.getDelta());
        Point origin = transformer.transformToPointOnScreen(grid.getTopLeft());
        if (grid.isSolid(x, y)) {
            painter.setColor(new Color(0, 0, 0));
        } else {
            double velocity = grid.getVelocityAt(x, y);
            painter.setColor(colorFactory.makeColorForValue(min, max, velocity));
        }
        painter.fillRectangle(origin.getX() + x * dxScreen, origin.getY() - grid.getHeight() + y * dyScreen, Math.ceil(dxScreen), Math.ceil(dyScreen));
    }
}
