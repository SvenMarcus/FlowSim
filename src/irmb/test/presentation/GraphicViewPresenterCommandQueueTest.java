package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * Created by Sven on 22.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class GraphicViewPresenterCommandQueueTest extends GraphicViewPresenterTest {

    @Test
    public void whenBuildingShapeThenCallingUndo_shouldRemoveShape() {
        buildLine(13, 15, 18, 19);
        verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);

        sut.undo();
        verify(graphicView, times(3)).update();
        verifyNoMoreInteractions(painterSpy);
    }


    public class LineAddedContext {
        @Before
        public void setUp() {
            buildLine(13, 15, 18, 19);
            verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMovingLineThenCallingUndo_shouldUndoMove() {
            performMove(13, 15, 18, 21);
            verify(painterSpy, times(1)).paintLine(18, 21, 23, 25);

            sut.undo();
            verify(painterSpy, times(3)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMovingLineThenCallingUndoTwice_shouldUndoMoveAndRemoveLine() {
            performMove(13, 15, 18, 21);
            verify(painterSpy, times(1)).paintLine(18, 21, 23, 25);

            sut.undo();
            verify(painterSpy, times(3)).paintLine(13, 15, 18, 19);

            sut.undo();
            verify(graphicView, times(5)).update();
            verifyNoMoreInteractions(painterSpy);
        }

        public class MovedOnceContext {
            @Before
            public void setUp() {
                performMove(13, 15, 18, 21);
                verify(painterSpy, times(1)).paintLine(18, 21, 23, 25);
            }

            @Test
            public void whenAddingLineThenCallingUndoTwice_shouldRemoveLineThenUndoMove() {
                buildLine(34, 43, 50, 62);
                verify(painterSpy, times(2)).paintLine(34, 43, 50, 62);

                sut.undo();
                verify(graphicView, times(6)).update();
                verify(painterSpy, times(4)).paintLine(18, 21, 23, 25);
                verifyNoMoreInteractions(painterSpy);

                sut.undo();
                verify(painterSpy, times(3)).paintLine(13, 15, 18, 19);
            }
        }


    }
}
