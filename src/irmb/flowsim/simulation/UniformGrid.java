package irmb.flowsim.simulation;

import irmb.flowsim.model.Point;

/**
 * Created by sven on 04.03.17.
 */
public abstract class UniformGrid {
    public abstract void setHeight(double length);

    public abstract double getHeight();

    public abstract void setWidth(double width);

    public abstract double getWidth();

    public abstract void setHorizontalNodes(int horizontalNodes);

    public abstract int getHorizontalNodes();

    public abstract void setVerticalNodes(int verticalNodes);

    public abstract int getVerticalNodes();

    public abstract double getVelocityAt(int x, int y);

    public abstract Point getTopLeft();

    public abstract void setDelta(double deltaX);

    public abstract double getDelta();

    public abstract void resetSolidNodes();

    public abstract void setSolid(int x, int y);

    public abstract boolean isPointInside(Point point);

    public abstract boolean isSolid(int x, int y);

    public abstract double getHorizontalVelocityAt(int x, int y);

    public abstract double getVerticalVelocityAt(int x, int y);

    public abstract double getMNUPS();
}
