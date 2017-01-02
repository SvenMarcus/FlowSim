package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.presentation.builder.PaintableRectangleBuilder;
import irmb.flowsim.presentation.factory.ShapeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sven on 14.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PaintableRectangleBuilderTest extends Rectangle {

    private Point firstPoint;
    private Point secondPoint;
    private PaintableRectangleBuilder sut;

    @Before
    public void setUp() {
        ShapeFactory factory = mock(ShapeFactory.class);
        when(factory.makeShape("Rectangle")).thenReturn(this);
        sut = new PaintableRectangleBuilder(factory);
        firstPoint = new Point(5, 3);
        secondPoint = new Point(7, 8);
    }

    @Test
    public void whenAddingNoPoints_isObjectFinishedShouldBeFalse() {
        assertFalse(sut.isObjectFinished());
    }

    @Test
    public void whenAddingOnePoint_firstShouldEqualPoint() {
        sut.addPoint(firstPoint);
        assertExpectedPointEqualsActual(firstPoint, getFirst());
    }

    @Test
    public void whenSettingLastPoint_shouldDoNothing() {
        sut.setLastPoint(firstPoint);

        assertNull(getFirst());
        assertNull(getSecond());
    }

    @Test
    public void whenAddingOnePoint_isObjectFinishedShouldBeFalse() {
        sut.addPoint(firstPoint);
        assertFalse(sut.isObjectFinished());
    }

    public class OnePointAddedContext {
        @Before
        public void setUp() {
            sut.addPoint(firstPoint);
        }

        @Test
        public void whenAddingSecondPoint_firstAndSecondShouldEqualAddedPoints() {
            sut.addPoint(secondPoint);
            assertExpectedPointEqualsActual(firstPoint, getFirst());
            assertExpectedPointEqualsActual(secondPoint, getSecond());
        }

        @Test
        public void whenAddingSecondPoint_isObjectFinishedShouldBeTrue() {
            sut.addPoint(secondPoint);
            assertTrue(sut.isObjectFinished());
        }

        @Test
        public void whenSettingLastPoint_shouldAdjustFirst() {
            sut.setLastPoint(secondPoint);
            assertExpectedPointEqualsActual(secondPoint, getFirst());
        }

        public class TwoPointsAddedContext {
            private final Point third = new Point(2, 8);

            @Before
            public void setUp() {
                sut.addPoint(secondPoint);
            }

            @Test
            public void whenAddingThirdPoint_rectangleShouldBeUnchanged() {
                sut.addPoint(third);

                assertExpectedPointEqualsActual(firstPoint, getFirst());
                assertExpectedPointEqualsActual(secondPoint, getSecond());
            }

            @Test
            public void whenAddingThirdPoint_isObjectFinishedShouldBeTrue() {
                sut.addPoint(third);
                assertTrue(sut.isObjectFinished());
            }

            @Test
            public void whenSettingLastPoint_shouldAdjustSecond() {
                sut.setLastPoint(third);

                assertExpectedPointEqualsActual(firstPoint, getFirst());
                assertExpectedPointEqualsActual(third, getSecond());
            }

            @Test
            public void whenSettingLastPointAfterAddingPoint_shouldAdjustSecond() {
                Point point = new Point(10, 11);

                sut.addPoint(third);
                sut.setLastPoint(point);

                assertExpectedPointEqualsActual(point, getSecond());
            }
        }
    }
}