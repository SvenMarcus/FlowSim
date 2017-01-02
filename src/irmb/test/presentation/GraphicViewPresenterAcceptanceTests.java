package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 15.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class GraphicViewPresenterAcceptanceTests extends GraphicViewPresenterTest {

    public class SimplePaintingContext {
        @Test
        public void buildLineAcceptanceTest() {
            sut.beginPaint("Line");
            sut.handleLeftClick(13, 15);
            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);
//
//            sut.beginPaint("Line");
//            sut.handleLeftClick(36, 12);
//            sut.handleLeftClick(25, 57);
//            verify(painterSpy, times(1)).paintLine(36, 12, 25, 57);
//
//            verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
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
        public void livePaintingPolyLineAcceptanceTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
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

            sut.handleRightClick();
            verify(painterSpy, times(6)).paintLine(13, 15, 36, 12);
            verify(painterSpy, times(4)).paintLine(36, 12, 24, 20);
            verify(painterSpy, times(1)).paintLine(24, 20, 43, 22);

            sut.handleMouseMove(35, 84);
            sut.handleLeftClick(35, 84);
            verify(painterSpy, never()).paintLine(43, 22, 35, 84);
        }
    }

    @Test
    public void moveShapeAcceptanceTest() {
        sut.beginPaint("Line");

        buildLine(13, 15, 18, 19);
        verify(painterSpy, atLeastOnce()).paintLine(13, 15, 18, 19);

        sut.handleLeftClick(15, 18);
        sut.handleMouseDrag(20, 24);
        verify(painterSpy, atLeastOnce()).paintLine(18, 21, 23, 25);

        sut.handleMouseDrag(3, 10);
        verify(painterSpy, atLeastOnce()).paintLine(1, 7, 6, 11);

        sut.handleMouseRelease(3, 10);

        sut.handleLeftClick(0, 0);
        sut.handleMouseDrag(15, 18);
        verifyNoMoreInteractions(painterSpy);
    }

    @Test
    public void commandQueueAcceptanceTest() {
        sut.beginPaint("Line");

        buildLine(13, 15, 18, 19);
        verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);

        performMove(15, 18, 20, 24);
        verify(painterSpy, times(1)).paintLine(18, 21, 23, 25);

        sut.undo();
        verify(painterSpy, times(3)).paintLine(13, 15, 18, 19);

        sut.undo();
        verifyNoMoreInteractions(painterSpy);

        sut.undo();
        verifyNoMoreInteractions(painterSpy);

        sut.redo();
        verify(painterSpy, times(4)).paintLine(13, 15, 18, 19);

        sut.redo();
        verify(painterSpy, times(2)).paintLine(18, 21, 23, 25);

        sut.redo();
        verifyNoMoreInteractions(painterSpy);

        sut.undo();
        verify(painterSpy, times(5)).paintLine(13, 15, 18, 19);

        buildLine(35, 40, 10, 54);
        verify(painterSpy, times(1)).paintLine(35, 40, 10, 54);

        sut.redo();
        verifyNoMoreInteractions(painterSpy);
    }



}
