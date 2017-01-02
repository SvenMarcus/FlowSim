package irmb.test.model;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;

/**
 * Created by Sven on 02.01.2017.
 */
public class RectangleTest {
    @Test
    public void whenCallingMoveBy_shouldMoveLineByDelta() {
        Rectangle sut = new Rectangle();
        Point first = makePoint(21, 22);
        Point second = makePoint(11, 12);
        sut.setSecond(second);
        sut.setFirst(first);

        sut.moveBy(5, 6);
        Point newStart = makePoint(first.getX() + 5, first.getY() + 6);
        Point newEnd = makePoint(second.getX() + 5, second.getY() + 6);
        assertExpectedPointEqualsActual(newStart, sut.getFirst());
        assertExpectedPointEqualsActual(newEnd, sut.getSecond());
    }
}