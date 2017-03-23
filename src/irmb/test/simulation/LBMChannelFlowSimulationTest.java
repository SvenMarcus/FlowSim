package irmb.test.simulation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.*;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.simulation.LBMChannelFlowSimulation;
import irmb.flowsim.simulation.UniformGrid;
import irmb.flowsim.util.Observer;
import irmb.flowsim.view.graphics.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static irmb.test.util.TestUtil.doubleOf;
import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.*;

/**
 * Created by sven on 04.03.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class LBMChannelFlowSimulationTest {

    private final int screenLengthScale = 7;
    private int screenXOffset = 1;
    private int screenYOffset = 5;
    private UniformGrid gridSpy;
    private LBMChannelFlowSimulation sut;
    private Painter painterSpy;
    private CoordinateTransformer transformer;
    private int horizontalNodes;
    private int verticalNodes;

    private double fMax;
    private double fMin;

    private double[][] gridValues;
    private double[][] gridVxValues;
    private double[][] gridVyValues;
    private SolverMock solverSpy;
    private ColorFactory colorFactory;

    @Before
    public void setUp() {
        fMax = Double.MIN_VALUE;
        fMin = Double.MAX_VALUE;
        solverSpy = spy(new SolverMock());
        painterSpy = mock(Painter.class);
        transformer = mock(CoordinateTransformer.class);
        colorFactory = mock(ColorFactory.class);
        setColorFactoryBehavior(colorFactory);
        when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> {
            Point argument = invocationOnMock.getArgument(0);
            Point p = makePoint(argument.getX() + screenXOffset, argument.getY() + screenYOffset);
            return p;
        });
        when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> (double) invocationOnMock.getArgument(0) * screenLengthScale);
    }

    private void setColorFactoryBehavior(ColorFactory colorFactory) {
        when(colorFactory.makeColorForValue(anyDouble(), anyDouble(), anyDouble())).thenAnswer(invocationOnMock -> {
            int r = ((Double) invocationOnMock.getArgument(0)).intValue();
            int g = ((Double) invocationOnMock.getArgument(1)).intValue();
            int b = ((Double) invocationOnMock.getArgument(2)).intValue();
            return new Color(r, g, b);
        });
    }

    private void setGridBehavior(Point origin, double gridDelta) {
        initGridValues();
        when(gridSpy.getTopLeft()).thenReturn(origin);
        double width = (horizontalNodes - 1) * gridDelta;
        when(gridSpy.getWidth()).thenReturn(width);
        double height = (verticalNodes - 1) * gridDelta;
        when(gridSpy.getHeight()).thenReturn(height);
        when(gridSpy.getHorizontalNodes()).thenReturn(horizontalNodes);
        when(gridSpy.getVerticalNodes()).thenReturn(verticalNodes);
        when(gridSpy.getDelta()).thenReturn(gridDelta);
        when(gridSpy.getVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
            return gridValues[(int) invocationOnMock.getArgument(1)][(int) invocationOnMock.getArgument(0)];
        });
        when(gridSpy.getHorizontalVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
            return gridVxValues[(int) invocationOnMock.getArgument(1)][(int) invocationOnMock.getArgument(0)];
        });
        when(gridSpy.getVerticalVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
            return gridVyValues[(int) invocationOnMock.getArgument(1)][(int) invocationOnMock.getArgument(0)];
        });

        when(gridSpy.isPointInside(any(Point.class))).thenAnswer(invocationOnMock -> {
            Point point = invocationOnMock.getArgument(0);
            return !(point.getX() < origin.getX() || point.getX() > origin.getX() + width || point.getY() < origin.getY() - height || point.getY() > origin.getY());
        });
    }

    private void initGridValues() {
        Random rnd = new Random();
        gridValues = new double[verticalNodes][horizontalNodes];
        gridVxValues = new double[verticalNodes][horizontalNodes];
        gridVyValues = new double[verticalNodes][horizontalNodes];
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

    public class ArrowPlotStyleContext {
        @Before
        public void setUp() {
            gridSpy = mock(UniformGrid.class);
            horizontalNodes = 2;
            verticalNodes = 1;
            screenXOffset = 0;
            screenYOffset = 0;
            setGridBehavior(makePoint(0, 0), 1.);
            sut = new LBMChannelFlowSimulation(gridSpy, solverSpy, colorFactory);
        }

        @Test
        public void whenMinEqualsMax_shouldNotPaint() {
            gridValues[0][0] = 1;
            gridValues[0][1] = 1;
            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);
            verifyZeroInteractions(painterSpy);
        }

        @Test
        public void whenAddingArrowStyleThenPainting_shouldPaintVerticalArrow() {
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeVerticalArrowSetup();
            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);
            verify(painterSpy).paintLine(0.0d, -0.25d, 0.0d, 0.25d);
            verify(painterSpy).paintLine(0.0, 0.25, -0.125, 0.125);
            verify(painterSpy).paintLine(0.0, 0.25, 0.125, 0.125);
        }

        private void makeVerticalArrowSetup() {
            gridValues[0][0] = 1;
            gridValues[0][1] = 5;
            gridVxValues[0][0] = 0;
            gridVyValues[0][0] = 1;
            gridVxValues[0][1] = 3;
            gridVyValues[0][1] = 4;
        }

        @Test
        public void whenAddingArrowStyleThenPainting_shouldPaintHorizontalArrow() {
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeHorizontalArrowSetup();
            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);
            verify(painterSpy).paintLine(-0.25, 0.0, 0.25, 0.0);
            verify(painterSpy).paintLine(0.25, 0.0, 0.125, 0.125);
            verify(painterSpy).paintLine(0.25, 0.0, 0.125, -0.125);
        }

        private void makeHorizontalArrowSetup() {
            gridValues[0][0] = 1;
            gridValues[0][1] = 5;
            gridVxValues[0][0] = 1;
            gridVyValues[0][0] = 0;
            gridVxValues[0][1] = 3;
            gridVyValues[0][1] = 4;
        }

        @Test
        public void whenAddingArrowStyleThenPainting_shouldPaintBothCells() {
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeHorizontalArrowSetup();
            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);
            verify(painterSpy).paintLine(-0.25, 0.0, 0.25, 0.0);
            verify(painterSpy).paintLine(0.25, 0.0, 0.125, 0.125);
            verify(painterSpy).paintLine(0.25, 0.0, 0.125, -0.125);

            verify(painterSpy).paintLine(0.25, -1.0, 1.75, 1.0);
            verify(painterSpy).paintLine(1.75, 1.0, 0.875, 0.875);
            verify(painterSpy).paintLine(1.75, 1.0, 1.875, 0.125);
        }

        @Test
        public void whenPaintingWithDifferentMinMaxValues_shoudPaintBothCells() {
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            gridValues[0][0] = 1;
            gridValues[0][1] = Math.sqrt(6 * 6 + 5 * 5);
            gridVxValues[0][0] = 1;
            gridVyValues[0][0] = 0;
            gridVxValues[0][1] = 6;
            gridVyValues[0][1] = 5;

            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);

            verify(painterSpy).paintLine(doubleOf(-0.1468), doubleOf(0.0), doubleOf(0.1468), doubleOf(0.0));
            verify(painterSpy).paintLine(doubleOf(0.1468), doubleOf(0.0), doubleOf(0.0734), doubleOf(0.0734));
            verify(painterSpy).paintLine(doubleOf(0.1468), doubleOf(0.0), doubleOf(0.0734), doubleOf(-0.0734));

            verify(painterSpy).paintLine(doubleOf(0.1189), doubleOf(-0.7341), doubleOf(1.881), doubleOf(0.7341));
            verify(painterSpy).paintLine(doubleOf(1.881), doubleOf(0.7341), doubleOf(1.8076), doubleOf(-0.0734));
            verify(painterSpy).paintLine(doubleOf(1.881), doubleOf(0.7341), doubleOf(1.0734), doubleOf(0.8076));
        }

        @Test
        public void whenPaintingWithScreenOffset_shouldPaintBothCells() {
            screenXOffset = 5;
            screenYOffset = 3;

            setGridBehavior(makePoint(4, 7), 1);
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeVerticalArrowSetup();

            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);

            verify(painterSpy).paintLine(9.0, 9.75, 9.0, 10.25);
            verify(painterSpy).paintLine(9.0, 10.25, 8.875, 10.125);
            verify(painterSpy).paintLine(9.0, 10.25, 9.125, 10.125);

            verify(painterSpy).paintLine(9.25, 9.0, 10.75, 11.0);
            verify(painterSpy).paintLine(10.75, 11.0, 9.875, 10.875);
            verify(painterSpy).paintLine(10.75, 11.0, 10.875, 10.125);
        }

        @Test
        public void whenPaintingWithOffset_shouldOnlyPaintTwoCellsDivisibleByOffset() {
            horizontalNodes = 3;
            setGridBehavior(makePoint(0, 0), 1);
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeVerticalArrowSetup();
            gridValues[0][2] = Math.sqrt(5 * 5 + 4 * 4);
            gridVxValues[0][2] = 5;
            gridVyValues[0][2] = 4;

            sut.addPlotStyle("Arrow", 2);
            sut.paint(painterSpy, transformer);

            verify(painterSpy).paintLine(doubleOf(0.0), doubleOf(-0.1850), doubleOf(0.0), doubleOf(0.1850));
            verify(painterSpy).paintLine(doubleOf(0.0), doubleOf(0.1850), doubleOf(0.0925), doubleOf(0.0925));
            verify(painterSpy).paintLine(doubleOf(0.0), doubleOf(0.1850), doubleOf(-0.0925), doubleOf(0.0925));

            verify(painterSpy).paintLine(doubleOf(1.0746), doubleOf(-0.7403), doubleOf(2.9253), doubleOf(0.7403));
            verify(painterSpy).paintLine(doubleOf(2.9253), doubleOf(0.7403), doubleOf(2.8328), doubleOf(-0.0925));
            verify(painterSpy).paintLine(doubleOf(2.9253), doubleOf(0.7403), doubleOf(2.0925), doubleOf(0.8328));
        }

        @Test
        public void whenPaintingWithScreenScale_shouldPaintBothCells() {
            setGridBehavior(makePoint(0, 0), 1);
            makeVerticalArrowSetup();

            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);

            verify(painterSpy).paintLine(0.0, -1.75, 0.0, 1.75);
            verify(painterSpy).paintLine(0.0, 1.75, 0.875, 0.875);
            verify(painterSpy).paintLine(0.0, 1.75, -0.875, 0.875);

            verify(painterSpy).paintLine(1.75, -7.0, 12.25, 7.0);
            verify(painterSpy).paintLine(12.25, 7.0, 13.125, 0.875);
            verify(painterSpy).paintLine(12.25, 7.0, 6.125, 6.125);
        }

        @Test
        public void whenPaintingThreeTimesAndGridChanged_shouldUseMinMaxFromPreviousTimeStep() {
            setGridBehavior(makePoint(0, 0), 1);
            when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
            makeVerticalArrowSetup();

            sut.addPlotStyle("Arrow", 1);
            sut.paint(painterSpy, transformer);
            clearInvocations(painterSpy);

            gridValues[0][0] = Math.sqrt(5 * 5 + 6 * 6);
            gridVxValues[0][0] = 5;
            gridVyValues[0][0] = 6;

            sut.paint(painterSpy, transformer);
            clearInvocations(painterSpy);

            sut.paint(painterSpy, transformer);
            verify(painterSpy).paintLine(-1.7792013438759244, -2.135041612651109, 1.7792013438759244, 2.135041612651109);
            verify(painterSpy).paintLine(1.7792013438759244, 2.135041612651109, -0.17792013438759235, 1.957121478263517);
            verify(painterSpy).paintLine(1.7792013438759244, 2.135041612651109, 1.9571214782635167, 0.17792013438759224);

            verify(painterSpy).paintLine(-0.06752080632555457, -1.4233610751007395, 2.067520806325555, 1.4233610751007395);
            verify(painterSpy).paintLine(2.067520806325555, 1.4233610751007395, 0.8220798656124075, 1.2454409407131473);
            verify(painterSpy).paintLine(2.067520806325555, 1.4233610751007395, 2.2454409407131473, 0.17792013438759235);
        }

    }

    public class BasicGridContext {
        @Before
        public void setUp() {
            gridSpy = mock(UniformGrid.class);
            horizontalNodes = 4;
            verticalNodes = 3;
            setGridBehavior(makePoint(0, 2), 1.);

            sut = new LBMChannelFlowSimulation(gridSpy, solverSpy, colorFactory);
        }

        @Test
        public void whenCallingPaint_shouldSetCorrectColors() {
            sut.paint(painterSpy, transformer);
            ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
            verify(painterSpy, times(verticalNodes * horizontalNodes)).setColor(colorCaptor.capture());

            assertCorrectGridColors(colorCaptor.getAllValues());
        }

        @Test
        public void whenPaintingThirdTime_shouldUseColorsBaseOnMinMaxFromSecondPaint() {
            sut.paint(painterSpy, transformer);
            clearInvocations(painterSpy);

            gridValues[2][1] = -50;
            gridValues[0][2] = 1001;

            sut.paint(painterSpy, transformer);
            clearInvocations(painterSpy);

            fMin = -50;
            fMax = 1001;
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
        public void whenCallingSetShapesWithEmptyList_shouldOnlyResetGrid() {
            List<PaintableShape> shapeList = new ArrayList<>();
            sut.setShapes(shapeList);
            verify(gridSpy).resetSolidNodes();
            verifyNoMoreInteractions(gridSpy);
        }


        @Test
        public void whenCallingSetShapesWithLine_shouldMapToGrid() {
            List<PaintableShape> shapeList = getPaintableShapeListWithLine(makePoint(0, 0), makePoint(3, 2));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(0, 2);
            verify(gridSpy).setSolid(1, 1);
            verify(gridSpy).setSolid(2, 1);
            verify(gridSpy).setSolid(3, 0);

            clearInvocations(gridSpy);

            shapeList = getPaintableShapeListWithLine(makePoint(1, 0), makePoint(3, 2));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(1, 2);
            verify(gridSpy).setSolid(2, 1);
            verify(gridSpy).setSolid(3, 0);
        }

        @Test
        public void whenPaintingSolidNodes_shouldSetColorToBlack() {
            when(gridSpy.isSolid(1, 2)).thenReturn(true);
            when(gridSpy.isSolid(2, 1)).thenReturn(true);
            when(gridSpy.isSolid(3, 0)).thenReturn(true);

            sut.paint(painterSpy, transformer);

            ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);

            InOrder inOrder = inOrder(painterSpy);
            double width = gridSpy.getDelta() * screenLengthScale;
            inOrder.verify(painterSpy).fillRectangle((0 * width + screenXOffset), (0 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((1 * width + screenXOffset), (0 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((2 * width + screenXOffset), (0 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).setColor(colorCaptor.capture());
            inOrder.verify(painterSpy).fillRectangle((3 * width + screenXOffset), (0 * width + screenYOffset), width, width);

            inOrder.verify(painterSpy).fillRectangle((0 * width + screenXOffset), (1 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((1 * width + screenXOffset), (1 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).setColor(colorCaptor.capture());
            inOrder.verify(painterSpy).fillRectangle((2 * width + screenXOffset), (1 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((3 * width + screenXOffset), (1 * width + screenYOffset), width, width);

            inOrder.verify(painterSpy).fillRectangle((0 * width + screenXOffset), (2 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).setColor(colorCaptor.capture());
            inOrder.verify(painterSpy).fillRectangle((1 * width + screenXOffset), (2 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((2 * width + screenXOffset), (2 * width + screenYOffset), width, width);
            inOrder.verify(painterSpy).fillRectangle((3 * width + screenXOffset), (2 * width + screenYOffset), width, width);


            List<Color> capturedColors = colorCaptor.getAllValues();
            assertColorEquals(capturedColors.get(0), 0, 0, 0);
            assertColorEquals(capturedColors.get(1), 0, 0, 0);
            assertColorEquals(capturedColors.get(2), 0, 0, 0);
        }

    }

    public class GridWithOffsetContext {

        @Before
        public void setUp() {
            gridSpy = mock(UniformGrid.class);
            horizontalNodes = 5;
            verticalNodes = 8;
            setGridBehavior(makePoint(-1, 10), 1.);

            sut = new LBMChannelFlowSimulation(gridSpy, solverSpy, colorFactory);
        }


        @Test
        public void whenCallingSetShapesWithLineOnAltSetup_shouldMapToGrid() {

            List<PaintableShape> shapeList = getPaintableShapeListWithLine(makePoint(0, 3), makePoint(3, 5));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(1, 7);
            verify(gridSpy).setSolid(2, 6);
            verify(gridSpy).setSolid(3, 6);
            verify(gridSpy).setSolid(4, 5);
        }
    }

    public class RealisticGridContext {

        @Before
        public void setUp() {
            gridSpy = mock(UniformGrid.class);
            horizontalNodes = 11;
            verticalNodes = 16;
            setGridBehavior(makePoint(-1, 10), .5);

            sut = new LBMChannelFlowSimulation(gridSpy, solverSpy, colorFactory);
        }

        @Test
        public void whenCallingSetShapesWithLineOnGridWithNonIntDelta_shouldMapToGrid() {
            List<PaintableShape> shapeList = getPaintableShapeListWithLine(makePoint(0, 3), makePoint(3, 5));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(2, 14);
            verify(gridSpy).setSolid(3, 13);
            verify(gridSpy).setSolid(4, 13);
            verify(gridSpy).setSolid(5, 12);
            verify(gridSpy).setSolid(6, 11);
            verify(gridSpy).setSolid(7, 11);
            verify(gridSpy).setSolid(8, 10);
        }

        @Test
        public void whenCallingSetShapesWithLineOutsideGridBounds_shouldNotMapToGrid() {
            List<PaintableShape> shapeList = getPaintableShapeListWithLine(makePoint(-1.5, 4), makePoint(3, 5));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            shapeList = getPaintableShapeListWithLine(makePoint(4.1, 5), makePoint(0, 3));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            shapeList = getPaintableShapeListWithLine(makePoint(2, 2), makePoint(0, 3));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            shapeList = getPaintableShapeListWithLine(makePoint(2, 4), makePoint(0, 11));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());
        }

        @Test
        public void whenCallingSetShapesWithRectangle_shouldMapToGrid() {
            Rectangle rectangle = new Rectangle();
            rectangle.setFirst(makePoint(0, 3));
            rectangle.setSecond(makePoint(3, 5));
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(new PaintableRectangle(rectangle));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, atLeastOnce()).setSolid(2, 14);
            verify(gridSpy).setSolid(3, 14);
            verify(gridSpy).setSolid(4, 14);
            verify(gridSpy).setSolid(5, 14);
            verify(gridSpy).setSolid(6, 14);
            verify(gridSpy).setSolid(7, 14);
            verify(gridSpy, atLeastOnce()).setSolid(8, 14);

            verify(gridSpy, atLeastOnce()).setSolid(2, 10);
            verify(gridSpy).setSolid(3, 10);
            verify(gridSpy).setSolid(4, 10);
            verify(gridSpy).setSolid(5, 10);
            verify(gridSpy).setSolid(6, 10);
            verify(gridSpy).setSolid(7, 10);
            verify(gridSpy, atLeastOnce()).setSolid(8, 10);

            verify(gridSpy, atLeastOnce()).setSolid(2, 14);
            verify(gridSpy).setSolid(2, 13);
            verify(gridSpy).setSolid(2, 12);
            verify(gridSpy).setSolid(2, 11);
            verify(gridSpy, atLeastOnce()).setSolid(2, 10);

            verify(gridSpy, atLeastOnce()).setSolid(8, 14);
            verify(gridSpy).setSolid(8, 13);
            verify(gridSpy).setSolid(8, 12);
            verify(gridSpy).setSolid(8, 11);
            verify(gridSpy, atLeastOnce()).setSolid(8, 10);

        }

        @Test
        public void whenCallingSetShapesWithRectangleOutsideGridBounds_shouldNotMapToGrid() {
            Rectangle rectangle = new Rectangle();
            rectangle.setFirst(makePoint(-4, 1));
            rectangle.setSecond(makePoint(3, 5));
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(new PaintableRectangle(rectangle));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            rectangle.setFirst(makePoint(1, 4));
            rectangle.setSecond(makePoint(9, 11));
            shapeList = new ArrayList<>();
            shapeList.add(new PaintableRectangle(rectangle));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());
        }

        @Test
        public void whenCallingSetShapesWithPolyLine_shouldMapToGrid() {
            PolyLine polyLine = new PolyLine();
            polyLine.addPoint(makePoint(0, 3));
            polyLine.addPoint(makePoint(3, 5));
            polyLine.addPoint(makePoint(3, 10));
            polyLine.addPoint(makePoint(-1, 10));
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(new PaintablePolyLine(polyLine));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(2, 14);
            verify(gridSpy).setSolid(3, 13);
            verify(gridSpy).setSolid(4, 13);
            verify(gridSpy).setSolid(5, 12);
            verify(gridSpy).setSolid(6, 11);
            verify(gridSpy).setSolid(7, 11);
            verify(gridSpy, atLeastOnce()).setSolid(8, 10);

            verify(gridSpy).setSolid(8, 9);
            verify(gridSpy).setSolid(8, 8);
            verify(gridSpy).setSolid(8, 7);
            verify(gridSpy).setSolid(8, 6);
            verify(gridSpy).setSolid(8, 5);
            verify(gridSpy).setSolid(8, 4);
            verify(gridSpy).setSolid(8, 3);
            verify(gridSpy).setSolid(8, 2);
            verify(gridSpy).setSolid(8, 1);
            verify(gridSpy, atLeastOnce()).setSolid(8, 0);

            verify(gridSpy).setSolid(7, 0);
            verify(gridSpy).setSolid(6, 0);
            verify(gridSpy).setSolid(5, 0);
            verify(gridSpy).setSolid(4, 0);
            verify(gridSpy).setSolid(3, 0);
            verify(gridSpy).setSolid(2, 0);
            verify(gridSpy).setSolid(1, 0);
            verify(gridSpy).setSolid(0, 0);
        }

        @Test
        public void whenCallingSetShapesWithPolyLineOutsideGridBounds_shouldNotMapToGrid() {
            PolyLine polyLine = new PolyLine();
            polyLine.addPoint(makePoint(0, -1));
            polyLine.addPoint(makePoint(3, 5));
            polyLine.addPoint(makePoint(3, 10));
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(new PaintablePolyLine(polyLine));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            polyLine = new PolyLine();
            polyLine.addPoint(makePoint(0, 3));
            polyLine.addPoint(makePoint(5, 5));
            polyLine.addPoint(makePoint(3, 10));
            shapeList = new ArrayList<>();
            shapeList.add(new PaintablePolyLine(polyLine));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            polyLine = new PolyLine();
            polyLine.addPoint(makePoint(0, 3));
            polyLine.addPoint(makePoint(3, 5));
            polyLine.addPoint(makePoint(3, 11));
            shapeList = new ArrayList<>();
            shapeList.add(new PaintablePolyLine(polyLine));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());
        }

        @Test
        public void whenCallingSetLinesWithMultipleShapes_shouldMapAllToGrid() {
            List<PaintableShape> shapeList = getPaintableShapeListWithLine(makePoint(-1, 7), makePoint(1, 8));
            Rectangle rectangle = new Rectangle();
            rectangle.setFirst(makePoint(0, 3));
            rectangle.setSecond(makePoint(3, 5));
            shapeList.add(new PaintableRectangle(rectangle));

            sut.setShapes(shapeList);

            verify(gridSpy).resetSolidNodes();
            verify(gridSpy).setSolid(0, 6);
            verify(gridSpy).setSolid(1, 6);
            verify(gridSpy).setSolid(2, 5);
            verify(gridSpy).setSolid(3, 5);
            verify(gridSpy).setSolid(4, 4);

            verify(gridSpy, atLeastOnce()).setSolid(2, 14);
            verify(gridSpy).setSolid(3, 14);
            verify(gridSpy).setSolid(4, 14);
            verify(gridSpy).setSolid(5, 14);
            verify(gridSpy).setSolid(6, 14);
            verify(gridSpy).setSolid(7, 14);
            verify(gridSpy, atLeastOnce()).setSolid(8, 14);

            verify(gridSpy, atLeastOnce()).setSolid(2, 10);
            verify(gridSpy).setSolid(3, 10);
            verify(gridSpy).setSolid(4, 10);
            verify(gridSpy).setSolid(5, 10);
            verify(gridSpy).setSolid(6, 10);
            verify(gridSpy).setSolid(7, 10);
            verify(gridSpy, atLeastOnce()).setSolid(8, 10);

            verify(gridSpy, atLeastOnce()).setSolid(2, 14);
            verify(gridSpy).setSolid(2, 13);
            verify(gridSpy).setSolid(2, 12);
            verify(gridSpy).setSolid(2, 11);
            verify(gridSpy, atLeastOnce()).setSolid(2, 10);

            verify(gridSpy, atLeastOnce()).setSolid(8, 14);
            verify(gridSpy).setSolid(8, 13);
            verify(gridSpy).setSolid(8, 12);
            verify(gridSpy).setSolid(8, 11);
            verify(gridSpy, atLeastOnce()).setSolid(8, 10);

        }

        @Test
        public void whenCallingSetShapesWithBezierCurve_shouldMapToGrid() {
            BezierCurve bezierCurve = new BezierCurve();
            bezierCurve.addPoint(makePoint(-1, 3));
            bezierCurve.addPoint(makePoint(2, 5));
            bezierCurve.addPoint(makePoint(4, 7));
            bezierCurve.addPoint(makePoint(3, 9));
            PaintableBezierCurve paintableBezierCurve = new PaintableBezierCurve(bezierCurve);
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(paintableBezierCurve);

            sut.setShapes(shapeList);

            verify(gridSpy, atLeastOnce()).setSolid(0, 14);
            verify(gridSpy, atLeastOnce()).setSolid(0, 13);
            verify(gridSpy, atLeastOnce()).setSolid(1, 13);
            verify(gridSpy, atLeastOnce()).setSolid(1, 12);
            verify(gridSpy, atLeastOnce()).setSolid(2, 12);
            verify(gridSpy, atLeastOnce()).setSolid(2, 11);
            verify(gridSpy, atLeastOnce()).setSolid(3, 11);
            verify(gridSpy, atLeastOnce()).setSolid(4, 11);
            verify(gridSpy, atLeastOnce()).setSolid(4, 10);
            verify(gridSpy, atLeastOnce()).setSolid(5, 10);
            verify(gridSpy, atLeastOnce()).setSolid(5, 9);
            verify(gridSpy, atLeastOnce()).setSolid(6, 9);
            verify(gridSpy, atLeastOnce()).setSolid(6, 8);
            verify(gridSpy, atLeastOnce()).setSolid(7, 8);
            verify(gridSpy, atLeastOnce()).setSolid(7, 7);
            verify(gridSpy, atLeastOnce()).setSolid(7, 6);
            verify(gridSpy, atLeastOnce()).setSolid(8, 6);
            verify(gridSpy, atLeastOnce()).setSolid(8, 5);
            verify(gridSpy, atLeastOnce()).setSolid(8, 4);
            verify(gridSpy, atLeastOnce()).setSolid(8, 3);
            verify(gridSpy, atLeastOnce()).setSolid(8, 2);
        }

        @Test
        public void whenCallingSetShapesWithBezierCurveOutsideGridBounds_shouldNotMapToGrid() {
            BezierCurve bezierCurve = new BezierCurve();
            Point first = makePoint(-2, 3);
            bezierCurve.addPoint(first);
            Point second = makePoint(2, 5);
            bezierCurve.addPoint(second);
            Point third = makePoint(4, 7);
            bezierCurve.addPoint(third);
            PaintableBezierCurve paintableBezierCurve = new PaintableBezierCurve(bezierCurve);
            List<PaintableShape> shapeList = new ArrayList<>();
            shapeList.add(paintableBezierCurve);

            sut.setShapes(shapeList);
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            first.setX(-1);
            second.setY(2);

            sut.setShapes(shapeList);
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());

            clearInvocations(gridSpy);

            second.setY(3);
            third.setX(4);
            third.setY(10.1);

            sut.setShapes(shapeList);
            verify(gridSpy, never()).setSolid(anyInt(), anyInt());
        }
    }

    private List<PaintableShape> getPaintableShapeListWithLine(Point first, Point second) {
        Line line = new Line();
        line.setFirst(first);
        line.setSecond(second);
        PaintableLine paintableLine = new PaintableLine(line);
        List<PaintableShape> shapeList = new ArrayList<>();
        shapeList.add(paintableLine);
        return shapeList;
    }


}


