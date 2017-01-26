package irmb.flowsim.util;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by sven on 26.01.17.
 */
public abstract class Observable<T> {
    private List<Observer<T>> observers = new LinkedList<>();

    public void addObserver(Observer<T> observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer<T> observer) {
        observers.remove(observer);
    }

    protected void notifyObservers(T args) {
        for (Observer<T> observer : observers)
            observer.update(this, args);
    }
}
