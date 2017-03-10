package irmb.flowsim.view.javafx;

import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by Sven on 22.12.2016.
 */
public class JavaFXPainter implements Painter {
    private GraphicsContext graphicsContext;
    private javafx.scene.paint.Color jfxColor = javafx.scene.paint.Color.BLACK;

    public JavaFXPainter(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }

    public JavaFXPainter() {
    }

    @Override
    public void paintLine(double x1, double y1, double x2, double y2) {
        jfxColor = javafx.scene.paint.Color.BLACK;
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeLine(x1, y1, x2, y2);
    }

    @Override
    public void paintRectangle(double x, double y, double width, double height) {
        jfxColor = javafx.scene.paint.Color.BLACK;
        graphicsContext.setLineWidth(1);
        graphicsContext.strokeRect(x, y, width, height);
    }

    @Override
    public void setColor(Color color) {
        int r, g, b;
        r = color.r / 255;
        g = color.g / 255;
        b = color.b / 255;
        jfxColor = javafx.scene.paint.Color.color(r, g, b, 1);
    }

    @Override
    public void fillRectangle(double x, double y, double width, double height) {
        graphicsContext.setFill(jfxColor);
        graphicsContext.fillRect(x, y, width, height);
    }

    public void setGraphicsContext(GraphicsContext graphicsContext) {
        this.graphicsContext = graphicsContext;
    }
}
