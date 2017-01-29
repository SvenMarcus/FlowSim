package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.util.CoordinateTransformer;
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
    private final CoordinateTransformer transformer;


    public MouseStrategyFactoryImpl(List<PaintableShape> shapeList, CommandQueue commandQueue, GraphicView graphicView, PaintableShapeBuilderFactory factory, CoordinateTransformer transformer) {
        this.shapeList = shapeList;
        this.commandQueue = commandQueue;
        this.graphicView = graphicView;
        this.factory = factory;
        this.transformer = transformer;
    }

    @Override
    public MouseStrategy makeStrategy(String type) {
        switch (type) {
            case "Line":
                return makeBuildObjectMouseStrategy(type);
            case "Rectangle":
                return makeBuildObjectMouseStrategy(type);
            case "PolyLine":
                return makeBuildObjectMouseStrategy(type);
            case "Move":
                return new MoveMouseStrategy(shapeList, transformer);
            default:
                return new MoveMouseStrategy(shapeList, transformer);
        }
    }

    private BuildObjectMouseStrategy makeBuildObjectMouseStrategy(String type) {
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        return new BuildObjectMouseStrategy(shapeList, builder, transformer);
    }
}
