package irmb.flowsim.presentation.factory;

import irmb.flowsim.model.util.CoordinateTransformer;
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
    private final PaintableShapeBuilderFactory factory;
    private final CoordinateTransformer transformer;
    private int toleranceRadius = 3;

    public MouseStrategyFactoryImpl(List<PaintableShape> shapeList, PaintableShapeBuilderFactory factory, CoordinateTransformer transformer) {
        this.shapeList = shapeList;
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
            case "Bezier":
                return makeBuildObjectMouseStrategy(type);
            case "Move":
                return makeMoveMouseStrategy();
            default:
                return makeMoveMouseStrategy();
        }
    }

    private MouseStrategy makeMoveMouseStrategy() {
        MoveMouseStrategy moveMouseStrategy = new MoveMouseStrategy(shapeList, transformer);
        moveMouseStrategy.setToleranceRadius(toleranceRadius);
        return moveMouseStrategy;
    }

    private BuildObjectMouseStrategy makeBuildObjectMouseStrategy(String type) {
        PaintableShapeBuilder builder = factory.makeShapeBuilder(type);
        return new BuildObjectMouseStrategy(shapeList, transformer, builder);
    }

    public void setMoveStrategyToleranceRadius(int toleranceRadius) {
        this.toleranceRadius = toleranceRadius;
    }
}
