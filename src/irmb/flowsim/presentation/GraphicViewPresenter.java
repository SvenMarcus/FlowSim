package irmb.flowsim.presentation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Shape;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.presentation.command.MoveShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter {

    private GraphicView graphicView;
    private PaintableShapeBuilderFactory factory;

    private int pointsAdded = 0;
    private List<PaintableShape> shapeList = new LinkedList<>();
    private PaintableShapeBuilder shapeBuilder;
    private Point clickedPoint;

    protected List<Command> commandList = new LinkedList<>();
    private int currentIndex = -1;

    private MoveShapeCommand moveShapeCommand;
    private AddPaintableShapeCommand addPaintableShapeCommand;

    public GraphicViewPresenter(PaintableShapeBuilderFactory factory) {
        this.factory = factory;
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
    }

    public void handleLeftClick(double x, double y) {
        if (hasShapeBuilder()) {
            addPointToShape(x, y);
            addShapeToList();
            if (pointsAdded >= 2) {
                graphicView.update();
                resetBuilderWhenFinished();
            }
        } else {
            for (PaintableShape p : shapeList) {
                clickedPoint = new Point(x, y);
                if (p.isPointOnBoundary(new Point(x, y), 3)) {
                    moveShapeCommand = new MoveShapeCommand(p.getShape());
                }
            }
        }
    }

    private void addShapeToList() {
        addPaintableShapeCommand = new AddPaintableShapeCommand(shapeBuilder.getShape(), shapeList);
        addPaintableShapeCommand.execute();
    }

    private void addPointToShape(double x, double y) {
        shapeBuilder.addPoint(new Point(x, y));
        pointsAdded++;
    }

    private boolean hasShapeBuilder() {
        return shapeBuilder != null;
    }

    private void resetBuilderWhenFinished() {
        if (shapeBuilder.isObjectFinished()) {
            shapeBuilder = null;
            addCommand(addPaintableShapeCommand);
        }
    }

    public void handleRightClick() {
        if (hasShapeBuilder()) {
            if (pointsAdded > 2) {
                shapeBuilder.removeLastPoint();
                addCommand(addPaintableShapeCommand);
            } else
                addPaintableShapeCommand.undo();
            graphicView.update();
            shapeBuilder = null;
        }
    }

    public void beginPaint(String objectType) {
        shapeBuilder = factory.makeShapeBuilder(objectType);
        pointsAdded = 0;
    }

    public List<PaintableShape> getPaintableList() {
        return shapeList;
    }

    public void handleMouseMove(double x, double y) {
        if (hasShapeBuilder()) {
            if (pointsAdded == 1) {
                addPointToShape(x, y);
            } else if (pointsAdded > 1)
                shapeBuilder.setLastPoint(new Point(x, y));
            if (pointsAdded > 0)
                graphicView.update();
        }
    }

    public void handleMouseDrag(double x, double y) {
        if (moveShapeCommand != null) {
            moveShape(x, y);
            graphicView.update();
        }

    }

    private void moveShape(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        moveShapeCommand.setDelta(dx, dy);
        moveShapeCommand.execute();

        clickedPoint.setX(x);
        clickedPoint.setY(y);

    }

    public void handleMouseRelease(double x, double y) {
        if (moveShapeCommand != null)
            addCommand(moveShapeCommand);
        moveShapeCommand = null;
    }

    private void addCommand(Command command) {
        while (commandList.size() - 1 > currentIndex)
            commandList.remove(commandList.size() - 1);
        commandList.add(command);
        currentIndex++;
    }

    public void undo() {
        if (currentIndex > -1) {
            commandList.get(currentIndex--).undo();
            graphicView.update();
        }
    }

    public void redo() {
        int size = commandList.size();
        if (currentIndex < size - 1) {
            commandList.get(++currentIndex).redo();
            graphicView.update();
        }
    }
}
