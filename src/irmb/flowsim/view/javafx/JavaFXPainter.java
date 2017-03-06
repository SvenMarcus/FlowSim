package irmb.flowsim.view.javafx;

import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Sven on 22.12.2016.
 */
public class JavaFXPainter implements Painter {
    private GraphicsContext graphicsContext;

    public JavaFXPainter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    @Override
    public void paintLine(double x1, double y1, double x2, double y2) {
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void paintRectangle(double x, double y, double width, double height) {
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeRect(x, y, width, height);
    }

    @Override
    public void setColor(Color color) {

    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }
}
