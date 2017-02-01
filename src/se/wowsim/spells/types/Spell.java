package se.wowsim.spells.types;

import se.wowsim.Target;
import se.wowsim.classes.Classes;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Spell {

    protected int castTime;
    protected int rank;
    protected int cooldown;
    protected int maxCooldown;
    protected Classes spellClass;
    protected double totalDamage;
    protected Target target;

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

    public int getTimeTakenFromCaster() {
        return (this.getCastTime() < GLOBAL_COOLDOWN) ? GLOBAL_COOLDOWN : this.getCastTime();
    }

    public abstract void applySpell();

    public void setTarget(Target target) {
        this.target = target;
    }

    public abstract void init();

    public Classes getSpellClass() {
        return spellClass;
    }

    public abstract String getName();

    public void decrementCooldown() {
        if (cooldown > 0) { cooldown--; }
    }

    public double calculateDamageDealt(Target target, int timeLeft) {
        if (cooldown > 0) {
            return 0.0;
        }
        if (this.getCastTime() > timeLeft) {
            return 0.0;
        }
        if (this instanceof DamageOverTime) {
            if (target.notAffected((DamageOverTime) this)) {
                return ((DamageOverTime) this).calculateDotDamage(timeLeft);
            }
        } else if (this instanceof DirectDamage) {
            return this.getTotalDamage();
        }
        return 0.0;
    }

}
