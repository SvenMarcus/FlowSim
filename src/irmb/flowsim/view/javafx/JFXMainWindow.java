package irmb.flowsim.view.javafx;/**
 * Created by Sven on 22.12.2016.
 */

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.presentation.factory.MouseStrategyFactoryImpl;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.presentation.factory.ShapeFactoryImpl;
import irmb.flowsim.simulation.Simulation;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.simulation.SimulationFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JFXMainWindow extends Application {


    private PaintableShapeBuilderFactory builderFactory;
    private List<PaintableShape> shapeList;
    private CommandQueue commandQueue;
    private CoordinateTransformer transformer;
    private MouseStrategyFactoryImpl mouseStrategyFactory;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        builderFactory = makePaintableShapeBuilderFactory();
        shapeList = new LinkedList<>();
        commandQueue = new CommandQueue();

        transformer = new CoordinateTransformerImpl();
        transformer.setWorldBounds(new Point(0, 0.5), new Point(1, 0));
        transformer.setViewBounds(new Point(0, 0), new Point(800, 600));

        RootController rootController = new RootController();

        mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, rootController, builderFactory, transformer);


        SimulationGraphicViewPresenter presenter = makePresenter();

        rootController.setPresenter(presenter);

        Pane rootLayout = makeRootLayout(rootController);
        presenter.setGraphicView(rootController);

        primaryStage.setOnCloseRequest((event -> Platform.exit()));

        Scene root = new Scene(rootLayout);
        primaryStage.setScene(root);
        primaryStage.show();
    }

    private SimulationGraphicViewPresenter makePresenter() {
        SimulationFactory simulationFactory = new SimulationFactoryImpl();
        return new SimulationGraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList, transformer, simulationFactory);
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
