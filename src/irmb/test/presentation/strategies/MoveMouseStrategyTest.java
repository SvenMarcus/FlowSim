package irmb.test.presentation.strategies;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.presentation.command.MoveShapeCommand;
import irmb.flowsim.presentation.strategy.MoveMouseStrategy;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.presentation.strategy.StrategyEventArgs;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.Iterator;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 29.01.2017.
 */
@RunWith(HierarchicalContextRunner.class)
public class MoveMouseStrategyTest {

    private Command receivedCommand;
    private boolean hasNext;
    private GraphicView graphicView;
    private CommandQueue commandQueue;
    private List<PaintableShape> shapeList;
    private PaintableShape paintableShape;
    private Observer<StrategyEventArgs> observer;
    private CoordinateTransformer transformer;
    private MoveMouseStrategy sut;
    private Shape shape;

    @Before
    public void setUp() throws Exception {
        receivedCommand = null;
        shape = mock(Shape.class);
        graphicView = mock(GraphicView.class);
        commandQueue = mock(CommandQueue.class);
        shapeList = mock(List.class);
        paintableShape = mock(PaintableShape.class);
        setAcceptingPaintableShapeMockBehavior();
        setMockListBehavior();
        observer = mock(Observer.class);
        setObserverMockBehavior();
        transformer = mock(CoordinateTransformer.class);
        sut = new MoveMouseStrategy(shapeList, transformer);
        sut.addObserver(observer);

    }

    private void setObserverMockBehavior() {
        doAnswer(invocationOnMock -> {
            receivedCommand = ((StrategyEventArgs) invocationOnMock.getArgument(0)).getCommand();
            return null;
        }).when(observer).update(any());
    }

    private void setMockListBehavior() {
        when(shapeList.get(anyInt())).thenReturn(paintableShape);
        Iterator<PaintableShape> iteratorMock = mock(Iterator.class);
        when(iteratorMock.hasNext()).thenAnswer(invocationOnMock -> {
            hasNext = !hasNext;
            return hasNext;
        });
        when(iteratorMock.next()).thenAnswer(invocationOnMock -> paintableShape);
        when(shapeList.iterator()).thenReturn(iteratorMock);
    }

    private void setAcceptingPaintableShapeMockBehavior() {
        when(paintableShape.isPointOnBoundary(any(), anyDouble())).thenReturn(true);
        when(paintableShape.getShape()).thenReturn(shape);
    }

    @Test
    public void whenDraggingMouseAfterClickingOnShape_shouldNotifyObserverWithUpdate() {
        sut.onLeftClick(10, 10);
        sut.onMouseDrag(15, 21);
        verify(observer).update(argThat(args -> args.getState() == STRATEGY_STATE.UPDATE));
        assertNull(receivedCommand);
    }

    @Test
    public void whenDraggingMouse_shouldHaveMovedShapeBeforeUpdating() {
        sut.onLeftClick(10, 10);
        sut.onMouseDrag(15, 21);
        InOrder inOrder = inOrder(shape, observer);
        inOrder.verify(shape).moveBy(anyDouble(), anyDouble());
        inOrder.verify(observer).update(argThat(args -> args.getState() == STRATEGY_STATE.UPDATE));
    }

    @Test
    public void whenJustLeftClickingOnShape_shouldNotNotifyObserver() {
        sut.onLeftClick(12, 15);

        verifyZeroInteractions(observer);
    }

    @Test
    public void whenReleasingMouseAfterMovingShape_shouldNotifyObserverWithUpdateAndCommand() {
        sut.onLeftClick(10, 10);
        sut.onMouseDrag(15, 21);
        sut.onMouseRelease();

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer, atLeast(2)).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertThat(captor.getValue().getCommand(), is(instanceOf(MoveShapeCommand.class)));
    }


    @Test
    public void whenOnlyReleasingMouse_shouldNotNotifyObserver() {
        sut.onMouseRelease();
        verifyZeroInteractions(observer);
    }

    public class NoShapeAtClickedPointContext {
        @Before
        public void setUp() {
            setRejectingPaintableShapeMockBehavior();
        }

        private void setRejectingPaintableShapeMockBehavior() {
            when(paintableShape.isPointOnBoundary(any(), anyDouble())).thenReturn(false);
            when(paintableShape.getShape()).thenReturn(shape);
        }

        @Test
        public void whenDraggingMouse_shouldNotNotifyObserver() {
            sut.onLeftClick(12, 15);
            sut.onMouseDrag(15, 45);

            verifyZeroInteractions(observer);
        }
    }
}