package irmb.test.presentation;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.*;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;
import irmb.test.view.PainterMockFactory;
import irmb.test.view.SwingGraphicViewFake;
import org.junit.Before;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.spy;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenterTest {

    protected CommandQueue commandQueue;
    protected List<PaintableShape> shapeList;
    protected PaintableShapeBuilderFactory shapeBuilderFactory;
    protected Painter painterSpy;
    protected SwingGraphicViewFake graphicView;
    protected GraphicViewPresenter sut;

    @Before
    public void setUp() throws Exception {
        painterSpy = PainterMockFactory.makePainter("Swing");
        ShapeFactory factory = spy(new ShapeFactoryImpl());
        shapeBuilderFactory = spy(new PaintableShapeBuilderFactoryImpl(factory));
        commandQueue = spy(new CommandQueue());
        shapeList = new LinkedList<>();
        graphicView = spy(new SwingGraphicViewFake());
        MouseStrategyFactory mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, graphicView, shapeBuilderFactory);
        sut = new GraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList);
        graphicView.setPresenter(sut);
        graphicView.setPainter(painterSpy);
        sut.setGraphicView(graphicView);
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

    protected void buildPolyLine(List<Double> list) {
        sut.beginPaint("PolyLine");
        sut.handleLeftClick(list.get(0), list.get(1));
        for (int i = 2; i < list.size() - 1; i += 2) {
            sut.handleMouseMove(list.get(i), list.get(i + 1));
            sut.handleLeftClick(list.get(i), list.get(i + 1));
        }
        sut.handleRightClick();
    }

}