package irmb.test.presentation.builder;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.*;
import irmb.flowsim.presentation.builder.TwoPointShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeFactory;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintableRectangle;
import irmb.flowsim.view.graphics.PaintableShape;
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

    private final Line lineMock = mock(Line.class);
    private final Rectangle rectangleMock = mock(Rectangle.class);
    private TwoPointShapeBuilder sut;

    public class GeneralShapeBehaviorContext {
        @Before
        public void setUp() {
            ShapeFactory shapeFactory = mock(ShapeFactory.class);
            when(shapeFactory.makeShape(anyString())).thenReturn(lineMock);
            sut = new TwoPointShapeBuilder(lineMock, mock(PaintableShapeFactory.class));
        }

        @Test
        public void whenAddingOnePoint_shouldSetFirstPoint() {

            sut.addPoint(makePoint(1, 2));
            ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

            verify(lineMock).setFirst(pointCaptor.capture());
            assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getValue());
        }

        @Test
        public void isObjectFinished_shouldReturnFalse() {
            assertFalse(sut.isObjectFinished());
        }

        @Test
        public void whenRemovingLastPoint_shouldDoNothing() {
            sut.removeLastPoint();
            verifyZeroInteractions(lineMock);
        }

        @Test
        public void whenAddingPointAfterRemovingLastPoint_shouldSetFirst() {
            sut.removeLastPoint();
            clearInvocations(lineMock);

            sut.addPoint(makePoint(4, 6));

            ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
            verify(lineMock).setFirst(pointCaptor.capture());
            assertExpectedPointEqualsActual(makePoint(4, 6), pointCaptor.getAllValues().get(0));
        }

        @Test
        public void onCreation_isObjectPaintableShouldBeFalse() {
            assertFalse(sut.isObjectPaintable());
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

                verify(lineMock).setFirst(pointCaptor.capture());
                verify(lineMock).setSecond(pointCaptor.capture());
                assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getAllValues().get(0));
                assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(1));
            }

            @Test
            public void isObjectFinished_shouldReturnFalse() {
                assertFalse(sut.isObjectFinished());
            }

            @Test
            public void whenSettingLastPoint_shouldAdjustFirst() {
                clearInvocations(lineMock);
                sut.setLastPoint(makePoint(3, 4));
                ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                verify(lineMock).setFirst(pointCaptor.capture());
                assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(0));
                verifyNoMoreInteractions(lineMock);
            }

            @Test
            public void whenRemovingLastPoint_shouldSetFirstToNull() {
                clearInvocations(lineMock);
                sut.removeLastPoint();

                verify(lineMock).setFirst(null);
                verifyNoMoreInteractions(lineMock);
            }

            @Test
            public void whenAddingPointAfterRemovingLastPoint_shouldSetFirst() {
                sut.removeLastPoint();
                clearInvocations(lineMock);

                sut.addPoint(makePoint(4, 6));

                ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
                verify(lineMock).setFirst(pointCaptor.capture());
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

                    verify(lineMock).setFirst(pointCaptor.capture());
                    verify(lineMock).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(1, 2), pointCaptor.getAllValues().get(0));
                    assertExpectedPointEqualsActual(makePoint(3, 4), pointCaptor.getAllValues().get(1));
                }

                @Test
                public void isObjectFinished_shouldReturnTrue() {
                    assertTrue(sut.isObjectFinished());
                }

                @Test
                public void whenSettingLastPoint_shouldAdjustSecond() {
                    clearInvocations(lineMock);
                    sut.setLastPoint(makePoint(5, 6));
                    ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);

                    verify(lineMock).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(5, 6), pointCaptor.getAllValues().get(0));
                    verifyNoMoreInteractions(lineMock);
                }

                @Test
                public void whenRemovingLastPoint_shouldSetSecondToNull() {
                    clearInvocations(lineMock);
                    sut.removeLastPoint();

                    verify(lineMock).setSecond(null);
                    verifyNoMoreInteractions(lineMock);
                }

                @Test
                public void whenAddingPointAfterRemovingLastPoint_shouldSetSecond() {
                    sut.removeLastPoint();
                    clearInvocations(lineMock);

                    sut.addPoint(makePoint(4, 6));

                    ArgumentCaptor<Point> pointCaptor = ArgumentCaptor.forClass(Point.class);
                    verify(lineMock).setSecond(pointCaptor.capture());
                    assertExpectedPointEqualsActual(makePoint(4, 6), pointCaptor.getAllValues().get(0));
                }

                @Test
                public void whenAddingMorePointsToShape_isObjectFinishedShouldBeTrue() {
                    sut.addPoint(makePoint(1, 8));

                    assertTrue(sut.isObjectFinished());
                }

                @Test
                public void isObjectPaintable_shouldBeTrue() {
                    assertTrue(sut.isObjectPaintable());
                }

                @Test
                public void whenAddingMorePointsToShape_isObjectPaintableShouldBeTrue() {
                    sut.addPoint(makePoint(1, 8));

                    assertTrue(sut.isObjectPaintable());
                }
            }
        }
    }

    public class SpecificReturnValueContext {

        private PaintableShapeFactory paintableShapeFactory;
        private ShapeFactory shapeFactory;

        @Before
        public void setUp() {
            shapeFactory = mock(ShapeFactory.class);
            when(shapeFactory.makeShape("Line")).thenReturn(lineMock);
            when(shapeFactory.makeShape("Rectangle")).thenReturn(rectangleMock);
            paintableShapeFactory = mock(PaintableShapeFactory.class);
            when(paintableShapeFactory.makePaintableShape(lineMock)).thenReturn(mock(PaintableLine.class));
            when(paintableShapeFactory.makePaintableShape(rectangleMock)).thenReturn(mock(PaintableRectangle.class));
        }

        @Test
        public void whenCallingGetShapeForBuilderWithLine_shouldReturnPaintableLine() {
            TwoPointShapeBuilder sut = new TwoPointShapeBuilder(lineMock, paintableShapeFactory);
            assertThat(sut.getShape(), is(instanceOf(PaintableLine.class)));
            verify(paintableShapeFactory).makePaintableShape(lineMock);
        }

        @Test
        public void whenCallingGetShapeForBuilderWithRectangle_shouldReturnPaintableRectangle() {
            TwoPointShapeBuilder sut = new TwoPointShapeBuilder(rectangleMock, paintableShapeFactory);
            assertThat(sut.getShape(), is(instanceOf(PaintableRectangle.class)));
            verify(paintableShapeFactory).makePaintableShape(rectangleMock);
        }

        @Test
        public void whenCallingGetShapeTwice_shouldReturnSameInstanceOfPaintableShape() {
            TwoPointShapeBuilder sut = new TwoPointShapeBuilder(rectangleMock, paintableShapeFactory);
            assertTrue(sut.getShape() == sut.getShape());
        }
    }
}