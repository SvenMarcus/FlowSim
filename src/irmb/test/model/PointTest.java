package irmb.test.model;

import irmb.flowsim.model.Point;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by sven on 17.02.17.
 */
public class PointTest {

    @Test
    public void whenCallingMove_shouldMovePointByDelta() {
        Point sut = new Point(25, 31);
        sut.moveBy(2, -5);
        assertEquals(27, sut.getX(), 0);
        assertEquals(26, sut.getY(), 0);
    }

}