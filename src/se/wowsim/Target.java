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
        if(o instanceof DamageOverTime)
        	System.out.println(o.getName() + " faded");
    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }
    }
    
    public boolean notAffected(DamageOverTime dot) {
        for (Observer o : getObservers()) {
            if (o.getClass().equals(dot.getClass())) {
            	return(((DamageOverTime)o).getDuration() < dot.getCastTime());
            }
        }
        return true;
    }
    
    public List<Observer> getObservers() {
		return observers;
	}
}
