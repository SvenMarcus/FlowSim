package irmb.test.presentation.command;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Line;
import irmb.flowsim.presentation.command.MoveShapeCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Sven on 02.01.2017.
 */
@RunWith(HierarchicalContextRunner.class)
public class MoveShapeCommandTest {

    private MoveShapeCommand sut;
    private Line line;

    @Before
    public void setUp() {
        line = new Line();
        line.setFirst(makePoint(11, 12));
        line.setSecond(makePoint(21, 22));
        sut = new MoveShapeCommand(line);
    }

    @Test
    public void whenExecuting_shouldMoveShape() {
        sut.setDelta(3, 4);
        sut.execute();
        assertExpectedPointEqualsActual(makePoint(14, 16), line.getFirst());
        assertExpectedPointEqualsActual(makePoint(24, 26), line.getSecond());
    }

    @Test
    public void whenCallingUndo_shouldDoNothing() {
        sut.undo();
        assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
        assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
    }

    @Test
    public void whenCallingUndoAfterSettingDelta_shouldDoNothing() {
        sut.setDelta(3, 4);
        sut.undo();
        assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
        assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
    }

    @Test
    public void whenCallingRedo_shouldDoNothing() {
        sut.redo();
        assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
        assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
    }

    @Test
    public void whenCallingRedoAfterSettingDelta_shouldDoNothing() {
        sut.setDelta(3, 4);
        sut.redo();
        assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
        assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
    }

    public class CommandExecutedContext {
        @Before
        public void setUp() {
            sut.setDelta(3, 4);
            sut.execute();
        }

        @Test
        public void whenCallingUndo_shouldMoveToOriginalPosition() {
            sut.undo();
            assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
            assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
        }

        public class UndoCalledContext {
            @Before
            public void setUp() {
                sut.undo();
            }

            @Test
            public void whenCallingUndo_shouldDoNothing() {
                sut.undo();
                assertExpectedPointEqualsActual(makePoint(11, 12), line.getFirst());
                assertExpectedPointEqualsActual(makePoint(21, 22), line.getSecond());
            }

            @Test
            public void whenCallingRedo_shouldMoveShapeAgain() {
                sut.redo();
                assertExpectedPointEqualsActual(makePoint(14, 16), line.getFirst());
                assertExpectedPointEqualsActual(makePoint(24, 26), line.getSecond());
            }

            public class RedoCalledContext {
                @Before
                public void setUp() {
                    sut.redo();
                }

                @Test
                public void whenCallingRedo_shouldDoNothing() {
                    sut.redo();
                    assertExpectedPointEqualsActual(makePoint(14, 16), line.getFirst());
                    assertExpectedPointEqualsActual(makePoint(24, 26), line.getSecond());
                }
            }
        }
    }

}