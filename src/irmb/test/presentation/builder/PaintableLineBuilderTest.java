package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.presentation.builder.PaintableLineBuilder;
import irmb.flowsim.view.graphics.PaintableLine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 14.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PaintableLineBuilderTest extends PaintableLine {


    private Point start;
    private Point end;
    private PaintableLineBuilder sut;

    @Before
    public void setUp() throws Exception {
        PaintableFactory factory = mock(PaintableFactory.class);
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
        }
    }

}