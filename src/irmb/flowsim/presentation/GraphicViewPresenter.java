package irmb.flowsim.presentation;

import irmb.flowsim.presentation.command.ClearAllCommand;
import irmb.flowsim.presentation.command.Command;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.StrategyState;
import irmb.flowsim.view.graphics.Paintable;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 13.12.2016.
 */
public class GraphicViewPresenter {

    protected GraphicView graphicView;

    private MouseStrategyFactory factory;
    protected MouseStrategy strategy;

    protected List<PaintableShape> shapeList;
    protected CommandStack commandStack;


    public GraphicViewPresenter(MouseStrategyFactory strategyFactory, CommandStack commandStack, List<PaintableShape> shapeList) {
        this.factory = strategyFactory;
        this.commandStack = commandStack;
        this.shapeList = shapeList;
        attachObserverToCommandStack();
        makeStrategy("Move");
    }

    protected void attachObserverToCommandStack() {
        this.commandStack.addObserver((args) -> graphicView.update());
    }

    public void setGraphicView(GraphicView graphicView) {
        this.graphicView = graphicView;
    }

    public void beginPaint(String objectType) {
        makeStrategy(objectType);
    }

    public void handleLeftClick(double x, double y) {
        strategy.onLeftClick(x, y);
    }

    public void handleRightClick(double x, double y) {
        strategy.onRightClick(x, y);
    }

    public void handleMouseMove(double x, double y) {
        strategy.onMouseMove(x, y);
    }

    public void handleMouseDrag(double x, double y) {
        strategy.onMouseDrag(x, y);
    }

    public void handleMouseRelease() {
        strategy.onMouseRelease();
    }

    public void handleMiddleClick(double x, double y) {
        strategy.onMiddleClick(x, y);
    }

    public void handleScroll(double x, double y, int delta) {
        strategy.onScroll(x, y, delta);
    }

    public void undo() {
        commandStack.undo();
    }

    public void redo() {
        commandStack.redo();
    }

    public void clearAll() {
        Command clearAllCommand = new ClearAllCommand(shapeList);
        clearAllCommand.execute();
        commandStack.add(clearAllCommand);
        graphicView.update();
    }

    protected void makeStrategy(String objectType) {
        strategy = factory.makeStrategy(objectType);
        addStrategyObserver();
    }

    protected void addStrategyObserver() {
        strategy.addObserver((arg) -> {
            if (arg.getState() == StrategyState.FINISHED)
                makeStrategy("Move");
            if (arg.getCommand() != null)
                commandStack.add(arg.getCommand());
            graphicView.update();
        });
    }

    public List<Paintable> getPaintableList() {
        return new ArrayList<>(shapeList);
    }


}
