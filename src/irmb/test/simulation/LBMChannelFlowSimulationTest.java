package irmb.test.simulation;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.simulation.LBMChannelFlowSimulation;
import irmb.flowsim.simulation.UniformGrid;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulationTest {

    private UniformGrid gridSpy;
    private LBMChannelFlowSimulation sut;
    private Painter painterSpy;
    private CoordinateTransformer transformer;
    private int horizontalNodes;
    private int verticalNodes;

    private double fMax;
    private double fMin;

    private double[][] gridValues;
    private SolverMock solverSpy;

    @Before
    public void canCreateLBMChannelFlowSimulation() {
        fMax = Double.MIN_VALUE;
        fMin = Double.MAX_VALUE;
        solverSpy = spy(new SolverMock());
        gridSpy = mock(UniformGrid.class);
        horizontalNodes = 4;
        verticalNodes = 3;
        initGridValues();
        setGridBehavior();
        painterSpy = mock(Painter.class);
        transformer = mock(CoordinateTransformer.class);
        ColorFactory colorFactory = mock(ColorFactory.class);
        setColorFactoryBehavior(colorFactory);
        when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> {
            Point argument = invocationOnMock.getArgument(0);
            Point p = makePoint(argument.getX() + 1, argument.getY() + 5);
            return p;
        });
        when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> (double) invocationOnMock.getArgument(0) * 7);
        sut = new LBMChannelFlowSimulation(gridSpy, solverSpy, colorFactory);
    }

    private void setColorFactoryBehavior(ColorFactory colorFactory) {
        when(colorFactory.makeColorForValue(anyDouble(), anyDouble(), anyDouble())).thenAnswer(invocationOnMock -> {
            int r = ((Double) invocationOnMock.getArgument(0)).intValue();
            int g = ((Double) invocationOnMock.getArgument(1)).intValue();
            int b = ((Double) invocationOnMock.getArgument(2)).intValue();
            return new Color(r, g, b);
        });
    }

    private void setGridBehavior() {
        when(gridSpy.getOrigin()).thenReturn(makePoint(0, 0));
        when(gridSpy.getWidth()).thenReturn(3.);
        when(gridSpy.getHeight()).thenReturn(2.);
        when(gridSpy.getHorizontalNodes()).thenReturn(horizontalNodes);
        when(gridSpy.getVerticalNodes()).thenReturn(verticalNodes);
        when(gridSpy.getDelta()).thenReturn(1.);
        when(gridSpy.getVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
            return gridValues[(int) invocationOnMock.getArgument(1)][(int) invocationOnMock.getArgument(0)];
        });
    }

    private void initGridValues() {
        Random rnd = new Random();
        gridValues = new double[verticalNodes][horizontalNodes];
        for (int i = 0; i < verticalNodes; i++)
            for (int j = 0; j < horizontalNodes; j++) {
                int value = rnd.nextInt(1000);
                if (value > fMax)
                    fMax = value;
                if (value < fMin)
                    fMin = value;
                gridValues[i][j] = value;
            }
    }

    @Test
    public void whenCallingPaint_shouldSetCorrectColors() {
        sut.paint(painterSpy, transformer);
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(painterSpy, times(verticalNodes * horizontalNodes)).setColor(colorCaptor.capture());

        assertCorrectGridColors(colorCaptor.getAllValues());
    }

    @Test
    public void whenPaintingSecondTimeAfterGridExtremesChanged_shouldSetCorrectColors() {
        sut.paint(painterSpy, transformer);
        clearInvocations(painterSpy);

        fMin = -50;
        fMax = 1001;
        gridValues[2][1] = fMin;
        gridValues[0][2] = fMax;

        sut.paint(painterSpy, transformer);
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(painterSpy, times(verticalNodes * horizontalNodes)).setColor(colorCaptor.capture());
        assertCorrectGridColors(colorCaptor.getAllValues());
    }


    private void assertCorrectGridColors(List<Color> capturedValues) {
        int index = 0;
        for (int i = 0; i < verticalNodes; i++)
            for (int j = 0; j < horizontalNodes; j++) {
                assertColorEquals(capturedValues.get(index), (int) fMin, (int) fMax, (int) gridValues[i][j]);
                index++;
            }
    }

    private void assertColorEquals(Color c, int r, int g, int b) {
        assertEquals(r, c.r);
        assertEquals(g, c.g);
        assertEquals(b, c.b);

    }

    @Test
    public void whenCallingPaint_shouldPaintFilledRectanglesWithCorrectColor() {
        sut.paint(painterSpy, transformer);
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        ArgumentCaptor<Double> doubleCaptor = ArgumentCaptor.forClass(Double.class);
        int minNumberOfInvocations = horizontalNodes * verticalNodes;
        verify(painterSpy, atLeast(minNumberOfInvocations)).setColor(colorCaptor.capture());
        verify(painterSpy, atLeast(minNumberOfInvocations)).fillRectangle(
                doubleCaptor.capture(),
                doubleCaptor.capture(),
                doubleCaptor.capture(),
                doubleCaptor.capture()
        );
        assertCorrectGridColors(colorCaptor.getAllValues());
        assertCorrectRectangleValues(doubleCaptor.getAllValues());
    }

    private void assertCorrectRectangleValues(List<Double> capturedDoubles) {
        int index = 0;
        for (int i = 0; i < verticalNodes; i++)
            for (int j = 0; j < horizontalNodes; j++) {
                assertEquals(1 + j * 7, Math.round(capturedDoubles.get(index++)));
                assertEquals(5 + i * 7, Math.round(capturedDoubles.get(index++)));
                assertEquals(7, Math.round(capturedDoubles.get(index++)));
                assertEquals(7, Math.round(capturedDoubles.get(index++)));
            }
    }

    @Test
    public void whenCallingRun_shouldStartSolver() {
        sut.run();
        verify(solverSpy).solve();
    }

    @Test
    public void whenCallingPause_shouldPauseSolver() {
        sut.pause();
        verify(solverSpy).pause();
    }

    @Test
    public void whenSolverNotifiesSimulation_shouldNotifyObservers() {
        Observer<String> observerSpy = mock(Observer.class);
        sut.addObserver(observerSpy);

        solverSpy.notifyObservers("");

        verify(observerSpy).update(any());
    }


    @Test
    public void whenCallingSetShapesWithLine_shouldMapToGrid() {
        Line line = new Line();
        line.setFirst(makePoint(0, 0));
        line.setSecond(makePoint(3, 2));
        PaintableLine paintableLine = new PaintableLine(line);
        List<PaintableShape> shapeList = new ArrayList<>();
        shapeList.add(paintableLine);

        sut.setShapes(shapeList);
        verify(gridSpy).resetSolidNodes();
        verify(gridSpy).setSolid(0, 0);
        verify(gridSpy).setSolid(1, 1);
        verify(gridSpy).setSolid(2, 1);
        verify(gridSpy).setSolid(3, 2);

        clearInvocations(gridSpy);

        line.setFirst(makePoint(1, 0));
        sut.setShapes(shapeList);
        verify(gridSpy).resetSolidNodes();
        verify(gridSpy).setSolid(1, 0);
        verify(gridSpy).setSolid(2, 1);
        verify(gridSpy).setSolid(3, 2);
    }

}
