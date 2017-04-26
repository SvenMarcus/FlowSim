package irmb.test.model;

import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.ShapeVisitor;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by sven on 19.03.17.
 */
public class BezierCurveTest {

    private BezierCurve sut;

    @Before
    public void setUp() throws Exception {
        sut = new BezierCurve();
    }

    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        Point first = makePoint(11, 12);
        sut.addPoint(first);
        Point second = makePoint(21, 22);
        sut.addPoint(second);
        Point third = makePoint(45, 39);
        sut.addPoint(third);

        Point newFirst = makePoint(first.getX() + 5, first.getY() + 6);
        Point newSecond = makePoint(second.getX() + 5, second.getY() + 6);
        Point newThird = makePoint(third.getX() + 5, third.getY() + 6);

        sut.moveBy(5, 6);

        List<Point> pointList = sut.getPointList();
        assertExpectedPointEqualsActual(newFirst, pointList.get(0));
        assertExpectedPointEqualsActual(newSecond, pointList.get(1));
        assertExpectedPointEqualsActual(newThird, pointList.get(2));
    }

    @Test
    public void whenReceivingVisitor_shouldCallVisitMethod() {
        ShapeVisitor shapeVisitor = mock(ShapeVisitor.class);
        sut.accept(shapeVisitor);
        verify(shapeVisitor).visit(sut);
    }
    

}