package irmb.test.model;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import org.junit.Before;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;

/**
 * Created by Sven on 02.01.2017.
 */
public class LineTest {

    private Point start;
    private Point end;
    private Line sut;

    @Before
    public void setUp() throws Exception {
        start = makePoint(21, 22);
        end = makePoint(11, 12);
        sut = new Line();
        sut.setFirst(start);
        sut.setSecond(end);
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
        assertTrue(sut.getFirst() == start);
        assertTrue(sut.getSecond() == end);
    }

}