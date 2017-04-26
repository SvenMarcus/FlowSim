package irmb.flowsim.presentation.strategy;

import irmb.flowsim.presentation.command.Command;

/**
 * Created by sven on 26.01.17.
 */
public class StrategyEventArgs {

    private StrategyState state;
    private Command command;

    public StrategyEventArgs() {
    }

    public StrategyEventArgs(StrategyState state) {
        this.state = state;
    }

    public void setState(StrategyState state) {
        this.state = state;
    }

    public StrategyState getState() {
        return state;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
