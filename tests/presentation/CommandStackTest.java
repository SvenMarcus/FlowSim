package irmb.test.presentation;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.presentation.CommandStack;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.util.Observer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Created by Sven on 12.12.2016.
 */
@RunWith(HierarchicalContextRunner.class)
public class CommandStackTest extends CommandStack {

    private CommandStackTest sut;
    private Observer<String> observer;

    @Before
    public void setUp() throws Exception {
        observer = mock(Observer.class);
        sut = new CommandStackTest();
        sut.addObserver(observer);
    }

    @Test
    public void canAddCommand() {
        Command command = mock(Command.class);
        sut.add(command);
        assertTrue(sut.commandList.contains(command));
    }

    @Test
    public void whenCallingUndo_shouldDoNothing() {
        sut.undo();
    }

    @Test
    public void whenCallingRedo_shouldDoNothing() {
        sut.redo();
    }

    @Test
    public void whenCallingUndo_shouldNotNotifyObserver() {
        sut.undo();
        verify(observer, never()).update(any());
    }

    @Test
    public void whenCallingRedo_shouldNotNotifyObserver() {
        sut.redo();
        verify(observer, never()).update(any());
    }

    public class OneCommandAddedContext {

        private Command firstCommand;

        @Before
        public void setUp() {
            firstCommand = mock(Command.class);
            sut.add(firstCommand);
        }

        @Test
        public void whenCallingUndo_shouldUndoCommand() {
            sut.undo();
            verify(firstCommand).undo();
        }

        @Test
        public void whenCallingUndoTwice_shouldOnlyUndoFirstCommand() {
            sut.undo();
            sut.undo();
            verify(firstCommand, times(1)).undo();
        }

        @Test
        public void whenCallingRedo_shouldDoNothing() {
            sut.redo();
        }

        @Test
        public void whenCallingUndo_shouldNotifyObserver() {
            sut.undo();
            verify(observer).update("undo");
        }

        public class TwoCommandsAddedContext {

            private Command secondCommand;

            @Before
            public void setUp() {
                secondCommand = mock(Command.class);
                sut.add(secondCommand);
            }

            @Test
            public void whenCallingUndo_shouldUndoSecondCommand() {
                sut.undo();
                verify(secondCommand, times(1)).undo();
            }

            @Test
            public void whenCallingUndoTwice_shouldUndoBothCommands() {
                sut.undo();
                verify(secondCommand, times(1)).undo();
                sut.undo();
                verify(firstCommand, times(1)).undo();
            }

            public class CalledUndoOnceContext {

                private Command thirdCommand;

                @Before
                public void setUp() {
                    thirdCommand = mock(Command.class);
                    sut.undo();
                }

                @Test
                public void whenCallingRedo_shouldRedoSecondCommand() {
                    sut.redo();
                    verify(secondCommand, times(1)).redo();
                }

                @Test
                public void whenAddingNewCommand_queueShouldOnlyContainFirstAndThirdCommand() {
                    sut.add(thirdCommand);

                    assertFalse(sut.commandList.contains(secondCommand));
                    assertTrue(sut.commandList.contains(firstCommand));
                    assertTrue(sut.commandList.contains(thirdCommand));
                }

                @Test
                public void whenCallingRedo_shouldNotifyObserver() {
                    sut.redo();
                    verify(observer).update("redo");
                }

                public class CalledUndoTwiceContext {
                    @Before
                    public void setUp() {
                        sut.undo();
                    }

                    @Test
                    public void whenCallingRedo_shouldRedoFirstCommand() {
                        sut.redo();
                        verify(firstCommand, times(1)).redo();
                    }

                    @Test
                    public void whenAddingNewCommand_queueShouldOnlyContainNewCommand() {
                        sut.add(thirdCommand);
                        assertTrue(sut.commandList.contains(thirdCommand));
                        assertEquals(1, sut.commandList.size());
                        assertFalse(sut.commandList.contains(firstCommand));
                        assertFalse(sut.commandList.contains(secondCommand));
                    }

                }
            }
        }
    }


}