package irmb.flowsim.model.util;

import Jama.Matrix;
import irmb.flowsim.model.Point;

/**
 * Created by Sven on 10.01.2017.
 */
public class CoordinateTransformerImpl implements CoordinateTransformer {

    private Point worldTopLeft;
    private Point worldBottomRight;
    private Point viewTopLeft;
    private Point viewBottomRight;
    private double worldMidX;
    private double worldMidY;
    private double viewMidX;
    private double viewMidY;
    private Matrix translationMatrix;

    @Override
    public Point transformToPointOnScreen(Point point) {
        calculateMiddleValues();
        translationMatrix = makeTranslationMatrix();

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
        return 0.9 * Math.min(Vx, Vy);
    }

    private Matrix makeTranslationMatrix() {
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
        translationMatrix = makeTranslationMatrix().inverse();

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
}
