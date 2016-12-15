package irmb.flowsim.model;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 14.12.2016.
 */
public class PolyLine {

    private List<Point> pointList = new LinkedList<>();

    protected List<Point> getPointList() {
        return pointList;
    }

    public void addPoint(Point point) {
        pointList.add(point);
    }
}
