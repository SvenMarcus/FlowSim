package irmb.test.view.graphics;

import irmb.flowsim.model.Point;
import irmb.flowsim.view.graphics.PaintableLine;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.*;

/**
 * Created by Sven on 20.12.2016.
 */
public class PaintableLineTest {

    private PaintableLine sut;
    private Point start;
    private Point end;

    @Before
    public void setUp() throws Exception {
        sut = new PaintableLine();
        start = makePoint(21, 22);
        end = makePoint(11, 12);
        sut.setFirst(start);
        sut.setSecond(end);
    }

    private Point makePoint(int x, int y) {
        return new Point(x, y);
    }

    @Test
    public void givenPointOnStart_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(11, 12), 0));
    }

    @Test
    public void givenOrigin_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(0, 0), 0));
    }

    @Test
    public void givenPointOnEnd_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(21, 22), 0));
    }

    @Test
    public void givenPointAboveStart_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(11, 13), 0));
    }

    @Test
    public void givenPointBelowEnd_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(21, 21), 0));
    }

    @Test
    public void givenPointLeftOfStart_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(10, 11), 0));
    }

    @Test
    public void givenPointRightOfEnd_shouldReturnFalse() {
        assertFalse(sut.isPointOnBoundary(makePoint(22, 23), 0));
    }

    @Test
    public void givenPointOnLine_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(12, 13), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(13, 14), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(14, 15), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(15, 16), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(16, 17), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(17, 18), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(18, 19), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(19, 20), 0));
        assertTrue(sut.isPointOnBoundary(makePoint(20, 21), 0));
    }

    @Test
    public void givenPointWithinAllowedRadius_shouldReturnTrue() {
        assertTrue(sut.isPointOnBoundary(makePoint(11, 13), 1));
        assertTrue(sut.isPointOnBoundary(makePoint(11, 13), 1));
        assertTrue(sut.isPointOnBoundary(makePoint(12, 16), 3));
        assertTrue(sut.isPointOnBoundary(makePoint(14, 19), 3));
    }

}