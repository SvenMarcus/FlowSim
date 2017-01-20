package irmb.flowsim.presentation.factory;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicView;
import irmb.flowsim.presentation.builder.PaintableShapeBuilder;
import irmb.flowsim.presentation.strategy.BuildObjectMouseStrategy;
import irmb.flowsim.presentation.strategy.MouseStrategy;
import irmb.flowsim.presentation.strategy.MoveMouseStrategy;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 10.01.2017.
 */
public class MouseStrategyFactoryImpl implements MouseStrategyFactory {

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
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        switch (type) {
            case "Line":
                return new BuildObjectMouseStrategy(commandQueue, graphicView, shapeList, builder);
            case "Rectangle":
                return new BuildObjectMouseStrategy(commandQueue, graphicView, shapeList, builder);
            case "PolyLine":
                return new BuildObjectMouseStrategy(commandQueue, graphicView, shapeList, builder);
            case "Move":
                return new MoveMouseStrategy(commandQueue, graphicView, shapeList);
            default:
                return new MoveMouseStrategy(commandQueue, graphicView, shapeList);
        }
    }
}
