package irmb.flowsim.view.javafx;/**
 * Created by Sven on 22.12.2016.
 */

import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.factory.ShapeFactoryImpl;
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

        GraphicViewPresenter presenter = makePresenter(builderFactory, commandQueue, shapeList);
        RootController rootController = new RootController(presenter);

        Pane rootLayout = makeRootLayout(rootController);
        presenter.setGraphicView(rootController);

        Scene root = new Scene(rootLayout);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    private GraphicViewPresenter makePresenter(PaintableShapeBuilderFactory builderFactory, CommandQueue commandQueue, List<PaintableShape> shapeList) {
        return new GraphicViewPresenter(builderFactory, commandQueue, shapeList);
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
