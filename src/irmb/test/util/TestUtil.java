package irmb.test.util;

import irmb.flowsim.model.Point;
import org.mockito.ArgumentMatcher;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.doubleThat;

/**
 * Created by Sven on 14.12.2016.
 */
public abstract class TestUtil {

    public static final double DELTA = 0.0001;

    public static void assertExpectedPointEqualsActual(Point expected, Point actual) {
        assertEquals(expected.getX(), actual.getX(), DELTA);
        assertEquals(expected.getY(), actual.getY(), DELTA);
    }

    public static void assertExpectedPointEqualsActual(Point expected, Point actual, double delta) {
        assertEquals(expected.getX(), actual.getX(), delta);
        assertEquals(expected.getY(), actual.getY(), delta);
    }

    public static double doubleOf(double i) {
        return doubleThat(aDouble -> Math.abs(i - aDouble) <= DELTA);
    }

    public static Point makePoint(double x, double y) {
        return new Point(x, y);
    }
}
