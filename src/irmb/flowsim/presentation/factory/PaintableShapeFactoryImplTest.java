package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.Line;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.view.graphics.PaintableLine;
import irmb.flowsim.view.graphics.PaintablePolyLine;
import irmb.flowsim.view.graphics.PaintableRectangle;
import irmb.flowsim.view.graphics.PaintableShape;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by sven on 18.03.17.
 */
public class PaintableShapeFactoryImplTest {

    private PaintableShapeFactory sut;

    @Before
    public void setUp() throws Exception {
        sut = new PaintableShapeFactoryImpl();
    }

    @Test
    public void testMakePaintableLine() {
        PaintableShape shape = sut.makePaintableShape(mock(Line.class));
        assertThat(shape, is(instanceOf(PaintableLine.class)));
    }

    @Test
    public void testMakePaintableRectangle() {
        PaintableShape shape = sut.makePaintableShape(mock(Rectangle.class));
        assertThat(shape, is(instanceOf(PaintableRectangle.class)));
    }

    @Test
    public void testMakePaintablePolyLine() {
        PaintableShape shape = sut.makePaintableShape(mock(PolyLine.class));
        assertThat(shape, is(instanceOf(PaintablePolyLine.class)));
    }

}