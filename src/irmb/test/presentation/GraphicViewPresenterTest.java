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


    protected PaintableShapeBuilderFactory shapeBuilderFactory;
    protected Painter painterSpy;
    protected GraphicViewImplFake graphicView;
    protected GraphicViewPresenter sut;

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



}