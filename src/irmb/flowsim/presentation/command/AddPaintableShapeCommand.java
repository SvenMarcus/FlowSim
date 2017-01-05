package irmb.flowsim.presentation.command;

import irmb.flowsim.model.Shape;
import irmb.flowsim.view.graphics.PaintableShape;

import java.util.List;

/**
 * Created by Sven on 03.01.2017.
 */
public class AddPaintableShapeCommand implements Command {

    private PaintableShape shape;
    private List<PaintableShape> paintableShapeList;

    public AddPaintableShapeCommand(PaintableShape shape, List<PaintableShape> paintableShapeList) {
        this.shape = shape;
        this.paintableShapeList = paintableShapeList;
    }

    @Override
    public void execute() {
        if (!paintableShapeList.contains(shape))
            paintableShapeList.add(shape);
    }

    @Override
    public void undo() {
        paintableShapeList.remove(shape);
    }

    @Override
    public void redo() {
        execute();
    }
}
