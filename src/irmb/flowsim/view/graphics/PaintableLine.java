package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Line;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableLine extends Line implements Paintable {

    private final Line line;

    public PaintableLine() {
        line = new Line();
    }

    public PaintableLine(Line line) {
        this.line = line;
    }

    @Override
    public void paint(Painter painter) {
        painter.paintLine((int) getFirst().getX(), (int) getFirst().getY(), (int) getSecond().getX(), (int) getSecond().getY());
    }
}
