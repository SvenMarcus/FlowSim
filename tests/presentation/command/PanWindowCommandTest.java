package presentation.command;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.command.PanWindowCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.mockito.verification.AtLeastThenForget.atLeastThenForget;
import static org.mockito.Mockito.*;

/**
 * Created by sven on 24.01.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class PanWindowCommandTest {

    private CoordinateTransformer transformer;
    private PanWindowCommand sut;

    @Before
    public void setUp() throws Exception {
        transformer = mock(CoordinateTransformer.class);
        sut = new PanWindowCommand(transformer);
    }

    @Test
    public void testCanSetDelta() {
        sut.setDelta(10, -5);
    }

    @Test
    public void whenCallingExecute_shouldMoveViewWindow() {
        sut.setDelta(10, -5);
        sut.execute();
        verify(transformer).moveViewWindow(10, -5);

        sut.setDelta(15, 21);
        sut.execute();
        verify(transformer).moveViewWindow(15, 21);
    }

    public class CommandExecutedContext {
        @Before
        public void setUp() {
            sut.setDelta(10, -5);
            sut.execute();
            verify(transformer, atLeastThenForget(1)).moveViewWindow(10, -5);
        }

        @Test
        public void whenCallingUndo_shouldUndoMoveViewWindow() {
            sut.undo();
            verify(transformer, atLeastThenForget(1)).moveViewWindow(-10, 5);
        }

        public class CalledUndoOnceContext {
            @Before
            public void setUp() {
                sut.undo();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(-10, 5);
            }

            @Test
            public void whenCallingRedo_shouldRedoMoveViewWindow() {
                sut.redo();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(10, -5);
            }

            @Test
            public void whenCallingUndoAgain_shouldDoNothing() {
                sut.undo();
                verifyNoMoreInteractions(transformer);
            }

            public class RedoCalledContext {
                @Before
                public void setUp() {
                    sut.redo();
                    verify(transformer, atLeastThenForget(1)).moveViewWindow(10, -5);
                }

                @Test
                public void whenCallingRedoAgain_shouldDoNothing() {
                    sut.redo();
                    verifyNoMoreInteractions(transformer);
                }
            }
        }

        public class CommandExecutedTwiceContext {
            @Before
            public void setUp() {
                sut.setDelta(25, 13);
                sut.execute();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(25, 13);
            }

            @Test
            public void whenCallingUndo_shouldUndoBothMoves() {
                sut.undo();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(-35, -8);
            }

            @Test
            public void whenCallingRedoAfterUndo_shouldRedoBothMoves() {
                sut.undo();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(-35, -8);

                sut.redo();
                verify(transformer, atLeastThenForget(1)).moveViewWindow(35, 8);
            }
        }

    }

}