package se.jalo.wowsim;

import se.jalo.wowsim.spells.Corruption;

public class Main {
	public static void main(String[] args) {
		
		Target target = new Target();
		Corruption corruption = new Corruption(target);
		
		for(int i = 0; i <= 180; i ++) {
			System.out.println("decisecond: " + i);
			target.notifyObservers();
		}
	}
}
