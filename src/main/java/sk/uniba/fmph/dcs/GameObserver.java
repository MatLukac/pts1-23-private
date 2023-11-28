package sk.uniba.fmph.dcs;

import interfaces.GameObserverInterface;
import interfaces.ObserverInterface;

import java.util.ArrayList;
import java.util.List;

public final class GameObserver implements GameObserverInterface {
    private List<ObserverInterface> observers;

    public GameObserver() {

        observers = new ArrayList<>();
    }


    public void registerObserver(final ObserverInterface observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void cancelObserver(final ObserverInterface observer) {
        observers.remove(observer);
    }


    @Override
    public void notifyEverybody(final String newState) {
        for (ObserverInterface observer : observers) {
            observer.notify(newState);
        }
    }
}
