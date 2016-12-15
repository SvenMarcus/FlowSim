package irmb.flowsim.view;

import irmb.flowsim.presentation.Painter;

import java.awt.*;

/**
 * Created by Sven on 15.12.2016.
 */
public class SwingPainter implements Painter {

    private Graphics graphics;

    public SwingPainter(Graphics graphics) {
        this.graphics = graphics;
    }

    @Override
    public void paintLine(double x1, double y1, double x2, double y2) {
        graphics.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override
    public void paintRectangle(double x, double y, double width, double height) {
        graphics.drawRect((int) x, (int) y, (int) width, (int) height);
    }
}
