package irmb.flowsim.simulation.visualization;

import irmb.flowsim.presentation.factory.ColorFactoryImpl;

/**
 * Created by sven on 24.03.17.
 */
public class GridNodeStyleFactory {
    public GridNodeStyle makeGridNodeStyle(PlotStyle style) {
        switch (style) {
            case Color:
                return new ColorGridNodeStyle(new ColorFactoryImpl());
            case Arrow:
                return new ArrowGridNodeStyle(5);
            default:
                return null;
        }

    }

}
