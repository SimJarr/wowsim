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

    /**
     * apply every method in the Shadow tree
     */
    @Override
    void applyTalents() {
        improvedShadowWordPain(talentTree[1][1]);
        improvedMindBlast(talentTree[2][1]);
        mindFlay(talentTree[2][2]);
        shadowWeaving(talentTree[3][3]);
        darkness(talentTree[5][2]);
        shadowForm(talentTree[6][1]);
    }

    /**
     * improved Mind Blast's maxCooldown gets reduced by 5 decisec per talent point spent
     * @param pointsSpent amount of talent point spent
     */
    private void improvedMindBlast(int pointsSpent) {
        if (pointsSpent <= 0) {
            return;
        }
        Spell affectedSpell = getSpell("Mind Blast");
        reduceCooldown(affectedSpell, pointsSpent * 5);
    }

    /**
     * improved Shadow Word: Pain's duration gets increased by 30 decisec per talent point spent
     * @param pointsSpent amount of talent point spent
     */
    private void improvedShadowWordPain(int pointsSpent) {
        if (pointsSpent <= 0) {
            return;
        }
        Spell affectedSpell = getSpell("Shadow Word: Pain");
        increaseDuration((DamageOverTime) affectedSpell, pointsSpent * 30);
    }

    /**
     * if the required talent point is missing this will unlearn MindFlay from the Spell book
     * @param pointsSpent amount of talent point spent
     */
    private void mindFlay(int pointsSpent) {
        if (pointsSpent != 1) {
            unlearnSpell("Mind Flay");
        }
    }

    /**
     * shadowWeaving will be set inside the class, 20% chance per talent point spent
     * @param pointsSpent amount of talent point spent
     */
    private void shadowWeaving(int pointsSpent) {
        ((Priest) classTemplate).setShadowWeaving(pointsSpent);
    }

    /**
     * darkness increases the shadow damage dealt by 2% per point spent
     * @param pointsSpent amount of talent point spent
     */
    private void darkness(int pointsSpent) {
        increaseSchoolDamage(Spell.School.SHADOW, pointsSpent * 0.02);
    }

    /**
     * shadowForm increases the shadow damage dealt by 15%
     * @param pointsSpent amount of talent point spent (0 or 1)
     */
    private void shadowForm(int pointsSpent) {
        increaseSchoolDamage(Spell.School.SHADOW, pointsSpent * 0.15);
    }
}