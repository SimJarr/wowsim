package se.jalo.wowsim;

import java.util.ArrayList;
import java.util.List;

public class Target implements Subject {

	private List<Observer> observers;
	
	public Target() {
		this.observers = new ArrayList<>();
	}
	
	@Override
	public void register(Observer o) {
		observers.add(o);
		System.out.println(o.toString() + " applied to target");
	}

	@Override
	public void unregister(Observer o) {
		int index = observers.indexOf(o);
		observers.remove(index);		
	}

	@Override
	public void notifyObservers() {
		for(Observer o : observers) {
			o.update();
		}
	}
}
