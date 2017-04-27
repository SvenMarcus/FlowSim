package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.Line;
import irmb.flowsim.model.Point;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Shape;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.CommandStack;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.presentation.builder.*;
import irmb.flowsim.presentation.factory.*;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.test.simulation.SimulationSpy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by sven on 13.03.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class SimulationGraphicViewPresenterTestWithMouseStrategyFactory {

    private Painter painterSpy;
    private GraphicView graphicViewMock;
    private SimulationGraphicViewPresenter sut;
    private CoordinateTransformer transformer;
    private SimulationSpy simulationSpy;
    private List<PaintableShape> shapeList;
    private Line line;
    private MouseStrategyFactory mouseStrategyFactory;
    private PolyLine polyLine;
    private int pointsAdded;
    private List<Point> addedPoints;

    @Before
    public void setUp() {
        pointsAdded = 0;
        addedPoints = new ArrayList<>();
        graphicViewMock = mock(GraphicView.class);
        CommandStack commandStack = mock(CommandStack.class);
        shapeList = new ArrayList<>();
        makeTransformerMock();

        makeMouseStrategyFactory(commandStack);
        simulationSpy = spy(new SimulationSpy());
        SimulationFactory simulationFactory = makeSimulationFactory();
        sut = new SimulationGraphicViewPresenter(mouseStrategyFactory, commandStack, shapeList, transformer, simulationFactory);
        painterSpy = mock(Painter.class);
        setGraphicViewMockBehavior();
        sut.setGraphicView(graphicViewMock);
    }

    private SimulationFactory makeSimulationFactory() {
        SimulationFactory simulationFactory = mock(SimulationFactory.class);
        when(simulationFactory.makeSimulation()).thenReturn(simulationSpy);
        return simulationFactory;
    }

    private void makeMouseStrategyFactory(CommandStack commandStack) {
        PaintableShapeBuilderFactory shapeBuilderFactory = makeShapeBuilderFactory();
        mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, shapeBuilderFactory, transformer);
    }

    private void makeTransformerMock() {
        transformer = mock(CoordinateTransformer.class);
        when(transformer.transformToPointOnScreen(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
        when(transformer.transformToWorldPoint(any(Point.class))).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));
    }

    private PaintableShapeBuilderFactory makeShapeBuilderFactory() {
        line = new Line();
        polyLine = new PolyLine();
        PaintableLine paintableLine = new PaintableLine(line);
        PaintablePolyLine paintablePolyLine = new PaintablePolyLine(polyLine);


        PaintableShapeBuilderFactory shapeBuilderFactory = mock(PaintableShapeBuilderFactory.class);
        PaintableShapeFactory paintableShapeFactory = mock(PaintableShapeFactory.class);
        when(paintableShapeFactory.makePaintableShape(any(Shape.class))).thenAnswer(invocationOnMock -> {
            Shape shape = invocationOnMock.getArgument(0);
            return shape instanceof Line ? paintableLine : paintablePolyLine;
        });

        TwoPointShapeBuilder lineBuilder = new TwoPointShapeBuilder(line, paintableShapeFactory);
        MultiPointShapeBuilder polyLineBuilder = new MultiPointShapeBuilder(polyLine, paintableShapeFactory);
        when(shapeBuilderFactory.makeShapeBuilder("Line")).thenReturn(lineBuilder);
        when(shapeBuilderFactory.makeShapeBuilder("PolyLine")).thenReturn(polyLineBuilder);
        return shapeBuilderFactory;
    }

    private void setGraphicViewMockBehavior() {
        doAnswer(invocationOnMock -> {
            for (Paintable shape : sut.getPaintableList())
                shape.paint(painterSpy, transformer);
            return null;
        }).when(graphicViewMock).update();
    }

    @Test
    public void whenUpdatingGraphicViewAfterSettingFirstPointOfShape_shouldNotPaintShape() {
        sut.beginPaint("Line");
        sut.handleLeftClick(17, 8);
        graphicViewMock.update();
        verify(painterSpy, never()).paintLine(anyDouble(), anyDouble(), anyDouble(), anyDouble());
    }

    @Test
    public void whenBuildingLineBeforeAddingSimulation_shouldNotMapToGrid() {
        sut.beginPaint("Line");
        sut.handleLeftClick(1, 3);
        sut.handleMouseMove(6, 7);
        sut.handleLeftClick(6, 7);

        verifyZeroInteractions(simulationSpy);
    }

    public class SimulationAddedContext {

        @Before
        public void setUp() {
            sut.addSimulation();
            clearInvocations(simulationSpy);
        }

        @Test
        public void whenAddingShape_shouldMapShapeToGrid() {
            sut.beginPaint("Line");
            sut.handleLeftClick(1, 3);
            sut.handleMouseMove(6, 7);
            sut.handleLeftClick(6, 7);

            verify(simulationSpy, atLeast(2)).setShapes(shapeList);
        }

        @Test
        public void whenShapeChangesWhileConstructing_shouldMapToGrid() {
            sut.beginPaint("Line");
            sut.handleLeftClick(1, 3);
            sut.handleMouseMove(6, 7);

            verify(simulationSpy).setShapes(shapeList);
        }

        @Test
        public void whenOnlyMovingMouse_shouldNotMapToGrid() {
            sut.handleMouseMove(6, 7);
            verifyZeroInteractions(simulationSpy);
        }

        @Test
        public void whenPerformingBuildActionsWithoutBeginningPaint_shouldNotMapToGrid() {
            sut.handleLeftClick(1, 3);
            sut.handleMouseMove(6, 7);
            sut.handleLeftClick(6, 7);

            verifyZeroInteractions(simulationSpy);
        }

        @Test
        public void whenClickingAndMovingMouseAfterFinishingShape_shouldNotMapToGrid() {
            sut.beginPaint("Line");
            sut.handleLeftClick(1, 3);
            sut.handleMouseMove(6, 7);
            sut.handleLeftClick(6, 7);
            clearInvocations(simulationSpy);

            sut.handleLeftClick(5, 6);
            sut.handleMouseMove(3, 7);

            verifyZeroInteractions(simulationSpy);
        }

        @Test
        public void whenBuildingPolyLineAndAddingThreePoints_shouldMapTpGrid() {
            sut.beginPaint("PolyLine");
            sut.handleLeftClick(1, 3);
            sut.handleMouseMove(6, 7);
            sut.handleLeftClick(6, 7);
            sut.handleMouseMove(15, 17);
            sut.handleLeftClick(15, 17);

            verify(simulationSpy, atLeast(4)).setShapes(shapeList);
        }


    }

}
