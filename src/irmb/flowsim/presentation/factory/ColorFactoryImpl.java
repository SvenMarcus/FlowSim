package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.Color;

/**
 * Created by sven on 09.03.17.
 */
public class ColorFactoryImpl implements ColorFactory {
    @Override
    public Color makeColorForValue(double min, double max, double value) {
        if (value < min) value = min;
        if (value > max) value = max;
        double scale = (value - min) / (max - min);
        int r, g, b;
        r = b = g = 0;
        if (scale <= 0.25) {
            g = (int) (255 * scale / 0.25);
            b = 255;
        } else if (scale <= 0.5) {
            b = 255 - (int) Math.round(255 * (scale - 0.25) / 0.25);
            g = 255;
        } else if (scale <= 0.75) {
            r = (int) Math.round(255 * (scale - 0.5) / 0.25);
            g = 255;
        } else if (scale <= 1) {
            g = 255 - (int) Math.round(255 * (scale - 0.75) / 0.25);
            r = 255;
        }

        r = r < 0 ? 0 : r;
        g = g < 0 ? 0 : g;
        b = b < 0 ? 0 : b;

        r = r > 255 ? 255 : r;
        g = g > 255 ? 255 : g;
        b = b > 255 ? 255 : b;
        return new Color(r, g, b);
    }
}

