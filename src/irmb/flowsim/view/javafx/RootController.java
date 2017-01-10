package irmb.flowsim.view.javafx;

import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.view.graphics.PaintableShape;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
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
    private Canvas drawPanel;

    private GraphicViewPresenter presenter;
    private JavaFXPainter painter;

    public RootController(GraphicViewPresenter presenter) {
        this.presenter = presenter;

    }

    @FXML
    public void initialize() {
        drawPanel.heightProperty().bind(rootPane.heightProperty());
        drawPanel.widthProperty().bind(rootPane.widthProperty());
        drawPanel.heightProperty().addListener(o -> {
            update();
        });
        drawPanel.widthProperty().addListener(o -> {
            update();
        });
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

    public void onMousePressed(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY)
            presenter.handleLeftClick(event.getX(), event.getY());
        else if (event.getButton() == MouseButton.SECONDARY)
            presenter.handleRightClick();
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
        GraphicsContext graphicsContext2D = drawPanel.getGraphicsContext2D();
        graphicsContext2D.clearRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
        graphicsContext2D.setFill(Color.STEELBLUE);
        graphicsContext2D.fillRect(0, 0, drawPanel.getWidth(), drawPanel.getHeight());
        if (painter == null)
            painter = new JavaFXPainter(graphicsContext2D);
        for (PaintableShape p : presenter.getPaintableList()) {
            p.paint(painter);
        }
    }
}
