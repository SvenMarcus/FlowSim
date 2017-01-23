package irmb.flowsim.view.javafx;/**
 * Created by Sven on 22.12.2016.
 */

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.factory.MouseStrategyFactory;
import irmb.flowsim.presentation.factory.MouseStrategyFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JFXMainWindow extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        PaintableShapeBuilderFactory builderFactory = makePaintableShapeBuilderFactory();
        List<PaintableShape> shapeList = new LinkedList<>();
        CommandQueue commandQueue = new CommandQueue();

        RootController rootController = new RootController();
        MouseStrategyFactoryImpl mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, rootController, builderFactory, null);
        GraphicViewPresenter presenter = makePresenter(commandQueue, shapeList, mouseStrategyFactory);
        rootController.setPresenter(presenter);

        Pane rootLayout = makeRootLayout(rootController);
        presenter.setGraphicView(rootController);

        Scene root = new Scene(rootLayout);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    private GraphicViewPresenter makePresenter(CommandQueue commandQueue, List<PaintableShape> shapeList, MouseStrategyFactory strategyFactory) {
        return new GraphicViewPresenter(strategyFactory, commandQueue, shapeList);
    }

    private PaintableShapeBuilderFactory makePaintableShapeBuilderFactory() {
        ShapeFactoryImpl paintableFactory = new ShapeFactoryImpl();
        return new PaintableShapeBuilderFactoryImpl(paintableFactory);
    }

    private Pane makeRootLayout(RootController rootController) throws IOException {
        Pane rootLayout;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RootLayout.fxml"));
        loader.setController(rootController);
        rootLayout = loader.load();
        return rootLayout;
    }
}
