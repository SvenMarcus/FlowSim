package irmb.test.presentation.builder;

import irmb.flowsim.model.Point;
import irmb.flowsim.presentation.builder.PaintablePolyLineBuilder;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.view.factory.PaintableFactoryImpl;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by Sven on 15.12.2016.
 */
public class PaintablePolyLineBuilderTest extends PaintablePolyLine {

    private PaintablePolyLineBuilder sut;
    private final Point first = new Point(5, 3);

    @Before
    public void setUp() {
        PaintableFactory factory = spy(new PaintableFactoryImpl());
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
        assertEquals(first, getPointList().get(0));
    }

//    @Test
//    public void whenRemovingLastPoint_shouldDoNothing() {
//        sut.removeLastPoint();
//
//        GraphicPolyLineSpy polyLine = (GraphicPolyLineSpy) sut.getShape();
//        assertEquals(0, polyLine.getPointList().size());
//    }
//
//    public class OnePointAddedContext {
//
//        private final Point second = new Point(10, 5);
//
//        @Before
//        public void setUp() {
//            sut.addPoint(first);
//        }
//
//        @Test
//        public void whenSettingLastPoint_shouldAdjustFirstPoint() {
//            sut.setLastPoint(second);
//
//            GraphicPolyLineSpy polyLine = (GraphicPolyLineSpy) sut.getShape();
//            List<Point> pointList = polyLine.getPointList();
//            assertEquals(1, pointList.size());
//            assertEquals(second, pointList.get(0));
//        }
//
//        @Test
//        public void whenRemovingLastPoint_shouldRemoveLastPointFromList() {
//            sut.removeLastPoint();
//
//            GraphicPolyLineSpy polyLine = (GraphicPolyLineSpy) sut.getShape();
//            List<Point> pointList = polyLine.getPointList();
//            assertEquals(0, pointList.size());
//        }
//
//        public class TwoPointsAddedContext {
//            @Before
//            public void setUp() {
//                sut.addPoint(second);
//            }
//
//            @Test
//            public void whenSettingLastPoint_shouldAdjustSecond() {
//                Point third = new Point(10, 11);
//
//                sut.setLastPoint(third);
//
//                GraphicPolyLineSpy polyLine = (GraphicPolyLineSpy) sut.getShape();
//                List<Point> pointList = polyLine.getPointList();
//                assertEquals(2, pointList.size());
//                assertEquals(first, pointList.get(0));
//                assertEquals(third, pointList.get(1));
//            }
//        }
//    }

}