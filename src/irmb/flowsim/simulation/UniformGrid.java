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

    void setHorizontalNodes(double horizontalNodes);

    int getHorizontalNodes();

    void setVerticalNodes(double verticalNodes);

    int getVerticalNodes();

    double getVelocityAt(int x, int y);

    Point getOrigin();

    void setDeltaX(double deltaX);

    double getDeltaX();

    double getDeltaY();

    void setDeltaY(double deltaY);
}
