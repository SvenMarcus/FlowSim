package irmb.flowsim.model;

/**
 * Created by sven on 18.03.17.
 */
public interface TwoPointShape extends Shape {
    void setFirst(Point point);

    void setSecond(Point point);
}
