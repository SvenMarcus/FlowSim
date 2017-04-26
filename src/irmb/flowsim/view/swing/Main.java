package irmb.flowsim.view.swing;

import irmb.flowsim.model.Point;
import irmb.flowsim.model.util.CoordinateTransformer;
import irmb.flowsim.model.util.CoordinateTransformerImpl;
import irmb.flowsim.presentation.CommandQueue;
import irmb.flowsim.presentation.SimulationGraphicViewPresenter;
import irmb.flowsim.presentation.factory.*;
import irmb.flowsim.simulation.SimulationFactory;
import irmb.flowsim.simulation.SimulationFactoryImpl;
import irmb.flowsim.view.graphics.PaintableShape;

import javax.swing.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Sven on 15.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        setLookAndFeel();
        ShapeFactoryImpl paintableFactory = new ShapeFactoryImpl();
        PaintableShapeBuilderFactory builderFactory = new PaintableShapeBuilderFactoryImpl(paintableFactory, new PaintableShapeFactoryImpl());
        CommandQueue commandQueue = new CommandQueue();
        List<PaintableShape> shapeList = new LinkedList<>();
        CoordinateTransformer transformer = new CoordinateTransformerImpl();

        transformer.setWorldBounds(new Point(0, 0.5), new Point(1, 0));
        transformer.setViewBounds(new Point(0, 0), new Point(800, 600));

        MainWindow window = new MainWindow(transformer);
        MouseStrategyFactory mouseStrategyFactory = new MouseStrategyFactoryImpl(shapeList, commandQueue, window.getGraphicView(), builderFactory, transformer);
        SimulationFactory simulationFactory = new SimulationFactoryImpl();


        SimulationGraphicViewPresenter presenter = new SimulationGraphicViewPresenter(mouseStrategyFactory, commandQueue, shapeList, transformer, simulationFactory);
        window.setPresenter(presenter);
        presenter.setGraphicView(window.getGraphicView());
        window.setVisible(true);
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (ClassNotFoundException e) {
            // handle exception
        } catch (InstantiationException e) {
            // handle exception
        } catch (IllegalAccessException e) {
            // handle exception
        }
    }
}
