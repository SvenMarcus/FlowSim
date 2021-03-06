package irmb.flowsim.view.javafx;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.simulation.visualization.PlotStyle;
import irmb.flowsim.view.graphics.Paintable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.CacheHint;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.MenuBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * Created by Sven on 22.12.2016.
 */
public class RootController implements GraphicView {

    @FXML
    private ButtonBar buttonBar;
    @FXML
    private Pane rootPane;
    @FXML
    private Button lineButton;
    @FXML
    private Button rectangleButton;
    @FXML
    private Button polyLineButton;
    @FXML
    private Button bezierButton;
    @FXML
    private Button runSimulationButton;
    @FXML
    private Button pauseSimulationButton;
    @FXML
    private Button undoButton;
    @FXML
    private Button redoButton;
    @FXML
    private Button clearButton;
    @FXML
    private Canvas drawPanel;
    @FXML
    private MenuBar menuBar;

    private SimulationGraphicViewPresenter presenter;
    private JavaFXPainter painter;
    private volatile CoordinateTransformer transformer;
    private GraphicsContext graphicsContext2D;
    private Runnable runnable;
    private JavaFXRepaintScheduler repaintScheduler;


    public RootController(CoordinateTransformer transformer) {
        this.transformer = transformer;
    }

    public void setPresenter(SimulationGraphicViewPresenter presenter) {
        this.presenter = presenter;
    }

    @FXML
    public void initialize() {
        menuBar.useSystemMenuBarProperty().set(true);

        drawPanel.heightProperty().bind(rootPane.heightProperty());
        drawPanel.widthProperty().bind(rootPane.widthProperty());
        drawPanel.heightProperty().addListener(o -> update());
        drawPanel.widthProperty().addListener(o -> update());
        drawPanel.setOnScroll(event -> presenter.handleScroll(event.getX(), event.getY(), (int) event.getDeltaY()));
        drawPanel.setCache(true);
        drawPanel.setCacheHint(CacheHint.SPEED);
        runnable = () -> {
            graphicsContext2D = drawPanel.getGraphicsContext2D();
            if (painter == null)
                painter = new JavaFXPainter();
            painter.setGraphicsContext(graphicsContext2D);
            graphicsContext2D.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
            graphicsContext2D.setFill(Color.WHITE);
            graphicsContext2D.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
            graphicsContext2D.save();
            for (Paintable p : presenter.getPaintableList()) {
                p.paint(painter, transformer);
            }
            graphicsContext2D.restore();
        };
        repaintScheduler = new JavaFXRepaintScheduler(runnable);
        repaintScheduler.setDelay(16);
        repaintScheduler.start();

    }

    public void onLineButtonClick(ActionEvent event) {
        presenter.beginPaint("Line");
    }

    public void onRectangleButtonClick(ActionEvent event) {
        presenter.beginPaint("Rectangle");
    }

    public void onPolyLineButtonClick(ActionEvent event) {
        presenter.beginPaint("PolyLine");
    }

    public void onBezierButtonClick(ActionEvent event) {
        presenter.beginPaint("Bezier");
    }

    public void onAddSimulationButtonClick(ActionEvent event) {
        presenter.addSimulation();
    }

    public void onRunSimulationClick(ActionEvent event) {
        presenter.runSimulation();
    }

    public void onPauseSimulationClick(ActionEvent event) {
        presenter.pauseSimulation();
    }

    public void onRemoveSimulationClick(ActionEvent event) {
        presenter.removeSimulation();
    }

    public void onClearClick(ActionEvent event) {
        presenter.clearAll();
    }

    public void onCloseClick(ActionEvent event) {
        System.exit(1);
    }

    public void onColorPlotChecked(ActionEvent event) {
        presenter.togglePlotStyle(PlotStyle.Color);
    }

    public void onArrowPlotChecked(ActionEvent event) {
        presenter.togglePlotStyle(PlotStyle.Arrow);
    }

    public void onInfoPlotChecked(ActionEvent event) {
        presenter.togglePlotStyle(PlotStyle.Info);
    }

    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            presenter.handleLeftClick(event.getX(), event.getY());
        else if (event.getButton() == MouseButton.SECONDARY)
            presenter.handleRightClick(event.getX(), event.getY());
        else if (event.getButton() == MouseButton.MIDDLE)
            presenter.handleMiddleClick(event.getX(), event.getY());
    }

    public void onMouseMoved(MouseEvent event) {
        presenter.handleMouseMove(event.getX(), event.getY());
    }

    public void onMouseDragged(MouseEvent event) {
        presenter.handleMouseDrag(event.getX(), event.getY());
    }

    public void onMouseReleased(MouseEvent event) {
        presenter.handleMouseRelease();
    }

    public void onUndoButtonClick(ActionEvent event) {
        presenter.undo();
    }

    public void onRedoButtonClick(ActionEvent event) {
        presenter.redo();
    }


    @Override
    public void update() {
        repaintScheduler.needsUpdate(true);
    }

}
