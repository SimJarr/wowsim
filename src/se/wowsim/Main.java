package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Warlock;

public class Main {
	public static void main(String[] args) {
		
		
		Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 30).getClassInstance();
		Target target = new Target();

		Simulator.simulate(warlock, target, 200);



	}
}
