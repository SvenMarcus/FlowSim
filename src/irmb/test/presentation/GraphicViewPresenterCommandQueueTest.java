package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.mockito.verification.AtLeastThenForget.atLeastThenForget;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 22.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class GraphicViewPresenterCommandQueueTest extends GraphicViewPresenterTest {

    @Test
    public void whenBuildingShapeThenCallingUndo_shouldRemoveShape() {
        buildLine(13, 15, 18, 19);
        verify(painterSpy, atLeastThenForget(2)).paintLine(13, 15, 18, 19);

        sut.undo();
        verify(graphicView, atLeast(3)).update();
        verifyNoMoreInteractions(painterSpy);
    }

    public class LineAddedContext {
        @Before
        public void setUp() {
            buildLine(13, 15, 18, 19);
            verify(painterSpy, atLeastThenForget(2)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMovingLineThenCallingUndo_shouldUndoMove() {
            performMove(13, 15, 18, 21);
            verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMovingLineThenCallingUndoTwice_shouldUndoMoveAndRemoveLine() {
            performMove(13, 15, 18, 21);
            verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

            sut.undo();
            verify(graphicView, atLeastThenForget(5)).update();
            verifyNoMoreInteractions(painterSpy);
        }

        public class MovedOnceContext {
            @Before
            public void setUp() {
                performMove(13, 15, 18, 21);
                verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);
            }

            @Test
            public void whenAddingLineThenCallingUndoTwice_shouldRemoveLineThenUndoMove() {
                buildLine(34, 43, 50, 62);
                verify(painterSpy, atLeastThenForget(2)).paintLine(34, 43, 50, 62);

                sut.undo();
                verify(graphicView, atLeastThenForget(6)).update();
                verify(painterSpy, atLeastThenForget(1)).paintLine(18, 21, 23, 25);
                verifyNoMoreInteractions(painterSpy);

                sut.undo();
                verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
            }
        }

        @Test
        public void whenMovingWindowAndDraggingTwiceThenUndo_shouldUndoBothPans() {
            sut.handleMiddleClick(10, 10);
            sut.handleMouseDrag(20, 5);
            sut.handleMouseDrag(15, 15);
            sut.handleMouseRelease();

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenMiddleClickingWithoutDragThenUndo_shouldRemoveLine() {
            sut.handleMiddleClick(10, 5);
            sut.handleMouseRelease();

            sut.undo();
            graphicView.update();
            verifyNoMoreInteractions(painterSpy);
        }

        @Test
        public void whenMovingWindowTwiceThenUndoTwice_shouldUndoBothPans() {
            sut.handleMiddleClick(10, 10);
            sut.handleMouseDrag(20, 5);
            sut.handleMouseRelease();
            verify(painterSpy, atLeastThenForget(1)).paintLine(23, 10, 28, 14);

            sut.handleMiddleClick(10, 10);
            sut.handleMouseDrag(3, 18);
            sut.handleMouseRelease();
            verify(painterSpy, atLeastThenForget(1)).paintLine(16, 18, 21, 22);

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(23, 10, 28, 14);

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        }

        public class MovedViewWindowContext {
            @Before
            public void setUp() {
                sut.handleMiddleClick(10, 10);
                sut.handleMouseDrag(20, 5);
                sut.handleMouseRelease();
                verify(painterSpy, atLeastThenForget(1)).paintLine(23, 10, 28, 14);
            }

            @Test
            public void whenCallingUndo_shouldUndoPan() {
                sut.undo();
                verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
            }

            @Test
            public void whenCallingUndo_shouldOnlyUndoPan() {
                sut.undo();
                verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

                graphicView.update();
                verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
            }

            @Test
            public void whenCallingUndoThenRedo_shouldRedoPan() {
                sut.undo();
                verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);

                sut.redo();
                verify(painterSpy, atLeastThenForget(1)).paintLine(23, 10, 28, 14);
            }
        }

        @Test
        public void whenZoomingThenCallingUndo_shouldUndoZoom() {
            sut.handleScroll(55, 33, 1);
            clearInvocations(painterSpy);

            sut.undo();
            verify(painterSpy).paintLine(13, 15, 18, 19);
        }

        public class ZoomedContext {
            @Before
            public void setUp() {
                sut.handleScroll(54, 52, -2);
                clearInvocations(painterSpy);
            }

            @Test
            public void whenCallingUndoThenRedo_shouldRedoZoom() {
                sut.undo();
                clearInvocations(painterSpy);

                sut.redo();
                verify(painterSpy).paintLine(15, 17, 20, 20);
            }
        }

        @Test
        public void whenDeletingLineThenUndo_shouldRestoreLine() {
            sut.handleRightClick(13, 15);
            clearInvocations(graphicView);
            clearInvocations(painterSpy);

            sut.undo();
            verify(painterSpy, atLeastThenForget(1)).paintLine(13, 15, 18, 19);
        }

        @Test
        public void whenDeletingLineThenUndoThenRedo_shouldRemoveLine() {
            sut.handleRightClick(13, 15);
            sut.undo();
            clearInvocations(graphicView);
            clearInvocations(painterSpy);

            sut.redo();
            verify(graphicView).update();
            verifyZeroInteractions(painterSpy);
        }
    }
}
