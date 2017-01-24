package se.wowsim;

import se.wowsim.classes.Warlock;

public class Main {
	public static void main(String[] args) {
		
		Warlock warlock = new Warlock(28);
		Target target = new Target();

		int simDuration = 138;
		for(int i = 0; i <= simDuration; i ++) {
            System.out.println("decisecond: " + i);
			warlock.currentActivity(target, simDuration-i);
			target.notifyObservers();

		}
	}
}
