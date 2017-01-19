package irmb.test.model.util;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import org.junit.Before;
import org.junit.Test;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;

/**
 * Created by Sven on 10.01.2017.
 */
public class CoordinateTransformerImplTest extends CoordinateTransformerImpl {

    private CoordinateTransformerImplTest sut;

    @Before
    public void setUp() throws Exception {
        sut = new CoordinateTransformerImplTest();
        sut.setWorldBounds(makePoint(-15, 15), makePoint(10, -25));
        sut.setViewBounds(makePoint(0, 0), makePoint(800, 600));
    }

    @Test
    public void testTransformToPointOnScreen() {
        Point p = makePoint(0, 0);

        Point result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(433.75, 232.5), result);

        p = makePoint(-3, 12);

        result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(393.25, 70.5), result);

        p = makePoint(-18, 20);

        result = sut.transformToPointOnScreen(p);
        assertExpectedPointEqualsActual(makePoint(190.75, -37.5), result);
    }

    @Test
    public void testTransformToWorldPoint() {
        Point p = makePoint(433.75, 232.5);

        Point result = sut.transformToWorldPoint(p);
        assertExpectedPointEqualsActual(makePoint(0, 0), result);
    }

    @Test
    public void testMoveViewWindow() {
        sut.moveViewWindow(5, 23);
        assertExpectedPointEqualsActual(makePoint(5, 23), sut.viewTopLeft);
        assertExpectedPointEqualsActual(makePoint(805, 623), sut.viewBottomRight);
    }

}