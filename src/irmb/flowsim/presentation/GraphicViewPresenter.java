package irmb.flowsim.presentation;

import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter {

    private GraphicView graphicView;
    private PaintableShapeBuilderFactory factory;

    private int timesClicked = 0;
    private Paintable shape;
    private PaintableShapeBuilder shapeBuilder;

    public GraphicViewPresenter(PaintableShapeBuilderFactory factory) {
        this.factory = factory;
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
    }

    public void handleLeftClick(int x, int y) {
        timesClicked++;
        if (hasShapeBuilder()) {
            shapeBuilder.addPoint(new Point(x, y));
            if (timesClicked >= 2) {
                shape = shapeBuilder.getShape();
                graphicView.update();
                if (shapeBuilder.isObjectFinished())
                    shapeBuilder = null;
            }
        }
    }

    private boolean hasShapeBuilder() {
        return shapeBuilder != null;
    }

    public void beginPaint(String objectType) {
        shapeBuilder = factory.makeShapeBuilder(objectType);
        timesClicked = 0;
    }

    public Paintable getShape() {
        return shape;
    }
}
