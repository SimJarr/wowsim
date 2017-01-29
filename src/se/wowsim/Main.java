package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.classes.Warlock;

public class Main {
    public static void main(String[] args) {


        Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 32, 89).getClassInstance();
        //Priest snarre = (Priest) new ClassBuilder(Classes.PRIEST, 31, 10000).getClassInstance();
        Target target = new Target();

        Simulator.simulate(warlock, target, 445);

        System.out.println("Total Damage Done: " + Simulator.getTotalDamageDone());
        System.out.println("Dps: " + Simulator.getDps());
    }
}
