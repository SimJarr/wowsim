package se.wowsim.talents;

import se.wowsim.classes.ClassTemplate;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

public class Shadow extends TalentTreeSpecialization {

    public Shadow(ClassTemplate classTemplate, String talentFile) {
        super(classTemplate, talentFile);
        this.spellClass = Classes.PRIEST;
        applyTalents();
    }

    @Override
    void applyTalents() {
        improvedShadowWordPain(talentTree[1][1]);
        improvedMindBlast(talentTree[2][1]);
        mindFlay(talentTree[2][2]);
        shadowWeaving(talentTree[3][3]);
        darkness(talentTree[5][2]);
        shadowForm(talentTree[6][1]);
    }

    private void improvedMindBlast(int pointsSpent) {
        if (pointsSpent <= 0) {
            return;
        }
        Spell affectedSpell = getSpell("Mind Blast");
        reduceCooldown(affectedSpell, pointsSpent * 5);
    }

    private void improvedShadowWordPain(int pointsSpent) {
        if (pointsSpent <= 0) {
            return;
        }
        Spell affectedSpell = getSpell("Shadow Word: Pain");
        increaseDuration((DamageOverTime) affectedSpell, pointsSpent * 30);
    }

    private void mindFlay(int pointsSpent) {
        if (pointsSpent != 1) {
            unlearnSpell("Mind Flay");
        }
    }

    private void shadowWeaving(int pointsSpent) {
        ((Priest) classTemplate).setShadowWeaving(pointsSpent);
    }

    private void darkness(int pointsSpent) {
        increaseSchoolDamage(Spell.School.SHADOW, pointsSpent * 0.02);
    }

    private void shadowForm(int pointsSpent) {
        increaseSchoolDamage(Spell.School.SHADOW, pointsSpent * 0.15);
    }
}