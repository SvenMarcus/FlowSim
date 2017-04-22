package irmb.flowsim.view.javafx;

import irmb.flowsim.view.RepaintScheduler;
import javafx.animation.AnimationTimer;

/**
 * Created by sven on 22.04.17.
 */
public class JavaFXRepaintScheduler extends RepaintScheduler {

    private final AnimationTimer animationTimer;
    private long lastUpdate;

    public JavaFXRepaintScheduler(Runnable runnable) {
        super(runnable);
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                long delay = Math.round(1_000_000_000 / (double) wantedFPS);
                if (now - lastUpdate < delay)
                    return;
                performRepaintAction();
                lastUpdate = now;
            }
        };
    }

    @Override
    public void start() {
        animationTimer.start();
    }

    @Override
    public void stop() {
        animationTimer.stop();
    }

}
