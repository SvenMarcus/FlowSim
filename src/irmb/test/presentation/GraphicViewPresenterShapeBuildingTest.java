package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
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
    public void whenRightClicking_shouldDoNothing() {
        sut.handleRightClick(0, 0);
        verifyZeroInteractions(painterSpy);
    }

    public class TwoPointShapeContext {
        @Test
        public void whenClickingThreeTimes_shouldNotPaintAgain() {
            sut.beginPaint("Line");

            sut.handleLeftClick(13, 15);
            sut.handleLeftClick(18, 19);

            sut.handleLeftClick(99, 99);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);
        }
    }

    public class PolyLineContext {
        @Test
        public void whenRightClicking_shouldStopBuildingShape() {
            sut.beginPaint("PolyLine");

            sut.handleLeftClick(13, 15);

            sut.handleLeftClick(18, 19);
            verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

            sut.handleRightClick(0, 0);

            sut.handleLeftClick(36, 12);
            verify(painterSpy, never()).paintLine(18, 19, 36, 12);
        }
    }

    public class LivePaintingContext {


        @Test
        public void whenMovingMouse_shouldDoNothing() {
            sut.handleMouseMove(13, 15);
            verifyZeroInteractions(painterSpy);
        }


        public class TwoPointShapeContext {
            @Before
            public void setUp() {
                sut.beginPaint("Line");
            }

            @Test
            public void whenMovingMouseOnceAfterLeftClick_shouldAddNewPoint() {
                sut.handleLeftClick(13, 15);

                sut.handleMouseMove(18, 19);
                verify(painterSpy).paintLine(13, 15, 18, 19);
            }

            @Test
            public void whenMovingMouseAfterSettingObjectType_shouldDoNothing() {
                sut.handleMouseMove(13, 15);
                verifyZeroInteractions(painterSpy);
            }

            @Test
            public void whenMovingMouseTwice_shouldAdjustLastPoint() {
                sut.handleLeftClick(13, 15);
                sut.handleMouseMove(18, 19);

                sut.handleMouseMove(36, 12);
                verify(painterSpy).paintLine(13, 15, 36, 12);
            }

            @Test
            public void whenMovingMouseAfterSecondLeftClick_shouldNotAdjustPoint() {
                sut.handleLeftClick(13, 15);
                sut.handleMouseMove(18, 19);

                sut.handleLeftClick(18, 19);
                verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);

                sut.handleMouseMove(36, 12);
                verifyNoMoreInteractions(painterSpy);
            }

            @Test
            public void whenRightClickingAfterAddingOnePoint_shouldRemoveLine() {
                sut.handleLeftClick(13, 15);
                sut.handleMouseMove(18, 19);

                sut.handleRightClick(0, 0);
                verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);
                verify(graphicView, times(2)).update();
            }
        }

        public class PolyLineContext {
            @Before
            public void setUp() {
                sut.beginPaint("PolyLine");
            }

            @Test
            public void whenMovingMouseTwice_shouldOnlyAddOnePoint() {
                sut.handleLeftClick(13, 15);

                sut.handleMouseMove(18, 19);
                verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

                sut.handleMouseMove(36, 25);
                verify(painterSpy, times(1)).paintLine(13, 15, 36, 25);
            }

            @Test
            public void whenRightClickingAfterAddingTwoPoints_shouldRemoveLastPoint() {
                sut.handleLeftClick(13, 15);
                sut.handleMouseMove(18, 19);
                sut.handleLeftClick(18, 19);
                sut.handleMouseMove(36, 25);

                sut.handleRightClick(0, 0);
                verify(painterSpy, times(4)).paintLine(13, 15, 18, 19);
                verify(painterSpy, times(1)).paintLine(18, 19, 36, 25);
            }

            @Test
            public void afterUpdatingGraphicView_shouldStillPaintPolyLine() {
                sut.handleLeftClick(13, 15);
                sut.handleMouseMove(18, 19);
                sut.handleLeftClick(18, 19);
                sut.handleMouseMove(36, 25);

                sut.handleRightClick(0, 0);

                graphicView.update();
                verify(painterSpy, times(5)).paintLine(13, 15, 18, 19);
            }
        }
    }
}
