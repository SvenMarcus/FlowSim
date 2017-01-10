package irmb.test.view;

import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.swing.SwingGraphicView;
import irmb.flowsim.view.swing.SwingPainter;
import irmb.flowsim.view.graphics.PaintableShape;

/**
 * Created by Sven on 14.12.2016.
 */
public class SwingGraphicViewFake extends SwingGraphicView {


    @Override
    public void update() {
        for (PaintableShape p : presenter.getPaintableList())
            p.paint(painter);
    }

    public void setPainter(Painter painter) {
        this.painter = (SwingPainter) painter;
    }


}
