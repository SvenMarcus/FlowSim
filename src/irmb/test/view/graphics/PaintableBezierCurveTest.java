package irmb.test.view.graphics;

import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.PaintableBezierCurve;
import org.junit.Before;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;

/**
 * Created by sven on 19.03.17.
 */
public class PaintableBezierCurveTest {

    private BezierCurve bezierCurve;
    private PaintableBezierCurve sut;

    @Before
    public void setUp() throws Exception {
        bezierCurve = new BezierCurve();
        bezierCurve.addPoint(makePoint(14, 13));
        bezierCurve.addPoint(makePoint(18, 16));
        bezierCurve.addPoint(makePoint(22, 12));
        sut = new PaintableBezierCurve(bezierCurve);
    }

    @Test
    public void givenPointOnBoundary_isPointOnBoundaryShouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(14, 13), 0.01));
        assertTrue(sut.isPointOnBoundary(makePoint(16, 14.06), 0.01));
        assertTrue(sut.isPointOnBoundary(makePoint(19, 14.01), 0.01));
        assertTrue(sut.isPointOnBoundary(makePoint(21.1, 12.8), 0.01));
        assertTrue(sut.isPointOnBoundary(makePoint(22, 12), 0.01));
    }

    @Test
    public void givenPointNotOnBoundary_isPointOnBoundaryShouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(13, 13), 0.01));
        assertFalse(sut.isPointOnBoundary(makePoint(18, 14.06), 0.01));
        assertFalse(sut.isPointOnBoundary(makePoint(19, 13.5), 0.01));
        assertFalse(sut.isPointOnBoundary(makePoint(22.1, 12.8), 0.01));
        assertFalse(sut.isPointOnBoundary(makePoint(22.2, 12), 0.01));
    }

    @Test
    public void givenPointAtDefinedPoint_shouldReturnDefinedPoint() {
        Point result = sut.getDefinedPoint(makePoint(14, 13), 0);
        assertExpectedPointEqualsActual(makePoint(14, 13), result);

        result = sut.getDefinedPoint(makePoint(18, 16), 0);
        assertExpectedPointEqualsActual(makePoint(18, 16), result);

        result = sut.getDefinedPoint(makePoint(22, 12), 0);
        assertExpectedPointEqualsActual(makePoint(22, 12), result);
    }

    @Test
    public void givenPointNearDefinedPoint_shouldReturnClosestDefinedPoint() {
        Point result = sut.getDefinedPoint(makePoint(12, 13), 2);
        assertExpectedPointEqualsActual(makePoint(14, 13), result);

        result = sut.getDefinedPoint(makePoint(18, 19), 3);
        assertExpectedPointEqualsActual(makePoint(18, 16), result);

        result = sut.getDefinedPoint(makePoint(22, 10), 2);
        assertExpectedPointEqualsActual(makePoint(22, 12), result);
    }

    @Test
    public void givenPointOnDefinedPoint_shouldReturnTrue() {
        BezierCurve bezierCurve = new BezierCurve();
        bezierCurve.addPoint(makePoint(1, 4));
        bezierCurve.addPoint(makePoint(23, 48));
        bezierCurve.addPoint(makePoint(12, 15));
        bezierCurve.addPoint(makePoint(79, 30));


        sut = new PaintableBezierCurve(bezierCurve);

        assertTrue(sut.isPointOnBoundary(makePoint(1, 4), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(23, 48), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(12, 15), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(79, 30), 0));
    }

}