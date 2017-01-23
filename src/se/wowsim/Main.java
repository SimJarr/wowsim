package se.wowsim;

import se.wowsim.classes.Warlock;
import se.wowsim.spells.Corruption;
import se.wowsim.spells.Shadowbolt;

public class Main {
	public static void main(String[] args) {
		
		Warlock warlock = new Warlock(28);
		Target target = new Target();

		int simDuration = 510;
		for(int i = 1; i <= simDuration; i ++) {
			if(i % 10 == 0) { System.out.println("second: " + i/10); }
			warlock.determineSpell(target, simDuration-i);
			target.notifyObservers();
		}
		
		
	}
}
