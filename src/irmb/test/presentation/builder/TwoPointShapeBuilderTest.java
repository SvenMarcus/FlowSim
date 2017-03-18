package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.*;
import irmb.flowsim.presentation.builder.TwoPointShapeBuilder;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintableRectangle;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by sven on 18.03.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class TwoPointShapeBuilderTest {

    private TwoPointShape shape;
    private TwoPointShapeBuilder sut;

    public class GeneralShapeBehaviorContext {
        @Before
        public void setUp() {
            shape = mock(TwoPointShape.class);
            ShapeFactory shapeFactory = mock(ShapeFactory.class);
            when(shapeFactory.makeShape(anyString())).thenReturn(shape);

            sut = new TwoPointShapeBuilder(shapeFactory, "");
        }

        @Test
        public void whenAddingOnePoint_shouldSetFirstPoint() {

            sut.addPoint(makePoint(1, 2));
            ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

            verify(shape).setFirst(pointCaptor.capture());
            assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getValue());
        }

        @Test
        public void isObjectFinished_shouldReturnFalse() {
            assertFalse(sut.isObjectFinished());
        }

        @Test
        public void whenRemovingLastPoint_shouldDoNothing() {
            sut.removeLastPoint();
            verifyZeroInteractions(shape);
        }

        @Test
        public void whenAddingPointAfterRemovingLastPoint_shouldSetFirst() {
            sut.removeLastPoint();
            clearInvocations(shape);

            sut.addPoint(makePoint(4, 6));

            ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
            verify(shape).setFirst(pointCaptor.capture());
            assertExpectedPointEqualsActual(makePoint(4, 6), pointCaptor.getAllValues().get(0));
        }

        public class OnePointAddedContext {
            @Before
            public void setUp() {
                sut.addPoint(makePoint(1, 2));
            }

            @Test
            public void whenAddingSecondPoint_shouldSetSecondPoint() {
                sut.addPoint(makePoint(3, 4));
                ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                verify(shape).setFirst(pointCaptor.capture());
                verify(shape).setSecond(pointCaptor.capture());
                assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getAllValues().get(0));
                assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(1));
            }

            @Test
            public void isObjectFinished_shouldReturnFalse() {
                assertFalse(sut.isObjectFinished());
            }

            @Test
            public void whenSettingLastPoint_shouldAdjustFirst() {
                clearInvocations(shape);
                sut.setLastPoint(makePoint(3, 4));
                ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                verify(shape).setFirst(pointCaptor.capture());
                assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(0));
                verifyNoMoreInteractions(shape);
            }

            @Test
            public void whenRemovingLastPoint_shouldSetFirstToNull() {
                clearInvocations(shape);
                sut.removeLastPoint();

                verify(shape).setFirst(null);
                verifyNoMoreInteractions(shape);
            }

            @Test
            public void whenAddingPointAfterRemovingLastPoint_shouldSetFirst() {
                sut.removeLastPoint();
                clearInvocations(shape);

                sut.addPoint(makePoint(4, 6));

                ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
                verify(shape).setFirst(pointCaptor.capture());
                assertExpectedPointEqualsActual(makePoint(4, 6), pointCaptor.getAllValues().get(0));
            }

            public class TwoPointsAddedContext {
                @Before
                public void setUp() {
                    sut.addPoint(makePoint(3, 4));
                }

                @Test
                public void whenAddingThirdPoint_shouldDoNothing() {
                    sut.addPoint(makePoint(5, 6));
                    ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                    verify(shape).setFirst(pointCaptor.capture());
                    verify(shape).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getAllValues().get(0));
                    assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(1));
                }

                @Test
                public void isObjectFinished_shouldReturnTrue() {
                    assertTrue(sut.isObjectFinished());
                }

                @Test
                public void whenSettingLastPoint_shouldAdjustSecond() {
                    clearInvocations(shape);
                    sut.setLastPoint(makePoint(5, 6));
                    ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                    verify(shape).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(5, 6), pointCaptor.getAllValues().get(0));
                    verifyNoMoreInteractions(shape);
                }

                @Test
                public void whenRemovingLastPoint_shouldSetSecondToNull() {
                    clearInvocations(shape);
                    sut.removeLastPoint();

                    verify(shape).setSecond(null);
                    verifyNoMoreInteractions(shape);
                }

                @Test
                public void whenAddingPointAfterRemovingLastPoint_shouldSetSecond() {
                    sut.removeLastPoint();
                    clearInvocations(shape);

                    sut.addPoint(makePoint(4, 6));

                    ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
                    verify(shape).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(4, 6), pointCaptor.getAllValues().get(0));
                }
            }
        }
    }

    public class SpecificReturnValueContext {

        private ShapeFactory shapeFactory;

        @Before
        public void setUp() {
            shapeFactory = mock(ShapeFactory.class);
            when(shapeFactory.makeShape("Line")).thenReturn(mock(Line.class));
            when(shapeFactory.makeShape("Rectangle")).thenReturn(mock(Rectangle.class));
        }

        @Test
        public void whenCallingGetShapeForBuilderWithLine_shouldReturnPaintableLine() {
            TwoPointShapeBuilder sut = new TwoPointShapeBuilder(shapeFactory, "Line");
            assertThat(sut.getShape(), is(instanceOf(PaintableLine.class)));
        }

        @Test
        public void whenCallingGetShapeForBuilderWithRectangle_shouldReturnPaintableRectangle() {
            TwoPointShapeBuilder sut = new TwoPointShapeBuilder(shapeFactory, "Rectangle");
            assertThat(sut.getShape(), is(instanceOf(PaintableRectangle.class)));
        }

    }


}