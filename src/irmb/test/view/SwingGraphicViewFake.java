package irmb.test.view;

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.swing.SwingGraphicView;
import irmb.flowsim.view.swing.SwingPainter;
import irmb.flowsim.view.graphics.Paintable;

/**
 * Created by Sven on 14.12.2016.
 */
public class SwingGraphicViewFake extends SwingGraphicView {

    public SwingGraphicViewFake(GraphicViewPresenter presenter) {
        super(presenter);
    }

    @Override
    public void update() {
        for (Paintable p : presenter.getPaintableList())
            p.paint(painter);
    }

    public void setPainter(Painter painter) {
        this.painter = (SwingPainter) painter;
    }


}
