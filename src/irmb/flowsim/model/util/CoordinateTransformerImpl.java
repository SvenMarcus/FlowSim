package irmb.flowsim.model.util;

import Jama.Matrix;
import irmb.flowsim.model.Point;

import java.util.ArrayList;

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
    private Matrix translationMatrix;

    @Override
    public Point transformToPointOnScreen(Point point) {
        calculateMiddleValues();
        translationMatrix = makeTransformationMatrix();

        Matrix pointMatrix = new Matrix(1, 3);
        pointMatrix.set(0, 0, point.getX());
        pointMatrix.set(0, 1, point.getY());
        pointMatrix.set(0, 2, 1);


        pointMatrix = pointMatrix.times(translationMatrix);

        double x = pointMatrix.get(0, 0);
        double y = pointMatrix.get(0, 1);
        return new Point(x, y);
    }

    private double getScaleFactor() {
        double Vx = getDelta(viewTopLeft.getX(), viewBottomRight.getX()) / getDelta(worldTopLeft.getX(), worldBottomRight.getX());
        double Vy = getDelta(viewTopLeft.getY(), viewBottomRight.getY()) / getDelta(worldTopLeft.getY(), worldBottomRight.getY());
        return Math.min(Vx, Vy);
    }

    private Matrix makeTransformationMatrix() {
        double s = getScaleFactor();
        double tx = viewMidX - worldMidX;
        double ty = viewMidY - worldMidY;

        Matrix m = makeTranslation(-worldMidX, -worldMidY);
        m = m.times(makeScaling(s));
        m = m.times(makeTranslation(worldMidX, worldMidY));
        m = m.times(makeTranslation(tx, ty));
        return m;
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
        calculateMiddleValues();
        translationMatrix = makeTransformationMatrix().inverse();

        Matrix pointMatrix = new Matrix(1, 3);
        pointMatrix.set(0, 0, point.getX());
        pointMatrix.set(0, 1, point.getY());
        pointMatrix.set(0, 2, 1);


        pointMatrix = pointMatrix.times(translationMatrix);

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
    }

    @Override
    public void setViewBounds(Point topLeft, Point bottomRight) {
        this.viewTopLeft = topLeft;
        this.viewBottomRight = bottomRight;
    }

    @Override
    public void moveViewWindow(double dx, double dy) {
        viewTopLeft.setX(viewTopLeft.getX() + dx);
        viewTopLeft.setY(viewTopLeft.getY() + dy);
        viewBottomRight.setX(viewBottomRight.getX() + dx);
        viewBottomRight.setY(viewBottomRight.getY() + dy);
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
        calculateMiddleValues();
        worldTopLeft.setX(worldTopLeft.getX()*(1-zoomFactor)+(worldX-worldMidX)*zoomFactor);
        worldTopLeft.setY(worldTopLeft.getY()*(1-zoomFactor)+(worldY-worldMidY)*zoomFactor);
        worldBottomRight.setX(worldBottomRight.getX()*(1-zoomFactor)+(worldX-worldMidX)*zoomFactor);
        worldBottomRight.setY(worldBottomRight.getY()*(1-zoomFactor)+(worldY-worldMidY)*zoomFactor);
    }

}
