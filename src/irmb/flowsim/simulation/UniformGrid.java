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

    void setHorizontalNodes(int horizontalNodes);

    int getHorizontalNodes();

    void setVerticalNodes(int verticalNodes);

    int getVerticalNodes();

    double getVelocityAt(int x, int y);

    Point getTopLeft();

    void setDelta(double deltaX);

    double getDelta();

    void resetSolidNodes();

    void setSolid(int x, int y);

    boolean isPointInside(Point point);

    boolean isSolid(int x, int y);

    double getHorizontalVelocityAt(int x, int y);

    double getVerticalVelocityAt(int x, int y);

    double getMNUPS();

    double getRealTime();

    double getViscosity();

    double getVerticalGravity();

    double getHorizontalGravity();

    int getTimeStep();
}
