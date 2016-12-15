package irmb.flowsim.presentation;

/**
 * Created by Sven on 15.12.2016.
 */
public interface Painter {
    void paintLine(double x1, double y1, double x2, double y2);

    void paintRectangle(double x, double y, double width, double height);

}
