package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.STRATEGY_STATE;
import irmb.flowsim.presentation.strategy.StrategyEventArgs;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.test.simulation.SimulationMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by sven on 03.03.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class SimulationGraphicViewPresenterTest {

    private Painter painterSpy;
    private GraphicView graphicViewMock;
    private SimulationGraphicViewPresenter sut;
    private CoordinateTransformer transformer;
    private SimulationMock simulationSpy;
    private List<PaintableShape> shapeList;
    private MouseStrategy mouseStrategyMock;
    private Observer<StrategyEventArgs> mouseStrategyObserver;
    private Observer<String> commandQueueObserver;
    private CommandQueue commandQueue;

    @Before
    public void setUp() {
        makeStrategyMock();
        MouseStrategyFactory mouseStrategyFactory = makeMouseStrategyFactory();
        makeCommandQueueMock();
        shapeList = new ArrayList<>();
        transformer = mock(CoordinateTransformer.class);
        simulationSpy = spy(new SimulationMock());
        SimulationFactory simulationFactory = mock(SimulationFactory.class);
        when(simulationFactory.makeSimulation()).thenReturn(simulationSpy);
        sut = new SimulationGraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList, transformer, simulationFactory);
        painterSpy = mock(Painter.class);
        graphicViewMock = mock(GraphicView.class);
        setGraphicViewMockBehavior();
        sut.setGraphicView(graphicViewMock);
    }

    private MouseStrategyFactory makeMouseStrategyFactory() {
        MouseStrategyFactory mouseStrategyFactory = mock(MouseStrategyFactory.class);
        when(mouseStrategyFactory.makeStrategy(anyString())).thenReturn(mouseStrategyMock);
        return mouseStrategyFactory;
    }

    private void makeStrategyMock() {
        mouseStrategyMock = mock(MouseStrategy.class);
        doAnswer(invocationOnMock -> mouseStrategyObserver = invocationOnMock.getArgument(0)).when(mouseStrategyMock).addObserver(any());
        doAnswer(invocationOnMock -> {
            StrategyEventArgs argument = invocationOnMock.getArgument(0);
            mouseStrategyObserver.update(argument);
            return null;
        }).when(mouseStrategyMock).notifyObservers(any());
    }

    private void makeCommandQueueMock() {
        commandQueue = mock(CommandQueue.class);
        doAnswer(invocationOnMock -> commandQueueObserver = invocationOnMock.getArgument(0)).when(commandQueue).addObserver(any());
        doAnswer(invocationOnMock -> {
            String argument = invocationOnMock.getArgument(0);
            commandQueueObserver.update(argument);
            return null;
        }).when(commandQueue).notifyObservers(any());
    }

    private void setGraphicViewMockBehavior() {
        doAnswer(invocationOnMock -> {
            for (Paintable shape : sut.getPaintableList())
                shape.paint(painterSpy, transformer);
            return null;
        }).when(graphicViewMock).update();
    }

    @Test
    public void whenCallingAddSimulation_shouldPaintSimulation() {
        sut.addSimulation();
        verify(simulationSpy, atLeastOnce()).paint(painterSpy, transformer);
        verify(graphicViewMock, atLeastOnce()).update();
    }

    @Test
    public void whenCallingRunSimulation_shouldDoNothing() {
        sut.runSimulation();
        verifyZeroInteractions(simulationSpy);
    }

    @Test
    public void whenCallingPauseSimulation_shouldDoNothing() {
        sut.pauseSimulation();
        verifyZeroInteractions(simulationSpy);
    }

    public class ShapeAddedContext {

        private PaintableShape paintableShape;

        @Before
        public void setUp() {
            paintableShape = mock(PaintableShape.class);
            shapeList.add(paintableShape);
        }

        @Test
        public void whenAddingSimulation_shouldPaintShapeAndSimulation() {
            sut.addSimulation();
            verify(paintableShape, atLeastOnce()).paint(painterSpy, transformer);
            verify(simulationSpy, atLeastOnce()).paint(painterSpy, transformer);
            assertEquals(sut.getPaintableList().get(0), simulationSpy);
        }

        @Test
        public void whenPaintingBeforeAddingSimulation_shouldDoNothing() {
            graphicViewMock.update();
            verifyZeroInteractions(simulationSpy);
            verifyZeroInteractions(painterSpy);
        }

        @Test
        public void whenAddingSimulation_shouldMapShapesToGrid() {
            sut.addSimulation();
            verify(simulationSpy).setShapes(shapeList);
        }
    }

    public class SimulationAddedContext {
        @Before
        public void setUp() {
            sut.addSimulation();
            clearInvocations(simulationSpy);
            clearInvocations(graphicViewMock);
        }

        @Test
        public void whenCallingRunSimulation_shouldRunSimulation() {
            sut.runSimulation();
            verify(simulationSpy, only()).run();
        }

        @Test
        public void whenCallingPauseSimulation_shouldPauseSimulation() {
            sut.pauseSimulation();
            verify(simulationSpy, only()).pause();
        }

        @Test
        public void whenCallingStopSimulation_shouldStopSimulation() {
            sut.pauseSimulation();
            verify(simulationSpy, only()).pause();
        }

        @Test
        public void whenReceivingUpdateFromSimulation_shouldUpdateGraphicView() throws IllegalAccessException, InstantiationException {
            simulationSpy.notifyObservers(null);
            verify(graphicViewMock, atLeastOnce()).update();
        }

        @Test
        public void whenReceivingUpdateFromStrategy_shouldMapShapesToGrid() {
            mouseStrategyMock.notifyObservers(new StrategyEventArgs(STRATEGY_STATE.UPDATE));
            verify(simulationSpy).setShapes(shapeList);
        }

        @Test
        public void whenReceivingUpdateFromCommandQueue_shouldMapShapesToGrid() {
            commandQueue.notifyObservers("undo");
            verify(simulationSpy).setShapes(shapeList);

            clearInvocations(simulationSpy);
            clearInvocations(commandQueue);

            commandQueue.notifyObservers("redo");
            verify(simulationSpy).setShapes(shapeList);
        }
    }
}
