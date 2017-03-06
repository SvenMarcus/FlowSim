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

import java.util.List;

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

    @Before
    public void canCreateLBMChannelFlowSimulation() {
        gridSpy = mock(UniformGrid.class);
        when(gridSpy.getOrigin()).thenReturn(makePoint(0, 0));
        when(gridSpy.getWidth()).thenReturn(4.);
        when(gridSpy.getHeight()).thenReturn(3.);
        when(gridSpy.getHorizontalNodes()).thenReturn(4);
        when(gridSpy.getVerticalNodes()).thenReturn(3);
        when(gridSpy.getVelocityAt(anyInt(), anyInt())).thenAnswer(invocationOnMock -> (double) ((int) invocationOnMock.getArgument(0) + (int) invocationOnMock.getArgument(1)));
        painterSpy = mock(Painter.class);
        transformer = mock(CoordinateTransformer.class);
        Color color = new Color(100, 150, 200);
        ColorFactory colorFactory = mock(ColorFactory.class);
        when(colorFactory.makeColorForValue(anyDouble(), anyDouble(), anyDouble())).thenAnswer(invocationOnMock -> {
            int r = ((Double) invocationOnMock.getArgument(0)).intValue();
            int g = ((Double) invocationOnMock.getArgument(1)).intValue();
            int b = ((Double) invocationOnMock.getArgument(2)).intValue();
            return new Color(r, g, b);
        });
        when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(transformer.scaleToScreenLength(anyDouble())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        sut = new LBMChannelFlowSimulation(gridSpy, colorFactory);
    }

    @Test
    public void whenCallingPaint_shouldPaintGridValues() {
        sut.paint(painterSpy, transformer);
        ArgumentCaptor<Color> colorCaptor = ArgumentCaptor.forClass(Color.class);
        verify(painterSpy, times(12)).setColor(colorCaptor.capture());
        List<Color> capturedValues = colorCaptor.getAllValues();
        assertColorEquals(capturedValues.get(0), 0, 5, 0);
        assertColorEquals(capturedValues.get(1), 0, 5, 1);
        assertColorEquals(capturedValues.get(2), 0, 5, 2);
        assertColorEquals(capturedValues.get(3), 0, 5, 3);
        assertColorEquals(capturedValues.get(4), 0, 5, 1);
        assertColorEquals(capturedValues.get(5), 0, 5, 2);
        assertColorEquals(capturedValues.get(6), 0, 5, 3);
        assertColorEquals(capturedValues.get(7), 0, 5, 4);
        assertColorEquals(capturedValues.get(8), 0, 5, 2);
        assertColorEquals(capturedValues.get(9), 0, 5, 3);
        assertColorEquals(capturedValues.get(10), 0, 5, 4);
        assertColorEquals(capturedValues.get(11), 0, 5, 5);


    }

    private void assertColorEquals(Color c, int r, int g, int b) {
        assertEquals(r, c.r);
        assertEquals(g, c.g);
        assertEquals(b, c.b);
    }


}
