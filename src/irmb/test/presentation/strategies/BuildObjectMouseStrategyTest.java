package irmb.test.presentation.strategies;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.presentation.strategy.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 09.01.2017.
 */
public class BuildObjectMouseStrategyTest {

    private CommandQueue commandQueue;
    private ShapeFactory shapeFactory;
    private PaintableShapeBuilderFactory factory;
    private GraphicView graphicView;
    private List<PaintableShape> shapeList;
    private Observer observer;
    private BuildObjectMouseStrategy sut;

    @Before
    public void setUp() throws Exception {
        commandQueue = mock(CommandQueue.class);
        shapeFactory = spy(new ShapeFactoryImpl());
        factory = spy(new PaintableShapeBuilderFactoryImpl(shapeFactory));
        graphicView = mock(GraphicView.class);
        shapeList = new LinkedList<>();
        observer = mock(Observer.class);


    }

    private void makeBuildObjectMouseStrategyWith(String type) {
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        sut = new BuildObjectMouseStrategy(commandQueue, graphicView, shapeList, builder);
        sut.addObserver(observer);
    }

    @Test
    public void whenFinishingTwoPointObject_shouldNotifyObserver() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onLeftClick(3, 4);
        sut.onLeftClick(5, 6);

        verify(observer).update(sut, "finished");
    }

    @Test
    public void whenRightClicking_shouldNotifyObserver() {
        makeBuildObjectMouseStrategyWith("Line");

        sut.onRightClick();

        verify(observer).update(sut, "finished");
    }

    @Test
    public void whenBuildingMultiPointObject_shouldNotNotifyObserverUntilRightClick() {
        makeBuildObjectMouseStrategyWith("PolyLine");

        sut.onLeftClick(3, 4);
        sut.onLeftClick(5, 6);
        sut.onLeftClick(7, 8);
        sut.onLeftClick(9, 10);
        verifyZeroInteractions(observer);

        sut.onRightClick();
        verify(observer).update(sut, "finished");
    }


}