package irmb.flowsim.view.swing;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;

import javax.swing.*;
import java.awt.*;

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
    private JButton undoButton;
    private JButton redoButton;
    private JToolBar mainToolBar;
    private JButton bezierButton;
    private SimulationGraphicViewPresenter presenter;
    private CoordinateTransformer transformer;

    public MainWindow(CoordinateTransformer transformer) {
        this.transformer = transformer;
        $$$setupUI$$$();
        setSize(800, 600);
        add(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initListeners();
    }


    private void initListeners() {
        lineButton.addActionListener(e -> presenter.beginPaint("Line"));
        rectangleButton.addActionListener(e -> presenter.beginPaint("Rectangle"));
        polyLineButton.addActionListener(e -> presenter.beginPaint("PolyLine"));
        bezierButton.addActionListener(e -> presenter.beginPaint("Bezier"));
        addSimulationButton.addActionListener(e -> {
            presenter.addSimulation();
            presenter.runSimulation();
        });
        undoButton.addActionListener(e -> presenter.undo());
        redoButton.addActionListener(e -> presenter.redo());
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(0, 0));
        mainToolBar = new JToolBar();
        mainToolBar.setOrientation(0);
        mainToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        contentPanel.add(mainToolBar, BorderLayout.NORTH);
        lineButton = new JButton();
        lineButton.setText("Line");
        mainToolBar.add(lineButton);
        rectangleButton = new JButton();
        rectangleButton.setText("Rectangle");
        mainToolBar.add(rectangleButton);
        polyLineButton = new JButton();
        polyLineButton.setText("PolyLine");
        mainToolBar.add(polyLineButton);
        bezierButton = new JButton();
        bezierButton.setText("Bezier Curve");
        mainToolBar.add(bezierButton);
        addSimulationButton = new JButton();
        addSimulationButton.setText("Add Simulation");
        mainToolBar.add(addSimulationButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        mainToolBar.add(toolBar$Separator1);
        undoButton = new JButton();
        undoButton.setText("Undo");
        mainToolBar.add(undoButton);
        redoButton = new JButton();
        redoButton.setText("Redo");
        mainToolBar.add(redoButton);
        drawArea.setBackground(new Color(-10257778));
        contentPanel.add(drawArea, BorderLayout.CENTER);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
