package presentation.command;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.presentation.command.ClearAllCommand;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by sven on 20.03.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class ClearAllCommandTest {

    private ClearAllCommand sut;
    private List<PaintableShape> shapeList;
    private PaintableShape paintableShape;

    @Before
    public void setUp() {
        shapeList = new ArrayList<>();
        paintableShape = mock(PaintableShape.class);
        shapeList.add(paintableShape);
        sut = new ClearAllCommand(shapeList);
    }

    @Test
    public void whenExecuting_shouldClearShapeList() {
        sut.execute();
        assertEquals(0, shapeList.size());
    }

    @Test
    public void whenCallingUndo_shouldDoNothing() {
        sut.undo();
        assertTrue(shapeList.size() == 1);
        assertTrue(shapeList.contains(paintableShape));
    }

    public class CommandExecutedContext {
        @Before
        public void setUp() {
            sut.execute();
        }

        @Test
        public void whenCallingUndo_shouldAddShapesAgain() {
            sut.undo();
            assertTrue(shapeList.contains(paintableShape));
            assertEquals(1, shapeList.size());
        }

        public class UndoCalledContext {
            @Before
            public void setUp() {
                sut.undo();
            }

            @Test
            public void whenCallingRedo_shouldClearShapes() {
                sut.redo();
                assertEquals(0, shapeList.size());
            }

            @Test
            public void whenCallingUndoAfterRedo_shouldAddShapesAgain() {
                sut.redo();
                sut.undo();
                assertTrue(shapeList.contains(paintableShape));
                assertEquals(1, shapeList.size());
            }
        }
    }


}