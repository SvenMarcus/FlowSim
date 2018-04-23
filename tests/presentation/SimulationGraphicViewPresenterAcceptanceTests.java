package presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.simulation.SimulationFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;

import static irmb.mockito.verification.AtLeastThenForget.atLeastThenForget;
import static irmb.mockito.verification.AtLeastThenForgetAll.atLeastThenForgetAll;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;
import static util.TestUtil.*;

/**
 * Created by sven on 11.04.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class SimulationGraphicViewPresenterAcceptanceTests extends GraphicViewPresenterTest{


    @Before
    public void setUp() {
        initializeTestSetup();
        sut = new SimulationGraphicViewPresenter(mouseStrategyFactory, commandStack, shapeList, transformer, mock(SimulationFactory.class));
        sut.setGraphicView(graphicView);
    }

    public class SimplePaintingContext {
        @Test
        public void buildLineAcceptanceTest() {
            sut.beginPaint("Line");
            sut.handleLeftClick(13, 15);
            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

            sut.beginPaint("Line");
            sut.handleLeftClick(36, 12);
            sut.handleLeftClick(25, 57);
            verify(painterSpy, times(1)).paintLine(36, 12, 25, 57);

            verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void buildRectangleAcceptanceTest() {
            sut.beginPaint("Rectangle");
            sut.handleLeftClick(13, 15);
            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(1)).paintRectangle(13, 15, 5, 4);

            sut.beginPaint("Rectangle");
            sut.handleLeftClick(36, 12);
            sut.handleLeftClick(25, 57);
            verify(painterSpy, times(1)).paintRectangle(25, 12, 11, 45);

            verify(painterSpy, times(2)).paintRectangle(13, 15, 5, 4);
        }

        @Test
        public void buildPolyLineAcceptanceTest() {
            sut.beginPaint("PolyLine");
            sut.handleLeftClick(13, 15);
            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

            sut.handleLeftClick(36, 12);
            verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
            verify(painterSpy, times(1)).paintLine(18, 19, 36, 12);
        }

        @Test
        public void buildBezierCurveAcceptanceTest() {
            sut.beginPaint("Bezier");
            sut.handleLeftClick(14, 13);

            sut.handleLeftClick(18, 16);
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(14), doubleOf(13), doubleOf(18), doubleOf(16));

            sut.handleLeftClick(22, 12);
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(14), doubleOf(13), doubleOf(18), doubleOf(14.25));
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(18), doubleOf(14.25), doubleOf(22), doubleOf(12));

        }
    }

    public class LivePaintingContext {
        @Test
        public void livePaintingLineAcceptanceTest() {
            sut.beginPaint("Line");

            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

            sut.handleMouseMove(36, 12);
            verify(painterSpy, times(1)).paintLine(13, 15, 36, 12);

            sut.handleLeftClick(36, 12);
            verify(painterSpy, times(2)).paintLine(13, 15, 36, 12);

            sut.handleMouseMove(25, 57);
            verifyNoMoreInteractions(painterSpy);
        }

        @Test
        public void livePaintingRectangleAcceptanceTest() {
            sut.beginPaint("Rectangle");

            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            verify(painterSpy).paintRectangle(13, 15, 5, 4);

            sut.handleMouseMove(36, 12);
            verify(painterSpy).paintRectangle(13, 12, 23, 3);

            sut.handleLeftClick(36, 12);
            verify(painterSpy, times(2)).paintRectangle(13, 12, 23, 3);

            sut.handleMouseMove(25, 57);
            verifyNoMoreInteractions(painterSpy);
        }

        @Test
        public void livePaintingPolyLineAcceptanceTest() {
            sut.beginPaint("PolyLine");

            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);


            sut.handleMouseMove(36, 12);
            verify(painterSpy, times(1)).paintLine(13, 15, 36, 12);

            sut.handleLeftClick(36, 12);
            verify(painterSpy, times(2)).paintLine(13, 15, 36, 12);

            sut.handleMouseMove(24, 20);
            verify(painterSpy, times(3)).paintLine(13, 15, 36, 12);
            verify(painterSpy, times(1)).paintLine(36, 12, 24, 20);

            sut.handleLeftClick(24, 20);
            verify(painterSpy, times(4)).paintLine(13, 15, 36, 12);
            verify(painterSpy, times(2)).paintLine(36, 12, 24, 20);

            sut.handleMouseMove(43, 22);
            verify(painterSpy, times(5)).paintLine(13, 15, 36, 12);
            verify(painterSpy, times(3)).paintLine(36, 12, 24, 20);
            verify(painterSpy, times(1)).paintLine(24, 20, 43, 22);

            sut.handleRightClick(0, 0);
            verify(painterSpy, times(6)).paintLine(13, 15, 36, 12);
            verify(painterSpy, times(4)).paintLine(36, 12, 24, 20);
            verify(painterSpy, times(1)).paintLine(24, 20, 43, 22);

            sut.handleMouseMove(35, 84);
            sut.handleLeftClick(35, 84);
            verify(painterSpy, never()).paintLine(43, 22, 35, 84);
        }

        @Test
        public void livePaintingBezierCurveAcceptanceTest() {
            sut.beginPaint("Bezier");

            sut.handleLeftClick(14, 13);

            sut.handleMouseMove(18, 16);
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(14), doubleOf(13), doubleOf(18), doubleOf(16));

            sut.handleLeftClick(18, 16);

            sut.handleMouseMove(22, 12);
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(14), doubleOf(13), doubleOf(18), doubleOf(14.25));
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(18), doubleOf(14.25), doubleOf(22), doubleOf(12));

            sut.handleLeftClick(22, 12);

            sut.handleMouseMove(42, 18);
            clearInvocations(painterSpy);

            sut.handleRightClick(42, 18);
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(14), doubleOf(13), doubleOf(18), doubleOf(14.25));
            verify(painterSpy, atLeastOnce()).paintLine(doubleOf(18), doubleOf(14.25), doubleOf(22), doubleOf(12));

            verify(painterSpy, atLeastOnce()).paintPoint(doubleOf(14), doubleOf(13));
            verify(painterSpy, atLeastOnce()).paintPoint(doubleOf(18), doubleOf(16));
            verify(painterSpy, atLeastOnce()).paintPoint(doubleOf(22), doubleOf(12));

            sut.handleMouseMove(42, 18);
            verifyNoMoreInteractions(painterSpy);
        }

    }

    @Test
    public void moveShapeAcceptanceTest() {
        sut.beginPaint("Line");

        buildLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        sut.handleLeftClick(15, 18);
        sut.handleMouseDrag(20, 24);
        verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);

        sut.handleMouseDrag(3, 10);
        verify(painterSpy, atLeastThenForget(1)).paintLine(1, 7, 6, 11);

        sut.handleMouseRelease();
        clearInvocations(painterSpy);

        sut.handleLeftClick(0, 0);
        sut.handleMouseDrag(15, 18);
        verifyNoMoreInteractions(painterSpy);
    }

    @Test
    public void commandQueueAcceptanceTest() {

        buildLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        performMove(15, 18, 20, 24);
        verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);

        sut.undo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        sut.undo();
        verifyNoMoreInteractions(painterSpy);

        sut.undo();
        verifyNoMoreInteractions(painterSpy);

        sut.redo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        sut.redo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);

        sut.redo();
        verifyNoMoreInteractions(painterSpy);

        sut.undo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        List<Double> coordinates = makePolyLineCoordinates();
        buildPolyLine(coordinates);
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(35, 40, 10, 54);
        verify(painterSpy, atLeastThenForget(1)).paintLine(10, 54, 65, 74);

        sut.undo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        verify(painterSpy, never()).paintLine(35, 40, 10, 54);
        verify(painterSpy, never()).paintLine(10, 54, 65, 74);

        sut.redo();
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(35, 40, 10, 54);
        verify(painterSpy, atLeastThenForgetAll(1)).paintLine(10, 54, 65, 74);

        sut.redo();
        verifyNoMoreInteractions(painterSpy);

    }

    private List<Double> makePolyLineCoordinates() {
        List<Double> coordinates = new ArrayList<>();
        coordinates.add(35.);
        coordinates.add(40.);
        coordinates.add(10.);
        coordinates.add(54.);
        coordinates.add(65.);
        coordinates.add(74.);
        return coordinates;
    }

    @Test
    public void moveViewWindowAcceptanceTest() {
        buildLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

        List<Double> coordinates = makePolyLineCoordinates();
        buildPolyLine(coordinates);
        verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(1)).paintLine(35, 40, 10, 54);
        verify(painterSpy, atLeastThenForget(1)).paintLine(10, 54, 65, 74);

        Line line = (Line) shapeList.get(0).getShape();
        PolyLine polyLine = (PolyLine) shapeList.get(1).getShape();

        Point lineStart = makePoint(line.getFirst().getX(), line.getFirst().getY());
        Point lineEnd = makePoint(line.getSecond().getX(), line.getSecond().getY());
        List<Point> pointList = copyPolyLinePoints(polyLine);

        sut.handleMiddleClick(4, 5);
        sut.handleMouseDrag(-3, 1);
        sut.handleMouseDrag(10, 10);
        sut.handleMouseRelease();

        verify(painterSpy, atLeastThenForget(1)).paintLine(19, 20, 24, 24);
        verify(painterSpy, atLeastThenForget(1)).paintLine(41, 45, 16, 59);
        verify(painterSpy, atLeastThenForget(1)).paintLine(16, 59, 71, 79);

        verifyUnchangedWorldCoordinates(line, polyLine, lineStart, lineEnd, pointList);

        sut.beginPaint("Line");
        sut.handleMiddleClick(10, 10);
        sut.handleMouseDrag(3, 2);
        sut.handleMouseRelease();
        verify(painterSpy, atLeastThenForget(1)).paintLine(12, 12, 17, 16);
        verify(painterSpy, atLeastThenForget(1)).paintLine(34, 37, 9, 51);
        verify(painterSpy, atLeastThenForget(1)).paintLine(9, 51, 64, 71);
    }

    private void verifyUnchangedWorldCoordinates(Line line, PolyLine polyLine, Point lineStart, Point lineEnd, List<Point> pointList) {
        assertExpectedPointEqualsActual(lineStart, line.getFirst());
        assertExpectedPointEqualsActual(lineEnd, line.getSecond());

        assertExpectedPointEqualsActual(pointList.get(0), polyLine.getPointList().get(0));
        assertExpectedPointEqualsActual(pointList.get(1), polyLine.getPointList().get(1));
        assertExpectedPointEqualsActual(pointList.get(2), polyLine.getPointList().get(2));
    }

    private List<Point> copyPolyLinePoints(PolyLine polyLine) {
        List<Point> pointList = new ArrayList<>();
        for (Point p : polyLine.getPointList())
            pointList.add(makePoint(p.getX(), p.getY()));
        return pointList;
    }

    @Test
    public void zoomAcceptanceTest() {
        buildLine(36, 22, 114, 76);

        List<Double> coordinates = makePolyLineCoordinates();
        buildPolyLine(coordinates);


        Line line = (Line) shapeList.get(0).getShape();
        PolyLine polyLine = (PolyLine) shapeList.get(1).getShape();

        Point lineStart = makePoint(line.getFirst().getX(), line.getFirst().getY());
        Point lineEnd = makePoint(line.getSecond().getX(), line.getSecond().getY());

        List<Point> pointList = copyPolyLinePoints(polyLine);

        clearInvocations(painterSpy);

        ArgumentCaptor<Double> doubleCaptor = ArgumentCaptor.forClass(Double.class);
        sut.handleScroll(15, 19, -1);
        verify(painterSpy, atLeastThenForget(1)).paintLine(doubleCaptor.capture(), doubleCaptor.capture(), doubleCaptor.capture(), doubleCaptor.capture());

        List<Double> capturedArgs = doubleCaptor.getAllValues();

        verifyLineCoordinatesAfterZoomOut(capturedArgs);
        verifyPolyLineCoordinatesAfterZoomOut(capturedArgs);

        verifyUnchangedWorldCoordinates(line, polyLine, lineStart, lineEnd, pointList);

        doubleCaptor = ArgumentCaptor.forClass(Double.class);

        sut.beginPaint("Line");
        sut.handleScroll(15, 19, 1);

        verify(painterSpy, atLeastThenForget(1)).paintLine(doubleCaptor.capture(), doubleCaptor.capture(), doubleCaptor.capture(), doubleCaptor.capture());

        verifyLineCoordinatesAfterZoomIn(doubleCaptor.getAllValues());
        verifyPolyLineCoordinatesAfterZoomIn(doubleCaptor.getAllValues());

        verifyUnchangedWorldCoordinates(line, polyLine, lineStart, lineEnd, pointList);
    }

    private void verifyLineCoordinatesAfterZoomIn(List<Double> capturedArgs) {
        assertEquals((double) 36, capturedArgs.get(0));
        assertEquals((double) 22, capturedArgs.get(1));
        assertEquals((double) 114, capturedArgs.get(2));
        assertEquals((double) 76, capturedArgs.get(3));
    }

    private void verifyPolyLineCoordinatesAfterZoomIn(List<Double> capturedArgs) {
        assertEquals((double) 35, capturedArgs.get(4));
        assertEquals((double) 40, capturedArgs.get(5));
        assertEquals((double) 10, capturedArgs.get(6));
        assertEquals((double) 54, capturedArgs.get(7));
        assertEquals((double) 10, capturedArgs.get(8));
        assertEquals((double) 54, capturedArgs.get(9));
        assertEquals((double) 65, capturedArgs.get(10));
        assertEquals((double) 74, capturedArgs.get(11));
    }

    private void verifyPolyLineCoordinatesAfterZoomOut(List<Double> capturedArgs) {
        assertEquals((double) 34, capturedArgs.get(4));
        assertEquals((double) 39, capturedArgs.get(5));
        assertEquals((double) 10, capturedArgs.get(6));
        assertEquals((double) 52, capturedArgs.get(7));
        assertEquals((double) 10, capturedArgs.get(8));
        assertEquals((double) 52, capturedArgs.get(9));
        assertEquals((double) 63, capturedArgs.get(10));
        assertEquals((double) 71, capturedArgs.get(11));
    }

    private void verifyLineCoordinatesAfterZoomOut(List<Double> capturedArgs) {
        assertEquals((double) 35, capturedArgs.get(0));
        assertEquals((double) 22, capturedArgs.get(1));
        assertEquals((double) 109, capturedArgs.get(2));
        assertEquals((double) 73, capturedArgs.get(3));
    }

    @Test
    public void deleteShapeAcceptanceTest() {
        buildLine(52, 66, 474, 523);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        Point pointOnLine = new Point(119, 138.556);
        sut.handleRightClick(pointOnLine.getX(), pointOnLine.getY());
        verify(graphicView, atLeastOnce()).update();
        verifyZeroInteractions(painterSpy);
    }

    @Test
    public void movePointAcceptanceTest() {
        verifyMovePointForLine();

        verifyMovePointForPolyLine();

        verifyMovePointForRectangle();
    }

    private void verifyMovePointForRectangle() {
        buildRectangle(6, 23, 118, 165);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        sut.handleLeftClick(6, 23);
        sut.handleMouseDrag(112, 318);
        sut.handleMouseRelease();
        verify(painterSpy, atLeastThenForget(1)).paintRectangle(112, 165, 6, 153);
    }

    private void verifyMovePointForPolyLine() {
        List<Double> coordinates = makePolyLineCoordinates();
        buildPolyLine(coordinates);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        sut.handleLeftClick(10, 54);
        sut.handleMouseDrag(112, 318);
        sut.handleMouseRelease();
        verify(painterSpy, atLeastThenForget(1)).paintLine(35, 40, 112, 318);
        verify(painterSpy, atLeastThenForget(1)).paintLine(112, 318, 65, 74);
    }

    private void verifyMovePointForLine() {
        buildLine(52, 66, 474, 523);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        sut.handleLeftClick(52, 66);
        sut.handleMouseDrag(65, 12);
        sut.handleMouseRelease();
        verify(painterSpy, atLeastThenForget(1)).paintLine(65, 12, 474, 523);
    }

    @Test
    public void clearAllAcceptanceTest() {
        buildRectangle(15, 14, 80, 20);
        buildLine(74, 18, 65, 10);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        sut.clearAll();
        verify(graphicView).update();
        verifyZeroInteractions(painterSpy);

        clearInvocations(graphicView);
        clearInvocations(painterSpy);

        sut.undo();
        verify(painterSpy, atLeastOnce()).paintLine(74, 18, 65, 10);
        verify(painterSpy, atLeastOnce()).paintRectangle(15, 14, 65, 6);

        clearInvocations(graphicView);
        clearInvocations(painterSpy);

        sut.redo();
        verify(graphicView).update();
        verifyZeroInteractions(painterSpy);
    }
}
