package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.strategies.MouseStrategy;

/**
 * Created by Sven on 08.01.2017.
 */
public interface MouseStrategyFactory {
    MouseStrategy makeStrategy(String type);
}
