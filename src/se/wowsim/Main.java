package se.wowsim;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.classes.Warlock;
import se.wowsim.spells.Corruption;
import se.wowsim.spells.CurseOfAgony;
import se.wowsim.spells.DrainLife;
import se.wowsim.spells.SiphonLife;

public class Main {
    public static void main(String[] args) {


        Warlock warlock = (Warlock) new ClassBuilder(Classes.WARLOCK, 36, 127).getClassInstance();
        Priest snarre = (Priest) new ClassBuilder(Classes.PRIEST, 31, 100).getClassInstance();
        Target target = new Target();

        //Simulator.simulate(snarre, target, 240);
        Simulator.simulate(warlock, target, 320);
    }
}
