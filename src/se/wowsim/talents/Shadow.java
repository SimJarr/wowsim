package se.wowsim.talents;

import se.wowsim.classes.ClassTemplate;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

public class Shadow extends TalentTreeSpecialization {

    private int[][] talentTree;

    public Shadow(ClassTemplate classTemplate, String talentFile) {
        this.classTemplate = classTemplate;
        this.spellClass = Classes.PRIEST;
        this.talentTree = new TalentParser("talents", talentFile).getTalents();
        runMethods();
    }

    public ClassTemplate getMyClass() {
        return classTemplate;
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

    private Spell getSpell(String spellName) {
        return classTemplate.getSpells().get(spellName);
    }

    private void runMethods() {
        improvedShadowWordPain(talentTree[1][1]);
        improvedMindBlast(talentTree[2][1]);
        mindFlay(talentTree[2][2]);
        shadowWeaving(talentTree[3][3]);
        darkness(talentTree[5][2]);
        shadowForm(talentTree[6][1]);
    }
}