package irmb.flowsim.util;

/**
 * Created by sven on 26.01.17.
 */
public interface Observer<T> {
    void update(Observable sender, T args);
}
