package se.wowsim;

import se.wowsim.spells.Corruption;

public class Main {
	public static void main(String[] args) {
		
		Target target = new Target();
		Corruption corruption = new Corruption(target);
		
		for(int i = 1; i <= 180; i ++) {
			if(i % 10 == 0) { System.out.println("second: " + i/10); }
			target.notifyObservers();
		}
	}
}
