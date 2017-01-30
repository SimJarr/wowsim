package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.classes.Classes;

public abstract class Spell {

    protected int castTime;
    protected Classes spellClass;
    protected int rank;
    protected double totalDamage;

    public Spell(int rank) {
        this.rank = rank;
    }

    public int getCastTime() {
        return castTime;
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    public int getRank() {
        return rank;
    }

    public abstract void applySpell();

    public abstract void setTarget(Target target);

    public abstract void init();

    public Classes getSpellClass() {
        return spellClass;
    }

    public abstract String getName();

    public double calculateDamageDealt(Target target, int timeLeft) {
        if (this.getCastTime() > timeLeft) {
            return 0.0;
        }
        if (this instanceof DamageOverTime) {
            if (target.notAffected((DamageOverTime) this)) {
                return ((DamageOverTime) this).calculateDotDamage(timeLeft);
            }
        } else if (this instanceof DirectDamage) {

            return ((DirectDamage) this).getTotalDamage();
        }
        return 0.0;
    }

}
