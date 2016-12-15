package irmb.flowsim.view;

import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;

import javax.swing.*;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewImpl extends JPanel implements GraphicView {


    protected GraphicViewPresenter presenter;
    protected SwingPainter painter;

    public GraphicViewImpl(GraphicViewPresenter presenter) {
        this.presenter = presenter;
        painter = new SwingPainter(getGraphics());
    }

    @Override
    public void update() {
        super.repaint();
        presenter.getShape().paint(painter);
    }

}
