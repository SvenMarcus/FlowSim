package irmb.flowsim.view.swing;

import irmb.flowsim.presentation.*;
import irmb.flowsim.presentation.Color;

import java.awt.*;

/**
 * Created by Sven on 15.12.2016.
 */
public class SwingPainter implements Painter {

    private Graphics graphics;

    public SwingPainter() {
    }

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

    @Override
    public void setColor(Color color) {
        int r, g, b;
        r = color.r;
        g = color.g;
        b = color.b;
        java.awt.Color c = new java.awt.Color(r, g, b);
        graphics.setColor(c);
    }

    @Override
    public void fillRectangle(double x, double y, double width, double height) {
        graphics.fillRect((int) x, (int) y, (int) width, (int) height);
    }

    @Override
    public void paintPoint(double x, double y) {
        graphics.drawLine((int) x - 5, (int) y - 5, (int) x + 5, (int) y + 5);
        graphics.drawLine((int) x + 5, (int) y - 5, (int) x - 5, (int) y + 5);
    }

    public void setGraphics(Graphics graphics) {
        this.graphics = graphics;
    }
}
