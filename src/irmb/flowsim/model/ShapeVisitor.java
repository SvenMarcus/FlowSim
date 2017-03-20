package irmb.flowsim.model;

/**
 * Created by sven on 15.03.17.
 */
public interface ShapeVisitor {
    void visit(Line line);

    void visit(Rectangle rectangle);

    void visit(PolyLine polyLine);

    void visit(Point point);

    void visit(BezierCurve bezierCurve);
}
