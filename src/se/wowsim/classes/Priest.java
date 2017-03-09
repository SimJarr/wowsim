package se.wowsim.classes;

import se.wowsim.Target;
import se.wowsim.spells.ShadowWeaving;
import se.wowsim.spells.types.Spell;

public final class Priest extends ClassTemplate {

    private ShadowWeaving shadowWeaving;

    /**
     * The Priest class
     * @param intellect base intellect
     * @param level level of the Class
     */
    public Priest(int intellect, int level) {
        super(intellect, level);
        this.myClass = Classes.PRIEST;
        this.critChance = myClass.calculateCritChance(level, intellect);
    }

    public void setShadowWeaving(int points) {
        this.shadowWeaving = new ShadowWeaving(points);
    }

    /**
     * See if current Spell will apply a shadowWeaving
     * @param spell the used Spell to check the Spell's School
     * @param target the target we attack
     */
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