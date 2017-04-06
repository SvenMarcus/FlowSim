package irmb.test.model;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.model.ShapeVisitor;
import org.junit.Before;
import org.junit.Test;

import static test.irmb.flowsim.util.TestUtil.assertExpectedPointEqualsActual;
import static test.irmb.flowsim.util.TestUtil.makePoint;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Sven on 02.01.2017.
 */
public class RectangleTest {

    private Point first;
    private Point second;
    private Rectangle sut;

    @Before
    public void setUp() throws Exception {
        first = makePoint(21, 22);
        second = makePoint(11, 12);
        sut = new Rectangle();
        sut.setSecond(second);
        sut.setFirst(first);
    }

    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        sut.moveBy(5, 6);
        Point newStart = makePoint(21 + 5, 22 + 6);
        Point newEnd = makePoint(11 + 5, 12 + 6);
        assertExpectedPointEqualsActual(newStart, sut.getFirst());
        assertExpectedPointEqualsActual(newEnd, sut.getSecond());
    }

    @Test
    public void whenCallingMoveBy_shouldNotCreateNewPoints() {
        sut.moveBy(5, 6);
        assertTrue(sut.getFirst() == first);
        assertTrue(sut.getSecond() == second);
    }

    @Test
    public void whenCallingAccept_shouldCallVisitWithSelf() {
        ShapeVisitor shapeVisitorSpy = mock(ShapeVisitor.class);
        sut.accept(shapeVisitorSpy);
        verify(shapeVisitorSpy).visit(sut);
    }
}