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

    public GraphicViewPresenter(PaintableShapeBuilderFactory factory) {
        this.factory = factory;
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
    }

    public void handleLeftClick(int x, int y) {
        if (hasShapeBuilder()) {
            addPointToShape(x, y);
            addShapeToList();
            if (pointsAdded >= 2) {
                graphicView.update();
                resetBuilderWhenFinished();
            }
        }
    }

    private void addShapeToList() {
        if (!shapeList.contains(shapeBuilder.getShape()))
            shapeList.add(shapeBuilder.getShape());
    }

    private void addPointToShape(int x, int y) {
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

    public void handleMouseMove(int x, int y) {
        if (hasShapeBuilder()) {
            if (pointsAdded == 1) {
                addPointToShape(x, y);
            } else if (pointsAdded > 1)
                shapeBuilder.setLastPoint(new Point(x, y));
            if (pointsAdded > 0)
                graphicView.update();
        }
    }
}
