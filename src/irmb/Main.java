package irmb;

import irmb.flowsim.presentation.GraphicViewPresenter;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactory;
import irmb.flowsim.presentation.factory.PaintableShapeBuilderFactoryImpl;
import irmb.flowsim.view.MainWindow;
import irmb.flowsim.view.factory.ShapeFactoryImpl;

/**
 * Created by Sven on 15.12.2016.
 */
public class Main {
    public static void main(String[] args) {
        ShapeFactoryImpl paintableFactory = new ShapeFactoryImpl();
        PaintableShapeBuilderFactory builderFactory = new PaintableShapeBuilderFactoryImpl(paintableFactory);
        GraphicViewPresenter presenter = new GraphicViewPresenter(builderFactory);
        MainWindow window = new MainWindow(presenter);
        presenter.setGraphicView(window.getGraphicView());
        window.setVisible(true);
    }
}
