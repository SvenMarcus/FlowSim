package irmb.flowsim.view;

/**
 * Created by sven on 22.04.17.
 */
public abstract class RepaintScheduler {

    protected Runnable runnable;
    protected boolean needsUpdate;
    protected int wantedFPS;

    public RepaintScheduler(Runnable runnable) {
        this.runnable = runnable;
    }

    public void needsUpdate(boolean update) {
        this.needsUpdate = update;
    }

    protected void performRepaintAction() {
        if (needsUpdate) {
            runnable.run();
            needsUpdate = false;
        }
    }

    public abstract void start();

    public abstract void stop();

    public void setFPS(int fps) {
        wantedFPS = fps;
    }
}
