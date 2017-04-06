package test.irmb.flowsim.simulation.visualization;

import irmb.flowsim.simulation.visualization.*;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * Created by sven on 24.03.17.
 */
public class GridNodeStyleFactoryTest {

    private GridNodeStyleFactory sut;

    @Before
    public void setUp() throws Exception {
        sut = new GridNodeStyleFactory();
    }

    @Test
    public void testMakeColorPlot() {
        assertThat(sut.makeGridNodeStyle(PlotStyle.Color), is(instanceOf(ColorGridNodeStyle.class)));
    }

    @Test
    public void testMakeArrowPlot() {
        GridNodeStyle gridNodeStyle = sut.makeGridNodeStyle(PlotStyle.Arrow);
        assertThat(gridNodeStyle, is(instanceOf(ArrowGridNodeStyle.class)));
        assertEquals(new ArrowGridNodeStyle(5), gridNodeStyle);
    }
}