package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 15.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class GraphicViewPresenterShapeBuildingTest extends GraphicViewPresenterTest {
    @Test
    public void whenClickingTwiceWithoutSettingObjectType_shouldDoNothing() {
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verifyZeroInteractions(painterSpy);
    }

    @Test
    public void whenClickingThreeTimes_shouldNotPaintAgain() {
        sut.beginPaint("Line");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        sut.handleLeftClick(99, 99);
        verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);
    }

    @Test
    public void whenRightClicking_shouldStopBuildingShape() {
        sut.beginPaint("PolyLine");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

        sut.handleRightClick();
        sut.handleLeftClick(36, 12);
        verifyNoMoreInteractions(painterSpy);
    }

    public class LivePaintingContext {
        @Test
        public void whenMovingMouseOnceAfterLeftClick_shouldAddNewPoint() {
            sut.beginPaint("Line");
            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            verify(painterSpy).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMovingMouse_shouldDoNothing() {
            sut.handleMouseMove(13, 15);
            verifyZeroInteractions(painterSpy);
        }

        @Test
        public void whenMovingMouseAfterSettingObjectType_shouldDoNothing() {
            sut.beginPaint("Line");
            sut.handleMouseMove(13, 15);
        }

        @Test
        public void whenMovingMouseTwice_shouldAdjustLastPoint() {
            sut.beginPaint("Line");
            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            sut.handleMouseMove(36, 12);
            verify(painterSpy).paintLine(13, 15, 36, 12);
        }

        @Test
        public void whenMovingMouseAfterSecondLeftClick_shouldNotAdjustPoint() {
            sut.beginPaint("Line");
            sut.handleLeftClick(13, 15);
            sut.handleMouseMove(18, 19);
            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
            sut.handleMouseMove(36, 12);
            verifyNoMoreInteractions(painterSpy);
        }
    }
}
