package irmb.test.simulation;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ColorFactory;
import irmb.flowsim.simulation.LBMChannelFlowSimulation;
import irmb.flowsim.simulation.UniformGrid;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

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

    @Before
    public void canCreateLBMChannelFlowSimulation() {
        fMax = Double.MIN_VALUE;
        fMin = Double.MAX_VALUE;

        gridSpy = mock(UniformGrid.class);
        horizontalNodes = 4;
        verticalNodes = 3;
        initGridValues();
        setGridBehavior();
        painterSpy = mock(Painter.class);
        transformer = mock(CoordinateTransformer.class);
        ColorFactory colorFactory = mock(ColorFactory.class);
        setColorFactoryBehavior(colorFactory);
        when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        sut = new LBMChannelFlowSimulation(gridSpy, colorFactory);
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
        when(gridSpy.getWidth()).thenReturn(4.);
        when(gridSpy.getHeight()).thenReturn(3.);
        when(gridSpy.getHorizontalNodes()).thenReturn(horizontalNodes);
        when(gridSpy.getVerticalNodes()).thenReturn(verticalNodes);
        when(gridSpy.getDeltaX()).thenReturn(1.);
        when(gridSpy.getDeltaY()).thenReturn(1.);
        when(gridSpy.getVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> {
            return gridValues[(int) invocationOnMock.getArgument(0)][(int) invocationOnMock.getArgument(1)];
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
        verify(painterSpy, atLeast(12)).setColor(colorCaptor.capture());
        verify(painterSpy, atLeast(12)).fillRectangle(
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
                assertEquals(0 + i, Math.round(capturedDoubles.get(index++)));
                assertEquals(0 + j, Math.round(capturedDoubles.get(index++)));
                assertEquals(1, Math.round(capturedDoubles.get(index++)));
                assertEquals(1, Math.round(capturedDoubles.get(index++)));
            }
    }

}
