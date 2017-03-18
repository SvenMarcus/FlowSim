package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.Color;

/**
 * Created by sven on 06.03.17.
 */
public interface ColorFactory {
    Color makeColorForValue(double min, double max, double value);
}
