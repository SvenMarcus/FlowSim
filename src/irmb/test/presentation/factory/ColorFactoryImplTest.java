package irmb.test.presentation.factory;

import irmb.flowsim.presentation.Color;
import irmb.flowsim.presentation.factory.ColorFactoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by sven on 09.03.17.
 */
public class ColorFactoryImplTest {
    @Test
    public void testMakeColor() {
        Color result;
        ColorFactoryImpl sut = new ColorFactoryImpl();

        result = sut.makeColorForValue(10, 20, 10);
        assertEquals(0, result.r);
        assertEquals(0, result.g);
        assertEquals(255, result.b);

        result = sut.makeColorForValue(10, 20, 20);
        assertEquals(255, result.r);
        assertEquals(0, result.g);
        assertEquals(0, result.b);

        result = sut.makeColorForValue(10, 20, 11);
        assertEquals(0, result.r);
        assertEquals(102, result.g);
        assertEquals(255, result.b);

        result = sut.makeColorForValue(10, 20, 12.5);
        assertEquals(0, result.r);
        assertEquals(255, result.g);
        assertEquals(255, result.b);

        result = sut.makeColorForValue(10, 20, 15);
        assertEquals(0, result.r);
        assertEquals(255, result.g);
        assertEquals(255, result.b);

        result = sut.makeColorForValue(10, 20, 13);
        assertEquals(0, result.r);
        assertEquals(255, result.g);
        assertEquals(51, result.b);

        result = sut.makeColorForValue(10, 20, 16);
        assertEquals(102, result.r);
        assertEquals(255, result.g);
        assertEquals(0, result.b);

        result = sut.makeColorForValue(10, 20, 18);
        assertEquals(255, result.r);
        assertEquals(204, result.g);
        assertEquals(0, result.b);
    }
}