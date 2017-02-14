package se.wowsim.classes;

import se.wowsim.Target;
import se.wowsim.spells.types.Spell;

public final class Warlock extends ClassTemplate {

    public Warlock(int level, int intellect) {
        super(level, intellect);
        this.myClass = Classes.WARLOCK;
        this.critChance = myClass.calculateCritChance(level, intellect);
    }

    @Override
    public void applyDamageIncrease(Spell spell, Target target) {
        System.out.println("warlock");
    }

}