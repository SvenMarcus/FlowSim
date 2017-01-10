package irmb.test.presentation.factory;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.factory.MouseStrategyFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.strategies.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.strategies.MouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

/**
 * Created by Sven on 10.01.2017.
 */
public class MouseStrategyFactoryImplTest {

    @Test
    public void whenCallingMakeBuildObjectMouseStrategyWithLine_shouldReturnBuildObjectMouseStrategy() {
        List<PaintableShape> shapeList = mock(List.class);
        CommandQueue commandQueue = mock(CommandQueue.class);
        GraphicView graphicView = mock(GraphicView.class);
        PaintableShapeBuilderFactory factory = mock(PaintableShapeBuilderFactory.class);
        MouseStrategyFactoryImpl sut = new MouseStrategyFactoryImpl(shapeList, commandQueue, graphicView, factory);
        MouseStrategy strategy = sut.makeStrategy("Line");

        assertThat(strategy, is(instanceOf(BuildObjectMouseStrategy.class)));
    }

}