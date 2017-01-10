package irmb.flowsim.view.swing;

import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.view.graphics.PaintableShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Sven on 13.12.2016.
 */
public class SwingGraphicView extends JPanel implements GraphicView, MouseListener, MouseMotionListener {


    protected GraphicViewPresenter presenter;
    protected SwingPainter painter;

    public SwingGraphicView() {
        addMouseListener(this);
        addMouseMotionListener(this);
        painter = new SwingPainter();
    }

    public void setPresenter(GraphicViewPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void update() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        painter.setGraphics(g);
        for (PaintableShape p : presenter.getPaintableList())
            p.paint(painter);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1)
            presenter.handleLeftClick(e.getX(), e.getY());
        else if (e.getButton() == 3)
            presenter.handleRightClick();
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
        presenter.handleMouseMove(e.getX(), e.getY());
    }
}
