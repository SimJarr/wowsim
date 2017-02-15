package se.wowsim.classes;

import se.wowsim.Target;
import se.wowsim.spells.ShadowWeaving;
import se.wowsim.spells.types.Spell;

public final class Priest extends ClassTemplate {

    private ShadowWeaving shadowWeaving;

    public Priest(int intellect, int level) {
        super(intellect, level);
        this.myClass = Classes.PRIEST;
        this.critChance = myClass.calculateCritChance(level, intellect);
    }

    public void setShadowWeaving(int points) {
        this.shadowWeaving = new ShadowWeaving(points);
    }

    @Override
    public void applyDamageIncrease(Spell spell, Target target) {
        if (spell.getSchool() == Spell.School.SHADOW) {
            if (shadowWeaving != null && shadowWeaving.getPoints() > 0) {
                if (shadowWeaving.getChanceToApply() >= Math.random())
                    target.register(shadowWeaving);
            }
        }
    }
}