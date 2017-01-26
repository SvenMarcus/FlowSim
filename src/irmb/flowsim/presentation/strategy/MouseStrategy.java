package irmb.flowsim.presentation.strategy;


import irmb.flowsim.util.Observable;

/**
 * Created by Sven on 05.01.2017.
 */
public abstract class MouseStrategy extends Observable<StrategyEventArgs> {

    public abstract void onLeftClick(double x, double y);

    public abstract void onMouseMove(double x, double y);

    public abstract void onMouseDrag(double x, double y);

    public abstract void onWheelClick(double x, double y);

    public abstract void onRightClick();

    public abstract void onMouseRelease();

}
