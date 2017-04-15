package irmb.test.simulation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.simulation.visualization.ArrowGridNodeStyle;
import irmb.flowsim.simulation.visualization.ColorGridNodeStyle;
import irmb.flowsim.simulation.visualization.GridNodeStyle;
import irmb.flowsim.simulation.visualization.InfoDisplayGridNodeStyle;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by sven on 04.03.17.
 */
public class SimulationMock extends Simulation {

    private boolean colorStyleAdded;
    private boolean arrowStyleAdded;
    private GridNodeStyle addedPlotStyle;
    private boolean infoDisplayStyleAdded;

    @Override
    public void paint(Painter painter, CoordinateTransformer transformer) {

    }

    @Override
    public void run() {

    }

    @Override
    public void pause() {
    }

    @Override
    public void setShapes(List<PaintableShape> shapeList) {

    }

    @Override
    public void addPlotStyle(GridNodeStyle gridNodeStyle) {
        if (gridNodeStyle instanceof ColorGridNodeStyle) {
            colorStyleAdded = true;
        } else if (gridNodeStyle instanceof ArrowGridNodeStyle) {
            arrowStyleAdded = true;
        } else if (gridNodeStyle instanceof InfoDisplayGridNodeStyle)
        addedPlotStyle = gridNodeStyle;
    }

    @Override
    public void removePlotStyle(GridNodeStyle gridNodeStyle) {
        if (gridNodeStyle instanceof ColorGridNodeStyle) {
            colorStyleAdded = false;
        } else if (gridNodeStyle instanceof ArrowGridNodeStyle) {
            arrowStyleAdded = false;
        }
    }

    @Override
    public void notifyObservers(String args) {
        super.notifyObservers(args);
    }

    public boolean isColorStyleAdded() {
        return colorStyleAdded;
    }

    public boolean isArrowStyleAdded() {
        return arrowStyleAdded;
    }

    public GridNodeStyle getAddedPlotStyle() {
        return addedPlotStyle;
    }

    public boolean isInfoDisplayStyleAdded() {
        return infoDisplayStyleAdded;
    }
}
