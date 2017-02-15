package irmb.flowsim.presentation;

/**
 * Created by sven on 13.02.17.
 */
public enum Zoom {
    IN(1),
    OUT(-1);

    private int dir;

    Zoom(int dir) {
        this.dir = dir;
    }

    public int direction() {
        return dir;
    }
}
