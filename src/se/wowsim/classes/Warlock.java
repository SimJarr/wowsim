package se.wowsim.classes;

import se.wowsim.Target;
import se.wowsim.spells.types.Spell;

public final class Warlock extends ClassTemplate {

    /**
     * The Warlock class
     * @param intellect base intellect
     * @param level level of the Class
     */
    public Warlock(int level, int intellect) {
        super(level, intellect);
        this.myClass = Classes.WARLOCK;
        this.critChance = myClass.calculateCritChance(level, intellect);
    }

    /**
     * Not yet implemented, but will be used to apply a damage increase debuff
     * @param spell the used Spell to check the Spell's School
     * @param target the target we attack
     */
    @Override
    public void applyDamageIncrease(Spell spell, Target target) {
        //TODO damage increases for warlock
    }

}