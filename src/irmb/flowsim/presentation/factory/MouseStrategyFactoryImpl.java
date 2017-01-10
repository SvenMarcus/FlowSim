package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.strategies.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.strategies.MouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 10.01.2017.
 */
public class MouseStrategyFactoryImpl implements StrategyFactory {

    private final List<PaintableShape> shapeList;
    private final CommandQueue commandQueue;
    private final GraphicView graphicView;
    private final PaintableShapeBuilderFactory factory;

    public MouseStrategyFactoryImpl(List<PaintableShape> shapeList, CommandQueue commandQueue, GraphicView graphicView, PaintableShapeBuilderFactory factory) {
        this.shapeList = shapeList;
        this.commandQueue = commandQueue;
        this.graphicView = graphicView;
        this.factory = factory;
    }

    @Override
    public MouseStrategy makeStrategy(String type) {
        return new BuildObjectMouseStrategy(commandQueue, factory, graphicView, shapeList);
    }
}
