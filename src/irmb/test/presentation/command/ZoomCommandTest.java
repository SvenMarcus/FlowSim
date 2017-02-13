package irmb.test.presentation.command;

import de.bechte.junit.runners.context.HierarchicalContextRunner;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.presentation.command.ZoomCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Created by sven on 13.02.17.
 */
@RunWith(HierarchicalContextRunner.class)
public class ZoomCommandTest {

    private ZoomCommand sut;
    private CoordinateTransformer transformer;

    @Before
    public void setUp() throws Exception {
        transformer = mock(CoordinateTransformer.class);
        sut = new ZoomCommand(transformer);
    }

    @Test
    public void canSetZoomFactor() {
        sut.setZoomFactor(-0.05);
    }

    @Test
    public void canSetZoomPoint() {
        sut.setZoomPoint(0, 0);
    }

    @Test
    public void whenExecuting_shouldCallZoomWindowWithFactorAndZoomPoint() {
        sut.setZoomFactor(-0.05);
        sut.setZoomPoint(12, 68);

        sut.execute();
        verify(transformer).zoomWindow(-0.05, 12, 68);

        sut.setZoomFactor(0.15);
        sut.setZoomPoint(54, -5541);

        sut.execute();
        verify(transformer).zoomWindow(0.15, 54, -5541);
    }

    public class CommandExecutedContext {
        @Before
        public void setUp() {
            sut.setZoomFactor(-0.05);
            sut.setZoomPoint(12, 68);
            sut.execute();
            clearInvocations(transformer);
        }

        @Test
        public void whenCallingUndo_shouldUndoZoom() {
            sut.undo();
            verify(transformer).zoomWindow(0.05, 12, 68);
        }

        public class UndoCalledContext {
            @Before
            public void setUp() {
                sut.undo();
                clearInvocations(transformer);
            }

            @Test
            public void whenCallingRedo_shouldRedoZoom() {
                sut.redo();
                verify(transformer).zoomWindow(-0.05, 12, 68);
            }
        }
    }
}