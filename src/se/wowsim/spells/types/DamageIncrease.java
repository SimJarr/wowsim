package se.wowsim.spells.types;

import se.wowsim.Target;

public abstract class DamageIncrease {

    private int stacks;
    private int currentStacks;
    private int duration;
    private int currentDuration;
    private double damageIncreasePerStack;
    public Spell.School school;

    public DamageIncrease(int stacks, int duration, double damageIncreasePerStack) {
        this.stacks = stacks;
        this.duration = duration;
        this.damageIncreasePerStack = damageIncreasePerStack;
    }

    public double getCurrentAmp() {
        return damageIncreasePerStack * currentStacks;
    }

    /**
     * reduce the duration of the DamageIncrease by one
     * @param target the target that this DamageIncrease is applied to
     */
    public void decrementDuration(Target target) {
        currentDuration--;
        if (currentDuration == 0) {
            target.unregister(this);
        }
    }

    /**
     * increasing the stacks of this DamageIncrease by one until maximum are reached
     * also refreshes the duration to maximum
     */
    public void incrementStacks() {
        if (currentStacks < stacks) {
            currentStacks++;
        }
        currentDuration = duration;
    }

    public int getCurrentStacks() {
        return currentStacks;
    }

    public abstract String getName();

    public abstract void update(Target target);
}
