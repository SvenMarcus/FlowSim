package irmb.test.presentation.command;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Line;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Sven on 03.01.2017.
 */
@RunWith(HierarchicalContextRunner.class)
public class AddPaintableShapeCommandTest {

    private AddPaintableShapeCommand sut;
    private PaintableShape line;
    private List<PaintableShape> shapeList;

    @Before
    public void setUp() throws Exception {
        line = new PaintableLine(new Line());
        shapeList = new LinkedList<>();
        sut = new AddPaintableShapeCommand(line, shapeList);
    }

    @Test
    public void whenExecuting_shouldAddShapeToList() {
        sut.execute();
        assertTrue(shapeList.contains(line));
    }

    public class CommandExecutedContext {
        @Before
        public void setUp() {
            sut.execute();
        }

        @Test
        public void whenCallingExecute_shouldNotAddShapeAgain() {
            sut.execute();
            assertTrue(shapeList.contains(line));
            assertEquals(1, shapeList.size());
        }

        @Test
        public void whenCallingUndo_shouldRemoveShapeFromList() {
            sut.undo();
            assertFalse(shapeList.contains(line));
        }

        public class UndoCalledContext {
            @Before
            public void setUp() {
                sut.undo();
            }

            @Test
            public void whenCallingRedo_shouldAddShapeToList() {
                sut.redo();
                assertTrue(shapeList.contains(line));
            }
        }
    }

}