package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.builder.PaintableLineBuilder;
import irmb.flowsim.presentation.factory.ShapeFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static test.irmb.flowsim.util.TestUtil.assertExpectedPointEqualsActual;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Sven on 14.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PaintableLineBuilderTest extends Line {


    private Point start;
    private Point end;
    private PaintableLineBuilder sut;

    @Before
    public void setUp() throws Exception {
        ShapeFactory factory = mock(ShapeFactory.class);
        when(factory.makeShape("Line")).thenReturn(this);
        sut = new PaintableLineBuilder(factory);
        start = new Point(5, 3);
        end = new Point(7, 8);
    }

    @Test
    public void whenAddingOnePoint_lineStartShouldEqualPoint() {
        sut.addPoint(start);
        assertExpectedPointEqualsActual(start, getFirst());
    }

    @Test
    public void whenAddingOnePoint_isObjectFinishedShouldBeFalse() {
        sut.addPoint(start);
        assertFalse(sut.isObjectFinished());
    }

    @Test
    public void whenSettingLastPoint_shouldDoNothing() {
        sut.setLastPoint(start);

        assertNull(getFirst());
    }

    public class OnePointAddedContext {
        @Before
        public void setUp() {
            sut.addPoint(start);
        }

        @Test
        public void whenAddingSecondPoint_lineStartShouldEqualFirstLineEndShouldEqualSecond() {
            sut.addPoint(end);
            assertExpectedPointEqualsActual(start, getFirst());
            assertExpectedPointEqualsActual(end, getSecond());
        }

        @Test
        public void whenAddingSecondPoint_isObjectFinishedShouldBeTrue() {
            sut.addPoint(end);
            assertTrue(sut.isObjectFinished());
        }

        @Test
        public void whenSettingLastPoint_shouldAdjustFirst() {
            sut.setLastPoint(end);
            assertExpectedPointEqualsActual(end, getFirst());
        }

        public class TwoPointsAddedContext {
            private final Point third = new Point(2, 8);

            @Before
            public void setUp() {
                sut.addPoint(end);
            }

            @Test
            public void whenAddingThirdPoint_lineShouldBeUnchanged() {
                sut.addPoint(third);

                assertExpectedPointEqualsActual(start, getFirst());
                assertExpectedPointEqualsActual(end, getSecond());
            }

            @Test
            public void whenAddingThirdPoint_isObjectFinishedShouldBeTrue() {
                sut.addPoint(third);
                assertTrue(sut.isObjectFinished());
            }

            @Test
            public void whenSettingLastPoint_shouldAdjustSecondPoint() {
                sut.setLastPoint(third);

                assertExpectedPointEqualsActual(start, getFirst());
                assertExpectedPointEqualsActual(third, getSecond());
            }

            @Test
            public void whenSettingLastPointAfterAddingPoint_shouldAdjustSecondPoint() {
                Point point = new Point(10, 11);

                sut.addPoint(third);
                sut.setLastPoint(point);

                assertExpectedPointEqualsActual(point, getSecond());
            }
        }
    }

}