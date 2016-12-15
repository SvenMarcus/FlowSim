package irmb.test.view;

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.GraphicViewImpl;
import irmb.flowsim.view.SwingPainter;

import java.awt.*;

/**
 * Created by Sven on 14.12.2016.
 */
public class GraphicViewImplFake extends GraphicViewImpl {

    public GraphicViewImplFake(GraphicViewPresenter presenter) {
        super(presenter);
    }

    @Override
    public void update() {
        presenter.getShape().paint(painter);
    }

    public void setPainter(Painter painter) {
        this.painter = (SwingPainter) painter;
    }


}
