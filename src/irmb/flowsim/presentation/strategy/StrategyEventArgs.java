package irmb.flowsim.presentation.strategy;

import irmb.flowsim.presentation.command.AddPaintableShapeCommand;
import irmb.flowsim.presentation.command.Command;

/**
 * Created by sven on 26.01.17.
 */
public class StrategyEventArgs {

    private STRATEGY_STATE state;
    private Command command;

    public StrategyEventArgs() {
    }

    public StrategyEventArgs(STRATEGY_STATE state) {
        this.state = state;
    }

    public void setState(STRATEGY_STATE state) {
        this.state = state;
    }

    public STRATEGY_STATE getState() {
        return state;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }
}
