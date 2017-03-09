package se.wowsim.spells.types;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.classes.Classes;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Spell implements Observer, Cloneable {

    protected int castTime;
    protected int rank;
    protected int cooldown;
    protected int maxCooldown;
    protected Classes spellClass;
    protected double totalDamage;
    protected double baseDamage;
    protected double damageDoneDuringSim;
    protected Target target;
    protected School school;

    public Spell(int rank) {
        this.rank = rank;
    }

    public int getMaxCooldown() {
        return maxCooldown;
    }

    public School getSchool() {
        return school;
    }

    public void setMaxCooldown(int maxCooldown) {
        this.maxCooldown = maxCooldown;
    }

    public int getCooldown() {
        return cooldown;
    }

    public int getCastTime() {
        return castTime;
    }

    public double getTotalDamage() {
        return totalDamage;
    }

    public double getDamageDoneDuringSim() {
        return damageDoneDuringSim;
    }

    public double getBaseDamage() {
        return baseDamage;
    }

    public void setBaseDamage(double baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setTotalDamage(double totalDamage) {
        this.totalDamage = totalDamage;
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

    public Spell clone() throws CloneNotSupportedException {
        return (Spell) super.clone();
    }

    /**
     * reduce the cooldown of current Spell by one decisecond
     */
    public void decrementCooldown() {
        if (cooldown > 0) {
            cooldown--;
        }
    }

    /**
     * calculates the damage a Spell will do given a target and a time
     * will also keep in mind what type of Spell it is and address accordingly
     * @param target the target
     * @param timeLeft the time we have to work with
     * @return damage the Channeling Spell will do
     */
    public double calculateDamageDealt(Target target, int timeLeft) {
        if (cooldown > 0) {
            return 0.0;
        }
        if (this.getCastTime() > timeLeft) {
            return 0.0;
        }
        if (this instanceof DirectDamage) {
            return this.getTotalDamage();
        } else if (this instanceof Channeling) {
            if (target.notAffected((DamageOverTime) this)) {
                return ((Channeling) this).calculateChannelingDamage(timeLeft);
            }
        } else if (this instanceof DamageOverTime) {
            if (target.notAffected((DamageOverTime) this)) {
                return ((DamageOverTime) this).calculateDotDamage(timeLeft);
            }
        }
        return 0.0;
    }

    public enum School {
        ARCANE,
        FIRE,
        FROST,
        HOLY,
        NATURE,
        SHADOW
    }
}
