package se.wowsim.talents;

import se.wowsim.classes.ClassBuilder;
import se.wowsim.classes.ClassTemplate;
import se.wowsim.classes.Classes;
import se.wowsim.classes.Priest;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

import java.util.Map;

public class Shadow extends TalentTreeSpecialization {

    private int[][] talentTree;
    private Map<String, Spell> spells;
    private ClassTemplate classTemplate;

    public Shadow(ClassTemplate classTemplate, String talentFile) {
        this.classTemplate = classTemplate;
        this.spells = classTemplate.getSpells();
        this.spellClass = Classes.PRIEST;
        this.talentTree = new TalentParser("talents", talentFile).getTalents();
        runMethods();
    }

    //for testing
    public Map<String, Spell> getSpells() {
        return spells;
    }

    public ClassTemplate getMyClass(){
        return classTemplate;
    }

    private void improvedMindBlast(int pointsSpent) {
        Spell affectedSpell = spells.get("Mind Blast");
        reduceCooldown(affectedSpell, pointsSpent * 5);
    }

    private void improvedShadowWordPain(int pointsSpent) {
        Spell affectedSpell = spells.get("Shadow Word: Pain");
        increaseDuration((DamageOverTime) affectedSpell, pointsSpent * 30);
    }

    private void mindFlay(int pointsSpent) {
        if (pointsSpent == 1) {
            //TODO figure out a way to add a spell
            Spell affectedSpell = spells.get("Mind Flay");
            addSpell(affectedSpell);
        }
    }

    private void shadowWeaving(int pointsSpent) {
        //TODO implement debuffs
        //learnDebuff(spell, pointsSpent * 0.2);
    }

    private void darkness(int pointsSpent) {
        increaseSchoolDamage("Shadow", pointsSpent * 0.02);
    }

    private void shadowForm(int pointsSpent) {
        increaseSchoolDamage("Shadow", pointsSpent * 0.15);
    }

    private Map<String, Spell> getSpellsFromCaster() {
        // TEST
        Priest snarre = (Priest) new ClassBuilder(Classes.PRIEST, 31, 100).getClassInstance();

        return snarre.getSpells();
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