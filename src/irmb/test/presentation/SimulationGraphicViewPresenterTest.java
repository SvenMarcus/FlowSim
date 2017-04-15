package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Point;
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
import irmb.flowsim.simulation.SimulationFactoryImpl;
import irmb.flowsim.simulation.visualization.GridNodeStyle;
import irmb.flowsim.simulation.visualization.PlotStyle;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.test.simulation.SimulationSpy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
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
    private SimulationSpy simulationSpy;
    private List<PaintableShape> shapeList;
    private MouseStrategy mouseStrategyMock;
    private Observer<StrategyEventArgs> mouseStrategyObserver;
    private Observer<String> commandQueueObserver;
    private CommandQueue commandQueue;
    private MouseStrategyFactory mouseStrategyFactory;

    @Before
    public void setUp() {
        makeStrategyMock();
        mouseStrategyFactory = makeMouseStrategyFactory();
        makeCommandQueueMock();
        shapeList = new ArrayList<>();
        transformer = mock(CoordinateTransformer.class);
        simulationSpy = spy(new SimulationSpy());
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

    @Test
    public void whenReceivingUpdateFromCommandQueue_shouldNotMapToSimulation() {
        commandQueue.notifyObservers("undo");
        verifyZeroInteractions(simulationSpy);
    }

    @Test
    public void whenCallingClearAll_shouldNotMapToSimulation() {
        sut.clearAll();
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

        @Test
        public void whenAddingColorPlotStyleThenAddingSimulation_shouldAddColorStyleToSimulation() {
            sut.togglePlotStyle(PlotStyle.Color);
            sut.addSimulation();
            assertTrue(simulationSpy.isColorStyleAdded());
        }

        @Test
        public void whenAddingBothArrowAndColorPlotStyleThenAddingSimulation_shouldAddBothStylesToSimulation() {
            sut.togglePlotStyle(PlotStyle.Color);
            sut.togglePlotStyle(PlotStyle.Arrow);
            sut.addSimulation();
            assertTrue(simulationSpy.isColorStyleAdded());
            assertTrue(simulationSpy.isArrowStyleAdded());
        }

        @Test
        public void whenAddingSimulationWithNoStyles_shouldNotAddPlotStyle() {
            sut.addSimulation();
            verify(simulationSpy, never()).addPlotStyle(any());
        }

        @Test
        public void whenAddingThenRemovingPlotStyleThenAddingSimulation_shouldNotAddAnyPlotStyles() {
            sut.togglePlotStyle(PlotStyle.Color);
            sut.togglePlotStyle(PlotStyle.Color);
            clearInvocations(simulationSpy);

            sut.addSimulation();
            verify(simulationSpy, never()).addPlotStyle(any());
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
            sut.runSimulation();
            clearInvocations(simulationSpy);

            sut.pauseSimulation();
            verify(simulationSpy, only()).pause();
        }

        @Test
        public void whenCallingStopSimulationOnRunningSimulation_shouldStopSimulation() {
            sut.runSimulation();
            clearInvocations(simulationSpy);

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

        @Test
        public void whenCallingClearAll_shouldSetShapes() {
            sut.clearAll();
            verify(simulationSpy).setShapes(shapeList);
        }

        @Test
        public void whenAddingColorPlot_shouldAddColorPlotStyleToSimulation() {
            sut.togglePlotStyle(PlotStyle.Color);
            assertTrue(simulationSpy.isColorStyleAdded());
        }

        @Test
        public void whenAddingArrowPlot_shouldAddArrowPlotStyleToSimulation() {
            sut.togglePlotStyle(PlotStyle.Arrow);
            assertTrue(simulationSpy.isArrowStyleAdded());
        }

        @Test
        public void whenRemovingColorPlot_shouldRemoveColorPlotFromSimulation() {
            sut.togglePlotStyle(PlotStyle.Color);
            GridNodeStyle addedPlotStyle = simulationSpy.getAddedPlotStyle();

            sut.togglePlotStyle(PlotStyle.Color);
            verify(simulationSpy).removePlotStyle(addedPlotStyle);
            assertFalse(simulationSpy.isColorStyleAdded());
        }

        @Test
        public void whenRemovingPlotStyleThenAddingSimulation_shouldNotAddPlotStyleToNewSimulation() {
            sut.togglePlotStyle(PlotStyle.Color);
            sut.togglePlotStyle(PlotStyle.Color);
            simulationSpy.reset();

            sut.addSimulation();
            assertFalse(simulationSpy.isColorStyleAdded());
        }

        @Test
        public void whenAddingInfoDisplayPlot_shouldAddInfoDisplayPlotStyleToSimulation() {
            sut.togglePlotStyle(PlotStyle.Info);
            assertTrue(simulationSpy.isInfoDisplayStyleAdded());
        }

        @Test
        public void whenCallingRemoveOnRunningSimulation_shouldStopAndRemoveSimulation() {
            sut.runSimulation();
            clearInvocations(simulationSpy);

            sut.removeSimulation();
            verify(simulationSpy).pause();
            verify(graphicViewMock).update();
            verifyNoMoreInteractions(simulationSpy);
        }

        @Test
        public void whenCallingRunTwice_shouldOnlyStartSimulationOnce() {
            sut.runSimulation();
            sut.runSimulation();

            verify(simulationSpy).run();
        }

        @Test
        public void whenCallingRunAfterRunAndPause_shouldRunSimulationAgain() {
            sut.runSimulation();
            sut.pauseSimulation();
            clearInvocations(simulationSpy);

            sut.runSimulation();
            verify(simulationSpy).run();
        }

        @Test
        public void whenAddingPlotStyle_shouldUpdateGraphicView() {
            sut.togglePlotStyle(PlotStyle.Arrow);
            verify(graphicViewMock).update();
        }

        @Test
        public void whenAddingAndRunningNewSimulationAfterRunningThenRemovingLastSimulation_shouldRunNewSimulation() {
            sut.runSimulation();
            sut.removeSimulation();
            clearInvocations(simulationSpy);

            sut.addSimulation();
            sut.runSimulation();
            verify(simulationSpy).run();
        }

        @Test
        public void whenAddingNewSimulationOverRunningSimulation_shouldBeAbleToRunNewSimulation() {
            sut.runSimulation();
            clearInvocations(simulationSpy);

            sut.addSimulation();
            sut.runSimulation();
            verify(simulationSpy).run();
        }

        @Test
        public void whenRemovingPlotStyle_shouldUpdateGraphicView() {
            sut.togglePlotStyle(PlotStyle.Arrow);
            clearInvocations(graphicViewMock);

            sut.togglePlotStyle(PlotStyle.Arrow);
            verify(graphicViewMock, atLeastOnce()).update();
        }
    }

    public class IntegrationContext {
        @Before
        public void setUp() {
            SimulationFactory simulationFactory = new SimulationFactoryImpl();
            sut = new SimulationGraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList, transformer, simulationFactory);
            sut.setGraphicView(graphicViewMock);

            when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        }

        @Test
        public void whenCallingRemoveOnPausedSimulation_shouldRemoveSimulation() {
            sut.addSimulation();
            sut.pauseSimulation();
            clearInvocations(painterSpy);
            clearInvocations(graphicViewMock);

            sut.removeSimulation();

            verify(graphicViewMock).update();
            verifyZeroInteractions(painterSpy);
        }
    }
}
