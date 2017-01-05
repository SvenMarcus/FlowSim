package irmb.test.util;

import irmb.flowsim.model.Point;

import static org.junit.Assert.assertEquals;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class TestUtil {
    public static void assertExpectedPointEqualsActual(Point expected, Point actual) {
        assertEquals(expected.getX(), actual.getX(), 0.0001);
        assertEquals(expected.getY(), actual.getY(), 0.0001);
    }

    public static Point makePoint(double x, double y) {
        return new Point(x, y);
    }
}
