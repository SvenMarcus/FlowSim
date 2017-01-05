package irmb.test.presentation;

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.ShapeFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.factory.ShapeFactoryImpl;
import irmb.test.view.SwingGraphicViewFake;
import irmb.test.view.PainterMockFactory;
import org.junit.Before;

import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenterTest {


    protected PaintableShapeBuilderFactory shapeBuilderFactory;
    protected Painter painterSpy;
    protected SwingGraphicViewFake graphicView;
    protected GraphicViewPresenter sut;

    @Before
    public void setUp() throws Exception {
        painterSpy = PainterMockFactory.makePainter("Swing");
        ShapeFactory factory = spy(new ShapeFactoryImpl());
        shapeBuilderFactory = spy(new PaintableShapeBuilderFactoryImpl(factory));
        sut = new GraphicViewPresenter(shapeBuilderFactory);
        graphicView = spy(new SwingGraphicViewFake(sut));
        graphicView.setPainter(painterSpy);
        sut.setGraphicView(graphicView);
    }


    protected void performMove(double x1, double y1, double x2, double y2) {
        sut.handleLeftClick(x1, y1);
        sut.handleMouseDrag(x2, y2);
        sut.handleMouseRelease(x2, y2);
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