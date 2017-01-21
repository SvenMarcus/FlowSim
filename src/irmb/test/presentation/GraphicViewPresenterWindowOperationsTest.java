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

        performPan(10, 10, 15, 17);

        verify(painterSpy, atLeastThenForget(1)).paintLine(28, 21, 70, 55);
    }

    @Test
    public void whenDraggingMouse_shapeCoordinatesShouldNotChange() {
        buildLine(23, 14, 65, 48);
        verify(painterSpy, atLeastThenForget(1)).paintLine(23, 14, 65, 48);

        Line line = (Line) shapeList.get(0).getShape();
        Point lineStart = makePoint(line.getFirst().getX(), line.getFirst().getY());
        Point lineEnd = makePoint(line.getSecond().getX(), line.getSecond().getY());

        performPan(10, 10, 15, 17);

        verify(painterSpy, atLeastThenForget(1)).paintLine(28, 21, 70, 55);
        assertExpectedPointEqualsActual(lineStart, line.getFirst());
        assertExpectedPointEqualsActual(lineEnd, line.getSecond());
    }

    @Test
    public void whenDraggingMouseTwice_shouldMoveWindowTwice() {
        buildLine(23, 14, 65, 48);
        verify(painterSpy, atLeastThenForget(1)).paintLine(23, 14, 65, 48);

        sut.handleMiddleClick(10, 10);
        sut.handleMouseDrag(15, 17);

        verify(painterSpy, atLeastThenForget(1)).paintLine(28, 21, 70, 55);

        sut.handleMouseDrag(8, 12);
        verify(painterSpy, atLeastThenForget(1)).paintLine(21, 16, 63, 50);
    }

    private void performPan(double x1, double y1, double x2, double y2) {
        sut.handleMiddleClick(x1, y1);
        sut.handleMouseDrag(x2, y2);
        sut.handleMouseRelease();
    }
}
