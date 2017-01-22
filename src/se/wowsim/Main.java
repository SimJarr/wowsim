package se.wowsim;

import se.wowsim.classes.Warlock;
import se.wowsim.spells.Corruption;
import se.wowsim.spells.Shadowbolt;

public class Main {
	public static void main(String[] args) {
		
		Warlock warlock = new Warlock();
		
		
		
		Target target = new Target();
		new Corruption(target, 3);
		new Shadowbolt(target, 5);
		
		for(int i = 1; i <= 180; i ++) {
			if(i % 10 == 0) { System.out.println("second: " + i/10); }
			target.notifyObservers();
		}
	}
}
