package irmb.flowsim.presentation;

/**
 * Created by sven on 06.03.17.
 */
public class Color {

    public static Color BLACK = new Color(0,0,0);

    public int r = 0;
    public int g = 0;
    public int b = 0;

    public Color() {
    }

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
}
