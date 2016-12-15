package irmb.test.presentation;

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.Painter;
import irmb.flowsim.presentation.factory.PaintableFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.factory.PaintableFactoryImpl;
import irmb.test.view.GraphicViewImplFake;
import irmb.test.view.PainterMockFactory;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenterTest {


    private PaintableShapeBuilderFactory shapeBuilderFactory;
    private Painter painterSpy;
    private GraphicViewImplFake graphicView;
    private GraphicViewPresenter sut;

    @Before
    public void setUp() throws Exception {
        painterSpy = PainterMockFactory.makePainter("Swing");
        PaintableFactory factory = spy(new PaintableFactoryImpl());
        shapeBuilderFactory = spy(new PaintableShapeBuilderFactoryImpl(factory));
        sut = new GraphicViewPresenter(shapeBuilderFactory);
        graphicView = new GraphicViewImplFake(sut);
        graphicView.setPainter(painterSpy);
        sut.setGraphicView(graphicView);
    }

    @Test
    public void buildLineAcceptanceTest() {
        sut.beginPaint("Line");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

        sut.beginPaint("Line");
        sut.handleLeftClick(36, 12);
        sut.handleLeftClick(25, 57);
        verify(painterSpy, times(1)).paintLine(36, 12, 25, 57);
    }

    @Test
    public void whenClickingTwiceWithoutSettingObjectType_shouldDoNothing() {
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verifyZeroInteractions(painterSpy);
    }

    @Test
    public void whenClickingThreeTimes_shouldNotPaintAgain() {
        sut.beginPaint("Line");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        sut.handleLeftClick(99, 99);
        verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);
    }

    @Test
    public void buildRectangleAcceptanceTest() {
        sut.beginPaint("Rectangle");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verify(painterSpy, times(1)).paintRectangle(13, 15, 5, 4);

        sut.beginPaint("Rectangle");
        sut.handleLeftClick(36, 12);
        sut.handleLeftClick(25, 57);
        verify(painterSpy, times(1)).paintRectangle(25, 12, 11, 45);
    }

    @Test
    public void buildPolyLineAcceptanceTest() {
        sut.beginPaint("PolyLine");
        sut.handleLeftClick(13, 15);
        sut.handleLeftClick(18, 19);
        verify(painterSpy, times(1)).paintLine(13, 15, 18, 19);

        sut.handleLeftClick(36, 12);
        verify(painterSpy, times(2)).paintLine(13, 15, 18, 19);
        verify(painterSpy, times(1)).paintLine(18, 19, 36, 12);
    }

    @Test
    public void whenRightClicking_shouldStopBuildingShape() {
        sut.beginPaint("PolyLine");
    }
}