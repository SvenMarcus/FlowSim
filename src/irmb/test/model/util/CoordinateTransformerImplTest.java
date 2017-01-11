package irmb.test.model.util;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;

/**
 * Created by Sven on 10.01.2017.
 */
public class CoordinateTransformerImplTest {

    @Test
    public void testTransformWorldToViewPoint() {
        CoordinateTransformer sut = new CoordinateTransformerImpl();
        sut.setWorldBounds(-15, 10, -25, 15);
        sut.setViewBounds(0, 800, 0, 600);
        Point p = new Point(0, 0);
        Point result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(400, 300), result);
    }

}