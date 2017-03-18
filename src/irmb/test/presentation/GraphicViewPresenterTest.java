package irmb.test.presentation;

import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.*;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

import static irmb.test.util.TestUtil.makePoint;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenterTest {

    protected CommandQueue commandQueue;
    protected List<PaintableShape> shapeList;
    protected PaintableShapeBuilderFactory shapeBuilderFactory;
    protected Painter painterSpy;
    protected GraphicView graphicView;
    protected CoordinateTransformer transformer;
    protected GraphicViewPresenter sut;


    @Before
    public void setUp() throws Exception {
        transformer = new CoordinateTransformerImpl();
        setWorldAndViewBounds();
        painterSpy = mock(Painter.class);
        ShapeFactory factory = spy(new ShapeFactoryImpl());
        shapeBuilderFactory = spy(new PaintableShapeBuilderFactoryImpl(factory));
        commandQueue = spy(new CommandQueue());
        shapeList = new LinkedList<>();
        graphicView = mock(GraphicView.class);
        doAnswer(invocationOnMock -> {
            for (Paintable shape : sut.getPaintableList())
                shape.paint(painterSpy, transformer);
            return null;
        }).when(graphicView).update();
        MouseStrategyFactory mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, graphicView, shapeBuilderFactory, transformer);
        sut = new GraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList, transformer);
        sut.setGraphicView(graphicView);
    }

    private void setWorldAndViewBounds() {
        transformer.setWorldBounds(makePoint(-15, 10), makePoint(10, -10));
        transformer.setViewBounds(makePoint(0, 0), makePoint(800, 600));
    }


    protected void performMove(double x1, double y1, double x2, double y2) {
        sut.handleLeftClick(x1, y1);
        sut.handleMouseDrag(x2, y2);
        sut.handleMouseRelease();
    }

    protected void buildLine(double x1, double y1, double x2, double y2) {
        sut.beginPaint("Line");
        sut.handleLeftClick(x1, y1);
        sut.handleMouseMove(x2, y2);
        sut.handleLeftClick(x2, y2);
    }

    protected void buildRectangle(double x1, double y1, double x2, double y2) {
        sut.beginPaint("Rectangle");
        sut.handleLeftClick(x1, y1);
        sut.handleMouseMove(x2, y2);
        sut.handleLeftClick(x2, y2);
    }

    protected void buildPolyLine(List<Double> list) {
        sut.beginPaint("PolyLine");
        sut.handleLeftClick(list.get(0), list.get(1));
        for (int i = 2; i < list.size() - 1; i += 2) {
            sut.handleMouseMove(list.get(i), list.get(i + 1));
            sut.handleLeftClick(list.get(i), list.get(i + 1));
        }
        sut.handleRightClick(0, 0);
    }

}