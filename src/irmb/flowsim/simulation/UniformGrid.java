package irmb.flowsim.simulation;

import irmb.flowsim.model.Point;

/**
 * Created by sven on 04.03.17.
 */
public interface UniformGrid {
    void setHeight(double length);

    double getHeight();

    void setWidth(double width);

    double getWidth();

    void setDx(double dx);

    int getHorizontalNodes();

    void setDy(double dy);

    int getVerticalNodes();

    double getVelocityAt(int x, int y);

    Point getOrigin();
}
