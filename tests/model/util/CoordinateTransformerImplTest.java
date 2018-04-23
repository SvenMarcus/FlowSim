package model.util;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import org.junit.Before;
import org.junit.Test;

import static util.TestUtil.*;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by Sven on 10.01.2017.
 */
public class CoordinateTransformerImplTest extends CoordinateTransformerImpl {

    private CoordinateTransformerImplTest sut;
    private Point topLeft;
    private Point bottomRight;
    private Point worldTopLeft;
    private Point worldBottomRight;

    @Before
    public void setUp() throws Exception {
        sut = new CoordinateTransformerImplTest();
        sut.setWorldBounds(makePoint(-15, 15), makePoint(10, -25));
        sut.setViewBounds(makePoint(0, 0), makePoint(800, 600));
        topLeft = makePoint(0, 0);
        bottomRight = makePoint(800, 600);
    }

    @Test
    public void testTranslation() {
        sut.setWorldBounds(makePoint(0, 25), makePoint(10, 0));
        sut.setViewBounds(makePoint(0, 5), makePoint(10, 30));

        Point p = makePoint(0, 0);
        Point result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(0, 30), result);
    }

    @Test
    public void testScaling() {
        sut.setWorldBounds(makePoint(-25, 25), makePoint(25, -25));
        sut.setViewBounds(makePoint(-25, -25), makePoint(25, 25));

        Point origin = makePoint(0, 0);
        Point result = sut.transformToPointOnScreen(origin);
        assertExpectedPointEqualsActual(makePoint(0, 0), result);

        Point topLeft = makePoint(-25, 25);
        Point bottomRight = makePoint(25, -25);

        double distance = getDistance(topLeft, bottomRight);

        sut.setViewBounds(makePoint(-50, -50), makePoint(50, 50));
        Point topLeftView = sut.transformToPointOnScreen(topLeft);
        Point bottomRightView = sut.transformToPointOnScreen(bottomRight);
        assertEquals(distance * 2, getDistance(topLeftView, bottomRightView));
    }

    private double getDistance(Point first, Point second) {
        double dx = first.getX() - second.getX();
        double dy = first.getY() - second.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Test
    public void testTransformToPointOnScreen() {
        Point p = makePoint(0, 0);

        Point result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(437.5, 225), result);

        p = makePoint(-3, 12);

        result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(392.5, 45), result);

        p = makePoint(-18, 20);

        result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(167.5, -75), result);
    }

    @Test
    public void testTransformToWorldPoint() {
        Point p = makePoint(437.5, 225);

        Point result = sut.transformToWorldPoint(p);
        assertExpectedPointEqualsActual(makePoint(0, 0), result);

        p = makePoint(392.5, 45);

        result = sut.transformToWorldPoint(p);
        assertExpectedPointEqualsActual(makePoint(-3, 12), result);

        p = makePoint(167.5, -75);

        result = sut.transformToWorldPoint(p);
        assertExpectedPointEqualsActual(makePoint(-18, 20), result);
    }

    @Test
    public void testMoveViewWindow() {
        sut.moveViewWindow(5, 23);
        assertExpectedPointEqualsActual(makePoint(5, 23), sut.viewTopLeft);
        assertExpectedPointEqualsActual(makePoint(805, 623), sut.viewBottomRight);
    }

    @Test
    public void testScaleToScreenLength() {
        double screenLength = sut.scaleToScreenLength(5);
        assertEquals(5 * 15, screenLength, DELTA);

        screenLength = sut.scaleToScreenLength(34);
        assertEquals(34 * 15, screenLength, DELTA);
    }

    @Test
    public void testScaleToWorldLength() {
        double worldLength = sut.scaleToWorldLength(15);
        assertEquals(1, worldLength, DELTA);

        worldLength = sut.scaleToWorldLength(75);
        assertEquals(75 / 15, worldLength, DELTA);

        sut.setWorldBounds(makePoint(0, 0), makePoint(800, 600));
        sut.setViewBounds(makePoint(-15, 15), makePoint(10, -25));

        double expectedScaleFactor = 0.03125;
        worldLength = sut.scaleToWorldLength(15);
        assertEquals(15 / expectedScaleFactor, worldLength);
    }

    @Test
    public void testZoomOutFromOriginWithSymmetricSystem() {
        sut.setWorldBounds(makePoint(-10, 25), makePoint(10, -25));

        sut.zoomWindow(-0.05, 0, 0);

        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        assertExpectedPointEqualsActual(makePoint(-35, 26.25), worldTopLeft);
        assertExpectedPointEqualsActual(makePoint(35, -26.25), worldBottomRight);
    }

    @Test
    public void testZoomOutWithOffsetWithSymmetricSystem() {
        sut.setWorldBounds(makePoint(-10, 25), makePoint(10, -25));

        sut.zoomWindow(-0.05, 5, -7);

        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        assertExpectedPointEqualsActual(makePoint(-35.25, 26.6), worldTopLeft);
        assertExpectedPointEqualsActual(makePoint(34.75, -25.9), worldBottomRight);
    }

    @Test
    public void testZoomOutWithoutOffsetWithAsymmetricSystem() {
        sut.zoomWindow(-0.05, -2.5, -5);

        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        assertExpectedPointEqualsActual(makePoint(-30.5, 16), worldTopLeft);
        assertExpectedPointEqualsActual(makePoint(25.5, -26), worldBottomRight);
    }

    @Test
    public void testZoomOutWithOffsetWithAsymmetricSystem() {
        sut.zoomWindow(-0.05, 5, -7);

        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        Point topLeftExpected = makePoint(-30.875, 16.1);
        Point bottomRightExpected = makePoint(25.125, -25.9);
        assertExpectedPointEqualsActual(topLeftExpected, worldTopLeft);
        assertExpectedPointEqualsActual(bottomRightExpected, worldBottomRight);
    }

    @Test
    public void testZoomInWithOffsetAndSymmetricSystem() {
        sut.setWorldBounds(makePoint(-10, 25), makePoint(10, -25));

        sut.zoomWindow(0.05, 5, 7);
        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        Point topLeftExpected = makePoint(-31.41666667, 24.1);
        Point bottomRightExpected = makePoint(31.91666667, -23.4);
        assertExpectedPointEqualsActual(topLeftExpected, worldTopLeft, 0.02);
        assertExpectedPointEqualsActual(bottomRightExpected, worldBottomRight, 0.02);
    }

    @Test
    public void testZoomInWithOffsetAndAsymmetricSystem() {
        sut.zoomWindow(0.05, 5, -7);

        worldTopLeft = sut.transformToWorldPoint(topLeft);
        worldBottomRight = sut.transformToWorldPoint(bottomRight);

        Point topLeftExpected = makePoint(-27.45833333, 13.9);
        Point bottomRightExpected = makePoint(23.20833333, -24.1);
        assertExpectedPointEqualsActual(topLeftExpected, worldTopLeft, 0.02);
        assertExpectedPointEqualsActual(bottomRightExpected, worldBottomRight, 0.02);
    }
}