package irmb.test.presentation.factory;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.factory.MouseStrategyFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.strategy.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.MoveMouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

/**
 * Created by Sven on 10.01.2017.
 */
public class MouseStrategyFactoryImplTest {

    private List<PaintableShape> shapeList;
    private CommandQueue commandQueue;
    private GraphicView graphicView;
    private PaintableShapeBuilderFactory factory;
    private MouseStrategyFactoryImpl sut;
    private MouseStrategy strategy;

    @Before
    public void setUp() throws Exception {
        shapeList = mock(List.class);
        commandQueue = mock(CommandQueue.class);
        graphicView = mock(GraphicView.class);
        factory = mock(PaintableShapeBuilderFactory.class);
        sut = new MouseStrategyFactoryImpl(shapeList, commandQueue, graphicView, factory, null);
    }

    @Test
    public void whenCallingMakeStrategyWithShapeNames_shouldReturnBuildObjectMouseStrategy() {
        strategy = sut.makeStrategy("Line");
        assertThat(strategy, is(instanceOf(BuildObjectMouseStrategy.class)));
        strategy = sut.makeStrategy("Rectangle");
        assertThat(strategy, is(instanceOf(BuildObjectMouseStrategy.class)));
        strategy = sut.makeStrategy("PolyLine");
        assertThat(strategy, is(instanceOf(BuildObjectMouseStrategy.class)));
        strategy = sut.makeStrategy("Bezier");
        assertThat(strategy, is(instanceOf(BuildObjectMouseStrategy.class)));
    }

    @Test
    public void whenCallingMakeStrategyWithMove_shouldReturnMoveMouseStrategy() {
        strategy = sut.makeStrategy("Move");
        assertThat(strategy, is(instanceOf(MoveMouseStrategy.class)));
    }

    @Test
    public void whenCallingWithInvalidData_shouldReturnMoveMouseStrategy() {
        strategy = sut.makeStrategy("Invalid");
        assertThat(strategy, is(instanceOf(MoveMouseStrategy.class)));
    }
}