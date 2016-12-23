package irmb.flowsim.presentation;

import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.Paintable;
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
    private List<Paintable> shapeList = new LinkedList<>();
    private PaintableShapeBuilder shapeBuilder;
    private Point clickedPoint;
    private Paintable shapeToMove;

    private boolean moved;
    private Point origin;
    private double dx;
    private double dy;
    private Paintable lastMovedShape;
    private boolean shapeAdded;

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
            for (Paintable p : shapeList) {
                clickedPoint = new Point(x, y);
                origin = new Point(x, y);
                if (p.isPointOnBoundary(new Point(x, y), 3))
                    shapeToMove = p;
            }
        }
    }

    private void addShapeToList() {
        if (!shapeList.contains(shapeBuilder.getShape())) {
            shapeList.add(shapeBuilder.getShape());
            moved = false;
        }
    }

    private void addPointToShape(double x, double y) {
        shapeBuilder.addPoint(new Point(x, y));
        pointsAdded++;
    }

    private boolean hasShapeBuilder() {
        return shapeBuilder != null;
    }

    private void resetBuilderWhenFinished() {
        if (shapeBuilder.isObjectFinished())
            shapeBuilder = null;
    }

    public void handleRightClick() {
        if (hasShapeBuilder()) {
            if (pointsAdded > 2) {
                shapeBuilder.removeLastPoint();
            } else
                shapeList.remove(shapeBuilder.getShape());
            graphicView.update();
            shapeBuilder = null;
        }
    }

    public void beginPaint(String objectType) {
        shapeBuilder = factory.makeShapeBuilder(objectType);
        pointsAdded = 0;
    }

    public List<Paintable> getPaintableList() {
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
        if (shapeToMove != null) {
            moveShape(x, y);
            graphicView.update();
        }

    }

    private void moveShape(double x, double y) {
        double dx = x - clickedPoint.getX();
        double dy = y - clickedPoint.getY();

        shapeToMove.moveBy(dx, dy);

        clickedPoint.setX(x);
        clickedPoint.setY(y);

        moved = true;
    }

    public void handleMouseRelease(double x, double y) {
        dx = x - origin.getX();
        dy = y - origin.getY();
        lastMovedShape = shapeToMove;
        shapeToMove = null;
    }

    public void undo() {
        if (moved) {
            lastMovedShape.moveBy(-dx, -dy);
            moved = false;
        } else {
            shapeList.remove(shapeList.size() - 1);
            shapeAdded = false;
        }
        graphicView.update();
    }

    public void redo() {

    }
}
