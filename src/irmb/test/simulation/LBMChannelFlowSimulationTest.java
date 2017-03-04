package irmb.test.simulation;

import irmb.flowsim.simulation.LBMChannelFlowSimulation;
import irmb.flowsim.simulation.UniformGrid;
import org.junit.Test;

import static org.mockito.Mockito.mock;

/**
 * Created by sven on 04.03.17.
 */
public class LBMChannelFlowSimulationTest {

    @Test
    public void canCreateLBMChannelFlowSimulation() {
        UniformGrid gridSpy = mock(UniformGrid.class);
        LBMChannelFlowSimulation sut = new LBMChannelFlowSimulation();
    }

}
