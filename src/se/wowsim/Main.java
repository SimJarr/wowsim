package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Warlock;

public class Main {
	public static void main(String[] args) {
		
		
		Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 28).getClassInstance();
		Target target = new Target();

		int simDuration = 500;
		for(int i = 0; i <= simDuration; i ++) {
            System.out.println("decisecond: " + i);
			warlock.currentActivity(target, simDuration-i);
			target.notifyObservers();

		}
	}
}
