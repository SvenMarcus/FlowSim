package irmb.flowsim.view.swing;

import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.view.swing.SwingGraphicView;

import javax.swing.*;

/**
 * Created by Sven on 15.12.2016.
 */
public class MainWindow extends JFrame {
    private JButton lineButton;
    private JButton rectangleButton;
    private JButton polyLineButton;
    private JPanel drawArea;
    private JPanel contentPanel;
    private GraphicViewPresenter presenter;

    public MainWindow(GraphicViewPresenter presenter) {
        this.presenter = presenter;
        setSize(800, 600);
        add(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initListeners();
    }


    private void initListeners() {
        lineButton.addActionListener(e -> presenter.beginPaint("Line"));
        rectangleButton.addActionListener(e -> presenter.beginPaint("Rectangle"));
        polyLineButton.addActionListener(e -> presenter.beginPaint("PolyLine"));
    }


    private void createUIComponents() {
        drawArea = new SwingGraphicView(presenter);
    }

    public GraphicView getGraphicView() {
        return (GraphicView) drawArea;
    }
}
