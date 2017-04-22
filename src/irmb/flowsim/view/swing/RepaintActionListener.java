package irmb.flowsim.view.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by sven on 22.04.17.
 */
public class RepaintActionListener implements ActionListener{

    private Runnable runnable;
    private boolean shouldUpdate;

    public RepaintActionListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(shouldUpdate) {
            runnable.run();
            shouldUpdate = false;
        }
    }

    public void shouldUpdate(boolean update) {
        this.shouldUpdate = update;
    }
}
