package presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.builder.MultiPointShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeFactory;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static util.TestUtil.assertExpectedPointEqualsActual;
import static util.TestUtil.makePoint;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by Sven on 15.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class MultiPointShapeBuilderTest extends PolyLine {

    private MultiPointShapeBuilder sut;
    private PaintableShapeFactory paintableShapeFactory;
    private final Point first = new Point(5, 3);

    @Before
    public void setUp() {
        PaintablePolyLine paintablePolyLine = mock(PaintablePolyLine.class);
        paintableShapeFactory = mock(PaintableShapeFactory.class);
        sut = new MultiPointShapeBuilder(this, paintableShapeFactory);
    }

    @Test
    public void onCreation_isObjectFinishedShouldBeFalse() {
        assertFalse(sut.isObjectFinished());
    }

    @Test
    public void whenAddingOnePoint_shouldHaveCorrectPoint() {
        sut.addPoint(first);
        assertExpectedPointEqualsActual(first, getPointList().get(0));
    }

    @Test
    public void whenSettingLastPoint_shouldDoNothing() {
        sut.setLastPoint(first);
        assertEquals(0, getPointList().size());
    }

    @Test
    public void whenRemovingLastPoint_shouldDoNothing() {
        sut.removeLastPoint();

        assertEquals(0, getPointList().size());
    }

    @Test
    public void onCreation_isObjectPaintableShouldBeFalse() {
        assertFalse(sut.isObjectPaintable());
    }

    public class OnePointAddedContext {

        private final Point second = new Point(10, 5);

        @Before
        public void setUp() {
            sut.addPoint(first);
        }

        @Test
        public void whenSettingLastPoint_shouldAdjustFirstPoint() {
            sut.setLastPoint(second);

            assertEquals(1, getPointList().size());
            assertExpectedPointEqualsActual(second, getPointList().get(0));
        }


        @Test
        public void whenRemovingLastPoint_shouldRemoveLastPointFromList() {
            sut.removeLastPoint();

            assertEquals(0, getPointList().size());
        }

        public class TwoPointsAddedContext {
            @Before
            public void setUp() {
                sut.addPoint(second);
            }

            @Test
            public void whenSettingLastPoint_shouldAdjustSecond() {
                Point third = new Point(10, 11);

                sut.setLastPoint(third);

                assertEquals(2, getPointList().size());
                assertEquals(first, getPointList().get(0));
                assertEquals(third, getPointList().get(1));
            }

            @Test
            public void isObjectPaintable_shouldBeTrue() {
                assertTrue(sut.isObjectPaintable());
            }

            @Test
            public void whenAddingMorePoints_isObjectPaintableShouldBeTrue() {
                sut.addPoint(makePoint(10, 11));

                assertTrue(sut.isObjectPaintable());
            }
        }
    }

    public class SpecificReturnTypeContext {

        @Test
        public void whenCallingGetShapeForBuilderWithPolyLine_shouldReturnPaintablePolyLine() {
            sut.getShape();
            verify(paintableShapeFactory).makePaintableShape(any(PolyLine.class));
        }

        @Test
        public void whenCallingGetShapeForBuilderWithBezierCurve_shouldReturnPaintableBezierCurve() {
            sut = new MultiPointShapeBuilder(mock(BezierCurve.class), paintableShapeFactory);
            sut.getShape();
            verify(paintableShapeFactory).makePaintableShape(any(BezierCurve.class));
        }
    }
}
