package irmb.test.view;

import irmb.flowsim.presentation.Painter;
import irmb.flowsim.view.SwingPainter;

import static org.mockito.Mockito.mock;

/**
 * Created by Sven on 15.12.2016.
 */
public abstract class PainterMockFactory {
    public static Painter makePainter(String type) {
        switch (type) {
            case "Swing":
                return mock(SwingPainter.class);
            default:
                return null;
        }
    }
}
