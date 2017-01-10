package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.strategies.MouseStrategy;

/**
 * Created by Sven on 10.01.2017.
 */
public interface StrategyFactory {
    MouseStrategy makeStrategy(String type);
}
