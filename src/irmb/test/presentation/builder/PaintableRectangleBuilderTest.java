package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.presentation.builder.PaintableRectangleBuilder;
import irmb.flowsim.view.graphics.PaintableRectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sven on 14.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PaintableRectangleBuilderTest extends PaintableRectangle {

    private Point firstPoint;
    private Point secondPoint;
    private PaintableRectangleBuilder sut;

    @Before
    public void setUp() {
        PaintableFactory factory = mock(PaintableFactory.class);
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
        }
    }
}