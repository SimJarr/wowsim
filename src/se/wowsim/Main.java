package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.classes.Warlock;

public class Main {
	public static void main(String[] args) {
		

		//Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 32, 95).getClassInstance();
        Priest jarre = (Priest) new ClassBuilder(Classes.PRIEST, 31, 1000).getClassInstance();
		Target target = new Target();

        Simulator.simulate(jarre, target, 400);

        System.out.println("Total Damage Done: " + Simulator.getTotalDamageDone());
        System.out.println("Dps: " + Simulator.getDps());
	}
}
