package irmb.flowsim.presentation;

import irmb.flowsim.presentation.command.Command;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 12.12.2016.
 */
public class CommandQueue {


    protected List<Command> commandList;
    private int currentIndex = -1;

    public CommandQueue() {
        commandList = new LinkedList<>();
    }

    public void add(Command command) {
        while (commandList.size() - 1 > currentIndex)
            commandList.remove(commandList.size() - 1);
        commandList.add(command);
        currentIndex++;
    }

    public void undo() {
        if (currentIndex > -1) commandList.get(currentIndex--).undo();
    }

    public void redo() {
        int size = commandList.size();
        if (currentIndex < size - 1)
            commandList.get(++currentIndex).redo();
    }
}
