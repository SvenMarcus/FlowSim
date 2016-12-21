package irmb.test.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.PaintableRectangle;
import org.junit.Before;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.*;

/**
 * Created by Sven on 20.12.2016.
 */
public class PaintableRectangleTest extends PaintableRectangle {
    private PaintableRectangle sut;
    private Point second;
    private Point first;

    @Before
    public void setUp() throws Exception {
        sut = this;
        first = new Point(21, 22);
        second = new Point(11, 12);
        sut.setSecond(second);
        sut.setFirst(first);
    }

    @Test
    public void givenPointOnFirst_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(new Point(11, 12), 0));
    }

    @Test
    public void givenPointLeftOfFirst_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(10, 12), 0));
    }

    @Test
    public void givenPointBelowFirst_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(11, 10), 0));
    }

    @Test
    public void givenPointAboveSecond_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(21, 23), 0));
    }


    @Test
    public void givenPointRightOfSecond_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(22, 22), 0));
    }

    @Test
    public void givenPointInsideBoundingBoxAboveOfFirst_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(12, 13), 0));
    }

    @Test
    public void givenPointInsideBoundingBoxBelowSecond_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(new Point(20, 21), 0));
    }

    @Test
    public void givenPointOnUpperBoundary_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(new Point(15, 22), 0));
    }

    @Test
    public void givenPointOnLeftBoundary_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(new Point(11, 15), 0));
    }

    @Test
    public void givenPointOnRightBoundary_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(new Point(21, 15), 0));
    }

    @Test
    public void givenPointWithinRadius_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(new Point(8, 12), 3));
        assertTrue(sut.isPointOnBoundary(new Point(8, 17), 3));
        assertTrue(sut.isPointOnBoundary(new Point(24, 12), 3));
        assertTrue(sut.isPointOnBoundary(new Point(11, 9), 3));
        assertTrue(sut.isPointOnBoundary(new Point(16, 15), 3));
        assertTrue(sut.isPointOnBoundary(new Point(11, 25), 3));
        assertTrue(sut.isPointOnBoundary(new Point(16, 19), 3));
    }

    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        sut.moveBy(5, 6);
        Point newStart = new Point(first.getX() + 5, first.getY() + 6);
        Point newEnd = new Point(second.getX() + 5, second.getY() + 6);
        assertExpectedPointEqualsActual(newStart, getFirst());
        assertExpectedPointEqualsActual(newEnd, getSecond());
    }
}