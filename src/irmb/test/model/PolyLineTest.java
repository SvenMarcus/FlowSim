package irmb.test.model;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import org.junit.Test;

import java.util.List;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;

/**
 * Created by Sven on 02.01.2017.
 */
public class PolyLineTest {

    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        PolyLine sut = new PolyLine();
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
}