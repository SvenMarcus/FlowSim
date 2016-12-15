package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Rectangle;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableRectangle extends Rectangle implements Paintable {

    private final Rectangle rectangle;

    public PaintableRectangle() {
        rectangle = new Rectangle();
    }

    public PaintableRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    @Override
    public void paint(Painter painter) {
        int minX = getFirst().getX() < getSecond().getX() ? (int) getFirst().getX() : (int) getSecond().getX();
        int minY = getFirst().getY() < getSecond().getY() ? (int) getFirst().getY() : (int) getSecond().getY();
        int width = (int) Math.abs(getFirst().getX() - getSecond().getX());
        int height = (int) Math.abs(getFirst().getY() - getSecond().getY());
        painter.paintRectangle(minX, minY, width, height);
    }
}
