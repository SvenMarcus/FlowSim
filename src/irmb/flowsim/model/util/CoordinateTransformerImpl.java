package irmb.flowsim.model.util;

import Jama.Matrix;
import irmb.flowsim.model.Point;

/**
 * Created by Sven on 10.01.2017.
 */
public class CoordinateTransformerImpl implements CoordinateTransformer {

    protected Point worldTopLeft;
    protected Point worldBottomRight;
    protected Point viewTopLeft;
    protected Point viewBottomRight;
    private double worldMidX;
    private double worldMidY;
    private double viewMidX;
    private double viewMidY;
    private boolean needsUpdate;
    private Matrix transformationMatrix;
    private Matrix inverse;

    public CoordinateTransformerImpl() {
        needsUpdate = true;
    }

    @Override
    public Point transformToPointOnScreen(Point point) {
        makeTransformationMatrix();

        Matrix pointMatrix = new Matrix(1, 3);
        pointMatrix.set(0, 0, point.getX());
        pointMatrix.set(0, 1, point.getY());
        pointMatrix.set(0, 2, 1);
        
        pointMatrix = pointMatrix.times(transformationMatrix);

        double x = pointMatrix.get(0, 0);
        double y = pointMatrix.get(0, 1);
        return new Point(x, y);
    }

    private double getScaleFactor() {
        double Vx = getDelta(viewTopLeft.getX(), viewBottomRight.getX()) / getDelta(worldTopLeft.getX(), worldBottomRight.getX());
        double Vy = getDelta(viewTopLeft.getY(), viewBottomRight.getY()) / getDelta(worldTopLeft.getY(), worldBottomRight.getY());
        return Math.min(Vx, Vy);
    }

    private void makeTransformationMatrix() {
        if (needsUpdate) {
            calculateMiddleValues();
            double s = getScaleFactor();
            double tx = viewMidX - worldMidX;
            double ty = viewMidY - worldMidY;

            transformationMatrix = makeTranslation(-worldMidX, -worldMidY);
            transformationMatrix = transformationMatrix.times(makeScaling(s));
            transformationMatrix = transformationMatrix.times(makeTranslation(worldMidX, worldMidY));
            transformationMatrix = transformationMatrix.times(makeTranslation(tx, ty));
            inverse = transformationMatrix.inverse();
            needsUpdate = false;
        }
    }

    private Matrix makeScaling(double s) {
        return new Matrix(new double[][]{
                {s, 0, 0},
                {0, -s, 0},
                {0, 0, 1},
        });
    }

    private Matrix makeTranslation(double tx, double ty) {
        return new Matrix(new double[][]{
                {1, 0, 0},
                {0, 1, 0},
                {tx, ty, 1}
        });
    }

    private double getDelta(double min, double max) {
        return Math.abs(max - min);
    }

    private double getMiddle(double min, double max) {
        return (max + min) / 2;
    }

    @Override
    public Point transformToWorldPoint(Point point) {
        makeTransformationMatrix();

        Matrix pointMatrix = new Matrix(1, 3);
        pointMatrix.set(0, 0, point.getX());
        pointMatrix.set(0, 1, point.getY());
        pointMatrix.set(0, 2, 1);


        pointMatrix = pointMatrix.times(inverse);

        double x = pointMatrix.get(0, 0);
        double y = pointMatrix.get(0, 1);
        return new Point(x, y);
    }

    private void calculateMiddleValues() {
        worldMidX = getMiddle(worldTopLeft.getX(), worldBottomRight.getX());
        worldMidY = getMiddle(worldTopLeft.getY(), worldBottomRight.getY());

        viewMidX = getMiddle(viewTopLeft.getX(), viewBottomRight.getX());
        viewMidY = getMiddle(viewTopLeft.getY(), viewBottomRight.getY());
    }

    @Override
    public void setWorldBounds(Point topLeft, Point bottomRight) {
        this.worldTopLeft = topLeft;
        this.worldBottomRight = bottomRight;
        this.needsUpdate = true;
    }

    @Override
    public void setViewBounds(Point topLeft, Point bottomRight) {
        this.viewTopLeft = topLeft;
        this.viewBottomRight = bottomRight;
        this.needsUpdate = true;
    }

    @Override
    public void moveViewWindow(double dx, double dy) {
        viewTopLeft.setX(viewTopLeft.getX() + dx);
        viewTopLeft.setY(viewTopLeft.getY() + dy);
        viewBottomRight.setX(viewBottomRight.getX() + dx);
        viewBottomRight.setY(viewBottomRight.getY() + dy);
        this.needsUpdate = true;
    }

    @Override
    public double scaleToScreenLength(double length) {
        return getScaleFactor() * length;
    }

    @Override
    public double scaleToWorldLength(double length) {
        return length / getScaleFactor();
    }

    @Override
    public void zoomWindow(double zoomFactor, double worldX, double worldY) {
        double zoom = 1 - zoomFactor;
        calculateMiddleValues();
        double deltaX = worldX * zoomFactor;
        double deltaY = worldY * zoomFactor;

        worldTopLeft.setX(worldTopLeft.getX() * zoom + deltaX);
        worldTopLeft.setY(worldTopLeft.getY() * zoom + deltaY);
        worldBottomRight.setX(worldBottomRight.getX() * zoom + deltaX);
        worldBottomRight.setY(worldBottomRight.getY() * zoom + deltaY);
        this.needsUpdate = true;
    }

}
