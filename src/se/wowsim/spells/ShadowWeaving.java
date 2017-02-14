package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.spells.types.DamageIncrease;
import se.wowsim.spells.types.Spell;

public class ShadowWeaving extends DamageIncrease {

    private double chanceToApply;
    private int points;

    public ShadowWeaving(int points) {
        super(5, 150, 0.03);
        this.points = points;
        this.school = Spell.School.SHADOW;
        chanceToApply = points * 0.2;
    }

    public int getPoints() {
        return points;
    }

    public double getChanceToApply() {
        return chanceToApply;
    }

    @Override
    public void update(Target target) {
        decrementDuration(target);
    }

    @Override
    public String getName() {
        return "Shadow Weaving";
    }
}
