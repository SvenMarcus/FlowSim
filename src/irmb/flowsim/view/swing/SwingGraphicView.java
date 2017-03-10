package irmb.flowsim.view.swing;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.view.graphics.Paintable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by Sven on 13.12.2016.
 */
public class SwingGraphicView extends JPanel implements GraphicView, MouseListener, MouseMotionListener, MouseWheelListener {


    protected GraphicViewPresenter presenter;
    protected SwingPainter painter;
    protected CoordinateTransformer transformer;

    public SwingGraphicView(CoordinateTransformer transformer) {
        this.transformer = transformer;
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
        painter = new SwingPainter();
    }

    public void setPresenter(GraphicViewPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void update() {
        repaint();
    }

    @Override
    public void setCoordinateTransformer(CoordinateTransformer transformer) {
        this.transformer = transformer;
//        transformer.setViewBounds(new Point(0, 0), new Point(getWidth(), getHeight()));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (presenter != null) {
            painter.setGraphics(g);
            for (Paintable p : presenter.getPaintableList())
                p.paint(painter, transformer);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1)
            presenter.handleLeftClick(e.getX(), e.getY());
        else if (e.getButton() == 3)
            presenter.handleRightClick(0, 0);
        else if (e.getButton() == MouseEvent.BUTTON2)
            presenter.handleMiddleClick(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        presenter.handleMouseRelease();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        presenter.handleMouseDrag(e.getX(), e.getY());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (presenter != null)
            presenter.handleMouseMove(e.getX(), e.getY());
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        presenter.handleScroll(e.getX(), e.getY(), e.getWheelRotation());
    }
}
