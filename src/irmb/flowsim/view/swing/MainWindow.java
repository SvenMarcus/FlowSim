package irmb.flowsim.view.swing;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.simulation.visualization.PlotStyle;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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
    private JButton bezierButton;
    private JButton clearButton;
    private JButton pauseSimulationButton;
    private JButton runSimulationButton;
    private JButton removeSimulationButton;
    private JPanel topPanel;
    private JToolBar mainToolBar;

    private final JMenuItem lineMenuItem;
    private final JMenuItem rectangleMenuItem;
    private final JMenuItem polyLineMenuItem;
    private final JMenuItem bezierMenuItem;
    private final JMenuItem undo;
    private final JMenuItem redo;
    private final JMenuItem clear;

    private SimulationGraphicViewPresenter presenter;
    private CoordinateTransformer transformer;


    public MainWindow(CoordinateTransformer transformer) {
        this.transformer = transformer;
        $$$setupUI$$$();
        setSize(800, 600);
        add(contentPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem close = new JMenuItem("Close");
        close.addActionListener(e -> System.exit(1));
        fileMenu.add(close);
        menuBar.add(fileMenu);

        JMenu editMenu = new JMenu("Edit");
        undo = new JMenuItem("Undo");
        redo = new JMenuItem("Redo");
        clear = new JMenuItem("Clear");
        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.add(clear);
        menuBar.add(editMenu);

        JMenu shapesMenu = new JMenu("Shapes");
        lineMenuItem = new JMenuItem("Line");
        rectangleMenuItem = new JMenuItem("Rectangle");
        polyLineMenuItem = new JMenuItem("PolyLine");
        bezierMenuItem = new JMenuItem("Bezier Curve");
        shapesMenu.add(lineMenuItem);
        shapesMenu.add(rectangleMenuItem);
        shapesMenu.add(polyLineMenuItem);
        shapesMenu.add(bezierMenuItem);
        menuBar.add(shapesMenu);

        JMenu visualizationMenu = new JMenu("Visualization");
        JCheckBoxMenuItem colorPlotItem = new JCheckBoxMenuItem("Color Plot");
        colorPlotItem.addActionListener(e -> presenter.togglePlotStyle(PlotStyle.Color));
        JCheckBoxMenuItem arrowPlotItem = new JCheckBoxMenuItem("Arrow Plot");
        arrowPlotItem.addActionListener(e -> presenter.togglePlotStyle(PlotStyle.Arrow));
        visualizationMenu.add(colorPlotItem);
        visualizationMenu.add(arrowPlotItem);
        menuBar.add(visualizationMenu);


        topPanel.add(menuBar, BorderLayout.NORTH);
        initListeners();
        scaleButtonIcons();
    }

    private void scaleButtonIcons() {
        ImageIcon lineIcon = (ImageIcon) lineButton.getIcon();
        lineButton.setIcon(new ImageIcon(lineIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon rectangleIcon = (ImageIcon) rectangleButton.getIcon();
        rectangleButton.setIcon(new ImageIcon(rectangleIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon polyLineIcon = (ImageIcon) polyLineButton.getIcon();
        polyLineButton.setIcon(new ImageIcon(polyLineIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon bezierIcon = (ImageIcon) bezierButton.getIcon();
        bezierButton.setIcon(new ImageIcon(bezierIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon runIcon = (ImageIcon) runSimulationButton.getIcon();
        runSimulationButton.setIcon(new ImageIcon(runIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon addIcon = (ImageIcon) addSimulationButton.getIcon();
        addSimulationButton.setIcon(new ImageIcon(addIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon pauseIcon = (ImageIcon) pauseSimulationButton.getIcon();
        pauseSimulationButton.setIcon(new ImageIcon(pauseIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon removeIcon = (ImageIcon) removeSimulationButton.getIcon();
        removeSimulationButton.setIcon(new ImageIcon(removeIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon undoIcon = (ImageIcon) undoButton.getIcon();
        undoButton.setIcon(new ImageIcon(undoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon redoIcon = (ImageIcon) redoButton.getIcon();
        redoButton.setIcon(new ImageIcon(redoIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));

        ImageIcon clearIcon = (ImageIcon) clearButton.getIcon();
        clearButton.setIcon(new ImageIcon(clearIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
    }


    private void initListeners() {
        ActionListener lineListener = e -> presenter.beginPaint("Line");
        ActionListener rectangleListener = e -> presenter.beginPaint("Rectangle");
        ActionListener polyLineListener = e -> presenter.beginPaint("PolyLine");
        ActionListener bezierListener = e -> presenter.beginPaint("Bezier");
        ActionListener addSimulationListener = e -> presenter.addSimulation();
        ActionListener runSimulationListener = e -> presenter.runSimulation();
        ActionListener pauseSimulationListener = e -> presenter.pauseSimulation();
        ActionListener removeSimulationListener = e -> presenter.removeSimulation();
        ActionListener undoListener = e -> presenter.undo();
        ActionListener redoListener = e -> presenter.redo();
        ActionListener clearListener = e -> presenter.clearAll();

        lineButton.addActionListener(lineListener);
        lineMenuItem.addActionListener(lineListener);
        rectangleButton.addActionListener(rectangleListener);
        rectangleMenuItem.addActionListener(rectangleListener);
        polyLineButton.addActionListener(polyLineListener);
        polyLineMenuItem.addActionListener(polyLineListener);
        bezierButton.addActionListener(bezierListener);
        bezierMenuItem.addActionListener(bezierListener);
        addSimulationButton.addActionListener(addSimulationListener);

        runSimulationButton.addActionListener(runSimulationListener);

        pauseSimulationButton.addActionListener(pauseSimulationListener);

        removeSimulationButton.addActionListener(removeSimulationListener);

        undoButton.addActionListener(undoListener);
        undo.addActionListener(undoListener);
        redoButton.addActionListener(redoListener);
        redo.addActionListener(redoListener);
        clearButton.addActionListener(clearListener);
        clear.addActionListener(clearListener);
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
        drawArea.setBackground(new Color(-1));
        contentPanel.add(drawArea, BorderLayout.CENTER);
        topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(0, 0));
        contentPanel.add(topPanel, BorderLayout.NORTH);
        mainToolBar = new JToolBar();
        mainToolBar.setOrientation(0);
        mainToolBar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        topPanel.add(mainToolBar, BorderLayout.CENTER);
        lineButton = new JButton();
        lineButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/Line2D.gif")));
        lineButton.setText("Line");
        mainToolBar.add(lineButton);
        rectangleButton = new JButton();
        rectangleButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/Rectangle2D.gif")));
        rectangleButton.setText("Rectangle");
        mainToolBar.add(rectangleButton);
        polyLineButton = new JButton();
        polyLineButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/Polyline2D.gif")));
        polyLineButton.setText("PolyLine");
        mainToolBar.add(polyLineButton);
        bezierButton = new JButton();
        bezierButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/Bezier2D.gif")));
        bezierButton.setText("Bezier Curve");
        mainToolBar.add(bezierButton);
        addSimulationButton = new JButton();
        addSimulationButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/add-simulation.png")));
        addSimulationButton.setText("Add Simulation");
        mainToolBar.add(addSimulationButton);
        final JToolBar.Separator toolBar$Separator1 = new JToolBar.Separator();
        mainToolBar.add(toolBar$Separator1);
        runSimulationButton = new JButton();
        runSimulationButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/continue.png")));
        runSimulationButton.setText("Run Simulation");
        mainToolBar.add(runSimulationButton);
        pauseSimulationButton = new JButton();
        pauseSimulationButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/pause.png")));
        pauseSimulationButton.setText("Pause Simulation");
        mainToolBar.add(pauseSimulationButton);
        removeSimulationButton = new JButton();
        removeSimulationButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/remove-simulation.png")));
        removeSimulationButton.setText("Remove Simulation");
        mainToolBar.add(removeSimulationButton);
        undoButton = new JButton();
        undoButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/edit-undo.png")));
        undoButton.setText("Undo");
        mainToolBar.add(undoButton);
        redoButton = new JButton();
        redoButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/edit-redo.png")));
        redoButton.setText("Redo");
        mainToolBar.add(redoButton);
        clearButton = new JButton();
        clearButton.setIcon(new ImageIcon(getClass().getResource("/irmb/flowsim/view/resources/edit-clear.png")));
        clearButton.setText("Clear");
        mainToolBar.add(clearButton);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return contentPanel;
    }
}
