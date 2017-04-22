package irmb.flowsim.simulation.visualization;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;

import java.text.DecimalFormat;

/**
 * Created by sven on 15.04.17.
 */
public class InfoDisplayGridNodeStyle extends GridNodeStyle {

    private static int horizontalMargin = 10;
    private static int verticalMargin = 20;
    private static DecimalFormat exponentialFormat = new DecimalFormat("0.00E0");
    private static DecimalFormat secondsFormat = new DecimalFormat("0.0000");
    private int tempFps;
    private long lastTime;
    private int fps;

    protected InfoDisplayGridNodeStyle() {
        super(2);
    }

    @Override
    public void paintGridNode(Painter painter, CoordinateTransformer transformer) {
        painter.setColor(Color.BLACK);
        int verticalPosition = verticalMargin;
        painter.paintString(gridName(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(width(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(height(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(dof(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(realTime(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(MNUPS(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(viscosity(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(gravity(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        painter.paintString(timestep(), horizontalMargin, verticalPosition);
        verticalPosition += 20;
        updateFPS();
        painter.paintString("FPS: " + fps, horizontalMargin, verticalPosition);
    }

    private void updateFPS() {
        tempFps++;
        long currentTime = System.nanoTime();
        if (currentTime > lastTime + 1000000000) {
            lastTime = currentTime;
            fps = tempFps;
            tempFps = 0;
        }
    }

    private String timestep() {
        return "Time step: " + grid.getTimeStep() + " [s]" + newLine();
    }

    private String gravity() {
        return "Gravity: " + grid.getHorizontalGravity() + " " + grid.getVerticalGravity() + " [m/s^2]" + newLine();
    }

    private String viscosity() {
        return "Viscosity: " + exponentialFormat.format(grid.getViscosity()) + " [m^2/s]" + newLine();
    }

    private String MNUPS() {
        return "MNUPS: " + String.format("%1$.3f", grid.getMNUPS()) + newLine();
    }

    private String realTime() {
        return "real time: " + secondsFormat.format(grid.getRealTime()) + " [s]" + newLine();
    }

    private String dof() {
        return "dof: "
                + grid.getHorizontalNodes()
                + "x"
                + grid.getVerticalNodes()
                + " | "
                + grid.getHorizontalNodes() * grid.getVerticalNodes()
                + " | delta:"
                + grid.getDelta() + newLine();
    }

    private String height() {
        return "width:" + grid.getHeight() + " [m]" + newLine();
    }

    private String width() {
        return "Length: " + grid.getWidth() + "[m]" + newLine();
    }

    private String gridName() {
        return grid.getClass().getSimpleName() + newLine();
    }

    private static String newLine() {
        return "\n";
    }


}
