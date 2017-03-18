package irmb.flowsim.util;

/**
 * Created by sven on 09.03.17.
 */
public interface Observable<T> {
    void addObserver(Observer<T> observer);

    void removeObserver(Observer<T> observer);

    void notifyObservers(T args);
}
