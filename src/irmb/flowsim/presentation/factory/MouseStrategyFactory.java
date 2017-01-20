package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.strategy.MouseStrategy;

/**
 * Created by Sven on 10.01.2017.
 */
public interface MouseStrategyFactory {
    MouseStrategy makeStrategy(String type);
}
