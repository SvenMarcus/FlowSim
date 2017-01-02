package irmb.test.model;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;

/**
 * Created by Sven on 02.01.2017.
 */
public class LineTest {

    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        Line sut = new Line();
        Point start = makePoint(21, 22);
        Point end = makePoint(11, 12);
        sut.setFirst(start);
        sut.setSecond(end);
        sut.moveBy(5, 6);
        Point newStart = makePoint(start.getX() + 5, start.getY() + 6);
        Point newEnd = makePoint(end.getX() + 5, end.getY() + 6);
        assertExpectedPointEqualsActual(newStart, sut.getFirst());
        assertExpectedPointEqualsActual(newEnd, sut.getSecond());
    }

}