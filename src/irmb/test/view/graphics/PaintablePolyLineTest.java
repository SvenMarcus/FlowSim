package irmb.test.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Created by Sven on 20.12.2016.
 */
public class PaintablePolyLineTest {
    private PaintablePolyLine sut;
    private Point first;
    private Point second;
    private Point third;

    @Before
    public void setUp() {
        PolyLine polyLine = new PolyLine();
        first = makePoint(11, 12);
        polyLine.addPoint(first);
        second = makePoint(21, 22);
        polyLine.addPoint(second);
        third = makePoint(45, 39);
        polyLine.addPoint(third);
        sut = new PaintablePolyLine(polyLine);
    }

    @Test
    public void givenPointOnPolyLine_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(11, 12), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(16, 17), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(21, 22), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(33, 30.5), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(45, 39), 0));
    }

    @Test
    public void givenPointNotOnPolyLine_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(10, 12), 0));
        assertFalse(sut.isPointOnBoundary(makePoint(16, 18), 0));
        assertFalse(sut.isPointOnBoundary(makePoint(22, 22), 0));
        assertFalse(sut.isPointOnBoundary(makePoint(33, 32), 0));
        assertFalse(sut.isPointOnBoundary(makePoint(33, 28), 0));
    }

    @Test
    public void givenPointWithinRadius_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(11, 13), 1));
        assertTrue(sut.isPointOnBoundary(makePoint(11, 13), 1));
        assertTrue(sut.isPointOnBoundary(makePoint(12, 16), 3));
        assertTrue(sut.isPointOnBoundary(makePoint(14, 19), 3));

        assertTrue(sut.isPointOnBoundary(makePoint(46, 40), 3));
        assertTrue(sut.isPointOnBoundary(makePoint(45, 43), 3));
        assertTrue(sut.isPointOnBoundary(makePoint(48, 40), 3));
    }

}