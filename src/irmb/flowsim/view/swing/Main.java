package irmb.flowsim.view.swing;

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.factory.MouseStrategyFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.factory.ShapeFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 15.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        ShapeFactoryImpl paintableFactory = new ShapeFactoryImpl();
        PaintableShapeBuilderFactory builderFactory = new PaintableShapeBuilderFactoryImpl(paintableFactory);
        CommandQueue commandQueue = new CommandQueue();
        List<PaintableShape> shapeList = new LinkedList<>();
        MainWindow window = new MainWindow();
        MouseStrategyFactory mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, window.getGraphicView(), builderFactory);
        GraphicViewPresenter presenter = new GraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList);

        presenter.setGraphicView(window.getGraphicView());
        window.setVisible(true);
    }
}
