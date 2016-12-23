package irmb.flowsim.view.javafx;/**
 * Created by Sven on 22.12.2016.
 */

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.factory.PaintableFactoryImpl;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class JFXMainWindow extends Application {

    private static GraphicViewPresenter presenter;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        PaintableFactoryImpl paintableFactory = new PaintableFactoryImpl();
        PaintableShapeBuilderFactory builderFactory = new PaintableShapeBuilderFactoryImpl(paintableFactory);
        presenter = new GraphicViewPresenter(builderFactory);
        Pane rootLayout;
        RootController rootController = new RootController(presenter);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RootLayout.fxml"));
        loader.setController(rootController);
        rootLayout = loader.load();
        presenter.setGraphicView(rootController);
        Scene root = new Scene(rootLayout);
        primaryStage.setScene(root);
        primaryStage.show();
    }
}
