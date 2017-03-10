package irmb.flowsim.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sven on 26.01.17.
 */
public abstract class ObservableImpl<T> implements Observable<T> {
    private final List<Observer<T>> observers = new LinkedList<>();

    @Override
    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    public void notifyObservers(T args) {
        for (Observer<T> observer : observers)
            observer.update(args);
    }
}
