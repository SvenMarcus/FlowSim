package irmb.test.presentation.factory;

import irmb.flowsim.model.BezierCurve;
import irmb.flowsim.model.Line;
import irmb.flowsim.model.PolyLine;
import irmb.flowsim.model.Rectangle;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;

/**
 * Created by Sven on 14.12.2016.
 */
public class ShapeFactoryImplTest {

    private ShapeFactoryImpl sut;

    @Before
    public void setUp() throws Exception {
        sut = new ShapeFactoryImpl();
    }

    @Test
    public void testMakeLine() {
        assertThat(sut.makeShape("Line"), is(instanceOf(Line.class)));
    }

    @Test
    public void testMakeRectangle() {
        assertThat(sut.makeShape("Rectangle"), is(instanceOf(Rectangle.class)));
    }

    @Test
    public void testMakePolyLine() {
        assertThat(sut.makeShape("PolyLine"), is(instanceOf(PolyLine.class)));
    }

    @Test
    public void testMakeBezierCurve() {
        assertThat(sut.makeShape("Bezier"), is(instanceOf(BezierCurve.class)));
    }

    @Test
    public void testInvalidInput() {
        assertNull(sut.makeShape("invalid"));
        assertNull(sut.makeShape("2322"));
        assertNull(sut.makeShape("fw5r435"));
    }
}