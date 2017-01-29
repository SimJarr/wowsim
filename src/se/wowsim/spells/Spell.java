package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.classes.Classes;

public abstract class Spell {

    protected int castTime;
    protected Classes spellClass;
    protected int rank;

    public Spell(int rank) {
        this.rank = rank;
    }

    public int getCastTime() {
        return castTime;
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
        int globalCooldown = 15;
        if(this.getCastTime() > timeLeft) {
        	return 0.0;
        }
        if (this instanceof DamageOverTime) {
            if (target.notAffected((DamageOverTime) this) && this.getCastTime() >= globalCooldown) {
                return ((DamageOverTime) this).calculateDotDamage(timeLeft) / this.getCastTime();
            } else if (target.notAffected((DamageOverTime) this)) {
                return ((DamageOverTime) this).calculateDotDamage(timeLeft) / globalCooldown;
            }
        } else if (this instanceof DirectDamage) {
            if (this.getCastTime() >= globalCooldown) {
                return ((DirectDamage) this).getTotalDamage() / this.getCastTime();
            } else {
                return ((DirectDamage) this).getTotalDamage() / globalCooldown;
            }
        }
        return 0.0;
    }

}
