package irmb.flowsim.presentation.strategy;

/**
 * Created by sven on 26.01.17.
 */
public class StrategyEventArgs {

    private STRATEGY_STATE state;

    public StrategyEventArgs(STRATEGY_STATE state) {
        this.state = state;
    }

    public STRATEGY_STATE getState() {
        return state;
    }
}
