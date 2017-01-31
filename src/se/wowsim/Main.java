package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.classes.Warlock;

public class Main {
    public static void main(String[] args) {


        //Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 36, 100).getClassInstance();
        Priest snarre = (Priest) new ClassBuilder(Classes.PRIEST, 31, 100).getClassInstance();
        Target target = new Target();

        Simulator.simulate(snarre, target, 240);


    }
}
