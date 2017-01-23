package irmb.test.presentation.strategies;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.builder.PaintableLineBuilder;
import irmb.flowsim.presentation.builder.PaintablePolyLineBuilder;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.presentation.strategy.BuildObjectMouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 09.01.2017.
 */
public class BuildObjectMouseStrategyTest {

    private CommandQueue commandQueue;
    private PaintableShapeBuilderFactory factory;
    private GraphicView graphicView;
    private List<PaintableShape> shapeList;
    private Observer observer;
    private BuildObjectMouseStrategy sut;
    private int pointsAdded;
    private PaintableShapeBuilder lineBuilderMock;

    @Before
    public void setUp() throws Exception {
        commandQueue = mock(CommandQueue.class);
        graphicView = mock(GraphicView.class);
        shapeList = mock(List.class);
        observer = mock(Observer.class);
        factory = mock(PaintableShapeBuilderFactory.class);
        lineBuilderMock = mock(PaintableShapeBuilder.class);
        setMockBehavior();
    }

    private void setMockBehavior() {
        doAnswer(invocationOnMock -> {
            incrementPointsAdded();
            return null;
        }).when(lineBuilderMock).addPoint(any());
        when(lineBuilderMock.isObjectFinished()).thenAnswer(invocationOnMock -> pointsAdded >= 2);
    }

    private void incrementPointsAdded() {
        pointsAdded++;
    }

    private void makeBuildObjectMouseStrategyWith(String type) {
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        sut = new BuildObjectMouseStrategy(commandQueue, graphicView, shapeList, builder, null);
        sut.addObserver(observer);
    }

    @Test
    public void whenFinishingTwoPointObject_shouldNotifyObserver() {
        when(factory.makeShapeBuilder("Line")).thenReturn(lineBuilderMock);

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