package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

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

            sut.beginPaint("Line");
            sut.handleLeftClick(36, 12);
            sut.handleLeftClick(25, 57);
            verify(painterSpy, times(1)).paintLine(36, 12, 25, 57);
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
    }

}
