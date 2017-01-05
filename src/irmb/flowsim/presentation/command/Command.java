package irmb.flowsim.presentation.command;

/**
 * Created by Sven on 12.12.2016.
 */
public interface Command {
    void execute();

    void undo();

    void redo();
}
