package irmb.test.model;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.ShapeVisitor;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by sven on 17.02.17.
 */
public class PointTest {

    private Point sut;

    @Before
    public void setUp() throws Exception {
        sut = new Point(25, 31);
    }

    @Test
    public void whenCallingMove_shouldMovePointByDelta() {
        sut.moveBy(2, -5);
        assertEquals(27, sut.getX(), 0);
        assertEquals(26, sut.getY(), 0);
    }

    @Test
    public void whenCallingAccept_shouldCallVisitWithSelf() {
        ShapeVisitor shapeVisitorSpy = mock(ShapeVisitor.class);
        sut.accept(shapeVisitorSpy);
        verify(shapeVisitorSpy).visit(sut);
    }

}