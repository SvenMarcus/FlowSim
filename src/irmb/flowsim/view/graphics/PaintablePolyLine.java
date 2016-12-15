package irmb.flowsim.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.Painter;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLine extends PolyLine implements Paintable {


    private PolyLine polyLine;

    public PaintablePolyLine() {
        polyLine = new PolyLine();
    }

    public PaintablePolyLine(PolyLine polyLine) {
        this.polyLine = polyLine;
    }

    @Override
    public void paint(Painter painter) {
        for (int i = 0; i < getPointList().size() - 1; i++) {
            Point current = getPointList().get(i);
            Point next = getPointList().get(i + 1);
            painter.paintLine((int) current.getX(), (int) current.getY(), (int) next.getX(), (int) next.getY());
        }
    }
}
