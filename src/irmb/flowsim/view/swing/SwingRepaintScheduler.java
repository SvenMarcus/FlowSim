package irmb.flowsim.view.swing;

import irmb.flowsim.view.RepaintScheduler;

import javax.swing.*;

/**
 * Created by sven on 22.04.17.
 */
public class SwingRepaintScheduler extends RepaintScheduler {

    private final Timer timer;

    public SwingRepaintScheduler(Runnable runnable) {
        super(runnable);
        timer = new Timer(20, e -> performRepaintAction());
    }

    @Override
    public void start() {
        timer.start();
    }

    @Override
    public void stop() {
        timer.stop();
    }

    @Override
    public void setDelay(int delay) {
        timer.setDelay(delay);
    }
}
