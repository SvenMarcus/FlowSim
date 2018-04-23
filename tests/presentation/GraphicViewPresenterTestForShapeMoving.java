package presentation;

import org.junit.Test;

import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.verify;

/**
 * Created by sven on 17.02.17.
 */
public class GraphicViewPresenterTestForShapeMoving extends GraphicViewPresenterTest {

    @Test
    public void whenDraggingDefinedPoint_shouldOnlyMovePoint() {
        buildLine(13, 15, 18, 19);
        clearInvocations(painterSpy);
        clearInvocations(graphicView);

        sut.handleLeftClick(13, 15);
        sut.handleMouseDrag(5, 44);
        verify(painterSpy).paintLine(5, 44, 18, 19);
    }

}
