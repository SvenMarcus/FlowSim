package irmb.flowsim.simulation.visualization;

import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;

/**
 * Created by sven on 23.03.17.
 */
public class ColorGridNodeStyle extends GridNodeStyle {

    private ColorFactory colorFactory;



    public ColorGridNodeStyle(ColorFactory colorFactory) {
        super(0);
        this.colorFactory = colorFactory;
    }

    @Override
    public void paintGridNode(Painter painter, int xIndex, int yIndex, double min, double max, double vx, double vy, double viewX, double viewY, double viewDelta, double gridHeight) {
        double velocity = Math.sqrt(vx * vx + vy * vy);
        painter.setColor(colorFactory.makeColorForValue(min, max, velocity));

        painter.fillRectangle(viewX + xIndex * viewDelta, viewY - gridHeight + yIndex * viewDelta, Math.ceil(viewDelta), Math.ceil(viewDelta));
    }


}
