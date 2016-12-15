package irmb.test.presentation.factory;

import irmb.flowsim.presentation.builder.PaintableLineBuilder;
import irmb.flowsim.presentation.builder.PaintablePolyLineBuilder;
import irmb.flowsim.presentation.builder.PaintableRectangleBuilder;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableFactory;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Sven on 14.12.2016.
 */
public class PaintableShapeBuilderFactoryImplTest {

    private PaintableShapeBuilderFactoryImpl sut;

    @Before
    public void setUp() {
        PaintableFactory factory = mock(PaintableFactory.class);
        sut = new PaintableShapeBuilderFactoryImpl(factory);
    }

    @Test
    public void testMakeLineBuilder() {
        assertThat(sut.makeShapeBuilder("Line"), is(instanceOf(PaintableLineBuilder.class)));
    }

    @Test
    public void testMakeRectangleBuilder() {
        assertThat(sut.makeShapeBuilder("Rectangle"), is(instanceOf(PaintableRectangleBuilder.class)));
    }

    @Test
    public void testMakePolyLineBuilder() {
        assertThat(sut.makeShapeBuilder("PolyLine"), is(instanceOf(PaintablePolyLineBuilder.class)));
    }

    @Test
    public void testInvalidInput() {
        assertNull(sut.makeShapeBuilder("invalid"));
        assertNull(sut.makeShapeBuilder("2322"));
        assertNull(sut.makeShapeBuilder("fw5r435"));
    }
}