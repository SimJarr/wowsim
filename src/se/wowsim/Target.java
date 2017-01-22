package se.wowsim;

import se.wowsim.spells.DamageOverTime;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Target implements Subject {

    private List<Observer> observers;

    public Target() {
        this.observers = new CopyOnWriteArrayList<>();
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
        if(o instanceof DamageOverTime)
            System.out.println(o.getName() + " applied to target");
    }

    @Override
    public void unregister(Observer o) {
        int index = observers.indexOf(o);
        observers.remove(index);
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
}