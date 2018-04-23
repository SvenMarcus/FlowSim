package presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.builder.PaintablePolyLineBuilder;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static util.TestUtil.assertExpectedPointEqualsActual;

/**
 * Created by Sven on 15.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class PaintablePolyLineBuilderTest extends PolyLine {

    private PaintablePolyLineBuilder sut;
    private final Point first = new Point(5, 3);

    @Before
    public void setUp() {
        ShapeFactory factory = spy(new ShapeFactoryImpl());
        when(factory.makeShape("PolyLine")).thenReturn(this);
        sut = new PaintablePolyLineBuilder(factory);
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
        }
    }
}