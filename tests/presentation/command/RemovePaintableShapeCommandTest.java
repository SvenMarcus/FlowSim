package presentation.command;

import irmb.flowsim.presentation.command.RemovePaintableShapeCommand;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by sven on 16.02.17.
 */
public class RemovePaintableShapeCommandTest {

    private List<PaintableShape> shapeList;
    private PaintableShape paintableShape;
    private RemovePaintableShapeCommand sut;
    private boolean containsShape = true;

    @Before
    public void setUp() throws Exception {
        containsShape = true;
        shapeList = mock(List.class);
        paintableShape = mock(PaintableShape.class);
        when(shapeList.contains(paintableShape)).thenAnswer(invocationOnMock -> containsShape);
        when(shapeList.remove(paintableShape)).thenAnswer(invocationOnMock -> containsShape = false);
        when(shapeList.add(paintableShape)).thenAnswer(invocationOnMock -> containsShape = true);
        sut = new RemovePaintableShapeCommand(shapeList, paintableShape);
    }

    @Test
    public void whenExecuting_shouldRemoveShapeFromList() {
        sut.execute();
        verify(shapeList).remove(paintableShape);
    }

    @Test
    public void whenCallingUndo_shouldAddShapeToList() {
        sut.execute();

        sut.undo();
        verify(shapeList).add(paintableShape);
    }

    @Test
    public void whenOnlyCallingUndo_shouldDoNothing() {
        sut.undo();
        verify(shapeList, never()).add(paintableShape);
    }

    @Test
    public void whenCallingRedo_shouldRemoveShapeAgain() {
        sut.execute();
        sut.undo();
        clearInvocations(shapeList);

        sut.redo();
        verify(shapeList).remove(paintableShape);
    }
}