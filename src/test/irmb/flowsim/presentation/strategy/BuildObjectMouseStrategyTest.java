package irmb.test.presentation.strategy;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.presentation.command.PanWindowCommand;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.strategy.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.presentation.strategy.StrategyEventArgs;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;


import static irmb.mockito.verification.AtLeastThenForget.atLeastThenForget;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 09.01.2017.
 */
public class BuildObjectMouseStrategyTest {

    private CommandQueue commandQueue;
    private PaintableShapeBuilderFactory factory;
    private GraphicView graphicView;
    private List<PaintableShape> shapeList;
    private Observer<StrategyEventArgs> observer;
    private BuildObjectMouseStrategy sut;
    private int pointsAdded;
    private PaintableShapeBuilder lineBuilderMock;
    private CoordinateTransformer transformer;

    private Command receivedCommand;

    @Before
    public void setUp() throws Exception {
        pointsAdded = 0;
        receivedCommand = null;
        commandQueue = mock(CommandQueue.class);
        graphicView = mock(GraphicView.class);
        shapeList = mock(List.class);
        observer = mock(Observer.class);
        setObserverMockBehavior();
        transformer = mock(CoordinateTransformer.class);
        setTransformerMockBehavior();
        lineBuilderMock = mock(PaintableShapeBuilder.class);
        setLineBuilderMockBehavior();
        factory = mock(PaintableShapeBuilderFactory.class);
        setFactoryMockBehavior();
    }

    private void setObserverMockBehavior() {
        doAnswer(invocationOnMock -> {
            receivedCommand = ((StrategyEventArgs) invocationOnMock.getArgument(0)).getCommand();
            return null;
        }).when(observer).update(any());
    }

    private void setFactoryMockBehavior() {
        when(factory.makeShapeBuilder("Line")).thenReturn(lineBuilderMock);
        PaintableShapeBuilder polyLineBuilderMock = mock(PaintableShapeBuilder.class);
        when(polyLineBuilderMock.isObjectFinished()).thenAnswer(invocationOnMock -> false);
        when(factory.makeShapeBuilder("PolyLine")).thenReturn(polyLineBuilderMock);
    }

    private void setTransformerMockBehavior() {
        when(transformer.transformToWorldPoint(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private void setLineBuilderMockBehavior() {
        doAnswer(invocationOnMock -> {
            incrementPointsAdded();
            return null;
        }).when(lineBuilderMock).addPoint(any());
        when(lineBuilderMock.isObjectFinished()).thenAnswer(invocationOnMock -> pointsAdded >= 2);
    }

    private void incrementPointsAdded() {
        pointsAdded++;
    }

    private void makeBuildObjectMouseStrategyWith(String type) {
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        sut = new BuildObjectMouseStrategy(shapeList, transformer, builder);
        sut.addObserver(observer);
    }

    @Test
    public void whenFinishingTwoPointObject_shouldNotifyObserver() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onLeftClick(3, 4);
        sut.onLeftClick(5, 6);

        assertThatObserverWasNotifiedWithFinishedAndCommand();
    }

    @Test
    public void whenMovingMouse_shouldNotNotifyObserver() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onMouseMove(3, 5);
        verifyZeroInteractions(observer);
    }

    @Test
    public void whenAddingOnePointAndMovingMouse_shouldNotifyObserverWithUpdate() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onLeftClick(3, 4);
        sut.onMouseMove(10, 45);

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(receivedCommand);
    }

    @Test
    public void whenMovingMouseTwiceAfterAddingPoint_shouldNotifyObserverWithUpdate() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onLeftClick(3, 4);
        sut.onMouseMove(10, 45);
        sut.onMouseMove(12, 15);

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer, times(2)).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(receivedCommand);
    }

    private void assertThatObserverWasNotifiedWithFinishedAndCommand() {
        verify(observer).update(argThat(args -> args.getState() == STRATEGY_STATE.FINISHED));
        assertThat(receivedCommand, is(instanceOf(AddPaintableShapeCommand.class)));
    }

    @Test
    public void whenRightClicking_shouldNotifyObserver() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onRightClick(0, 0);

        verify(observer).update(argThat(args -> args.getState() == STRATEGY_STATE.FINISHED));
    }

    @Test
    public void whenRightClickingAfterAddingOnePoint_eventArgsShouldNotContainCommand() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onLeftClick(10, 10);
        sut.onRightClick(0, 0);

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer).update(captor.capture());
        assertNull(captor.getValue().getCommand());
    }

    @Test
    public void whenRightClickingAfterAddingMoreThanTwoPoints_eventArgsShouldHaveAddPaintableShapeCommand() {
        makeBuildObjectMouseStrategyWith("PolyLine");

        sut.onLeftClick(10, 10);
        sut.onLeftClick(13, 15);
        sut.onLeftClick(35, 22);
        sut.onRightClick(0, 0);

        assertThatObserverWasNotifiedWithFinishedAndCommand();
    }

    @Test
    public void whenBuildingMultiPointObject_shouldNotNotifyObserverWithUpdateAfterEachClick() {
        makeBuildObjectMouseStrategyWith("PolyLine");
        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);

        sut.onLeftClick(3, 4);

        sut.onLeftClick(5, 6);
        verify(observer, atLeastThenForget(1)).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(captor.getValue().getCommand());

        sut.onLeftClick(7, 8);
        verify(observer, atLeastThenForget(1)).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(captor.getValue().getCommand());

        sut.onLeftClick(9, 10);
        verify(observer, atLeastThenForget(1)).update(captor.capture());
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(captor.getValue().getCommand());

        sut.onRightClick(0, 0);
        verify(observer, atLeastThenForget(1)).update(argThat(args -> args.getState() == STRATEGY_STATE.FINISHED));
    }

    @Test
    public void whenDraggingWithMiddleButton_shouldPanWindow() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onMiddleClick(52, 13);
        sut.onMouseDrag(100, 65);
        verify(transformer).moveViewWindow(48, 52);
    }

    @Test
    public void whenDraggingWithMiddleButton_shouldNotifyObserverWithUpdate() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onMiddleClick(52, 13);
        sut.onMouseDrag(100, 65);

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer).update(captor.capture());
        assertThat(captor.getValue(), is(instanceOf(StrategyEventArgs.class)));
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertNull(captor.getValue().getCommand());
    }

    @Test
    public void whenReleasingMouseAfterPan_shouldNotifyObserverWithCommand() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onMiddleClick(52, 13);
        sut.onMouseDrag(100, 65);
        sut.onMouseRelease();

        ArgumentCaptor<StrategyEventArgs> captor = ArgumentCaptor.forClass(StrategyEventArgs.class);
        verify(observer, atLeast(2)).update(captor.capture());
        assertThat(captor.getValue(), is(instanceOf(StrategyEventArgs.class)));
        assertEquals(STRATEGY_STATE.UPDATE, captor.getValue().getState());
        assertThat(captor.getValue().getCommand(), is(instanceOf(PanWindowCommand.class)));
    }
}