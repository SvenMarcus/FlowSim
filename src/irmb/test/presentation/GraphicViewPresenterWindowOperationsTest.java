package irmb.test.presentation;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import org.junit.Test;

import static irmb.mockito.verification.AtLeastThenForget.atLeastThenForget;
import static irmb.test.util.TestUtil.assertExpectedPointEqualsActual;
import static irmb.test.util.TestUtil.makePoint;
import static org.mockito.Mockito.verify;

/**
 * Created by sven on 19.01.17.
 */
public class GraphicViewPresenterWindowOperationsTest extends GraphicViewPresenterTest {

    @Test
    public void whenDraggingMouse_shouldMoveViewWindow() {
        buildLine(23, 14, 65, 48);
        verify(painterSpy, atLeastThenForget(1)).paintLine(23, 14, 65, 48);

        sut.handleWheelClick(10, 10);
        sut.handleMouseDrag(15, 17);
        sut.handleMouseRelease();

        verify(painterSpy, atLeastThenForget(1)).paintLine(28, 21, 70, 55);
    }

    @Test
    public void whenDraggingMouse_shapeCoordinatesShouldNotChange() {
        buildLine(23, 14, 65, 48);
        verify(painterSpy, atLeastThenForget(1)).paintLine(23, 14, 65, 48);

        Line line = (Line) shapeList.get(0).getShape();
        Point lineStart = makePoint(line.getFirst().getX(), line.getFirst().getY());
        Point lineEnd = makePoint(line.getSecond().getX(), line.getSecond().getY());

        sut.handleWheelClick(10, 10);
        sut.handleMouseDrag(15, 17);
        sut.handleMouseRelease();

        verify(painterSpy, atLeastThenForget(1)).paintLine(28, 21, 70, 55);
        assertExpectedPointEqualsActual(lineStart, line.getFirst());
        assertExpectedPointEqualsActual(lineEnd, line.getSecond());
    }
}
