package irmb.flowsim.view.swing;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;

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
    private JButton addSimulationButton;
    private SimulationGraphicViewPresenter presenter;
    private CoordinateTransformer transformer;

    public MainWindow(CoordinateTransformer transformer) {
        this.transformer = transformer;
        setSize(800, 600);
        add(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initListeners();
    }


    private void initListeners() {
        lineButton.addActionListener(e -> presenter.beginPaint("Line"));
        rectangleButton.addActionListener(e -> presenter.beginPaint("Rectangle"));
        polyLineButton.addActionListener(e -> presenter.beginPaint("PolyLine"));
        addSimulationButton.addActionListener(e -> {
            presenter.addSimulation();
            presenter.runSimulation();
        });
    }


    private void createUIComponents() {
        drawArea = new SwingGraphicView(transformer);
    }

    public GraphicView getGraphicView() {
        return (GraphicView) drawArea;
    }

    public void setPresenter(SimulationGraphicViewPresenter presenter) {
        this.presenter = presenter;
        ((SwingGraphicView) drawArea).setPresenter(presenter);
    }
}
