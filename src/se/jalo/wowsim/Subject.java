package se.jalo.wowsim;

public interface Subject {

	void register(Observer o);
	void unregister(Observer o);
	void notifyObservers();
	
}
