package se.wowsim.animation;

import se.wowsim.Observer;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class Subject implements se.wowsim.Subject {

    private Set<Observer> observers;

    public Subject() {
        this.observers = new CopyOnWriteArraySet<>();
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}
