package irmb.test.simulation.jflowsim.adapters;

import irmb.flowsim.simulation.jflowsim.adapters.JFlowSimNavierStokesGridAdapter;
import numerics.lbm.navierstokes.LBMNavierStokesGrid;
import org.junit.Test;

import static irmb.test.util.TestUtil.makePoint;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by sven on 15.03.17.
 */
public class JFlowSimNavierStokesGridAdapterTest {

    @Test
    public void whenCallingIsPointInsideWithPointWithinGridBounds_shouldReturnTrue() {
        LBMNavierStokesGrid gridMock = createGridMock();
        when(gridMock.getMinX()).thenReturn(0.);
        when(gridMock.getMaxX()).thenReturn(0.5);
        when(gridMock.getMinY()).thenReturn(0.);
        when(gridMock.getMaxY()).thenReturn(0.2);
        when(gridMock.getWidth()).thenReturn(0.2);
        when(gridMock.getLength()).thenReturn(0.5);
        
        JFlowSimNavierStokesGridAdapter sut = new JFlowSimNavierStokesGridAdapter(gridMock);

        boolean result = sut.isPointInside(makePoint(0, 0));
        assertTrue(result);

        result = sut.isPointInside(makePoint(0, 0));
        assertTrue(result);
    }

    @Test
    public void whenCallingIsPointInsideWithPointOutsireGridBounds_shouldReturnFalse() {
        LBMNavierStokesGrid gridMock = createGridMock();
        JFlowSimNavierStokesGridAdapter sut = new JFlowSimNavierStokesGridAdapter(gridMock);

        boolean result = sut.isPointInside(makePoint(-4, 0));
        assertFalse(result);

        result = sut.isPointInside(makePoint(4.1, 0));
        assertFalse(result);

        result = sut.isPointInside(makePoint(0, -3.1));
        assertFalse(result);

        result = sut.isPointInside(makePoint(0, 2.1));
        assertFalse(result);
    }

    private LBMNavierStokesGrid createGridMock() {
        LBMNavierStokesGrid gridMock = mock(LBMNavierStokesGrid.class);
        when(gridMock.getMinX()).thenReturn(-3.);
        when(gridMock.getMaxX()).thenReturn(4.);
        when(gridMock.getMinY()).thenReturn(-1.);
        when(gridMock.getMaxY()).thenReturn(2.);
        return gridMock;
    }

}