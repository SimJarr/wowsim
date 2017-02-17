package se.wowsim.talents;

import se.wowsim.classes.ClassTemplate;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

abstract class TalentTreeSpecialization {

    protected Classes spellClass;
    int[][] talentTree;
    ClassTemplate classTemplate;

    TalentTreeSpecialization(ClassTemplate classTemplate, String talentFile) {
        this.classTemplate = classTemplate;
        this.talentTree = new TalentParser("talents", talentFile).getTalents();
    }

    abstract void applyTalents();

    public ClassTemplate getMyClass() {
        return classTemplate;
    }

    Spell getSpell(String spellName) {
        return classTemplate.getSpells().get(spellName);
    }

    void reduceCooldown(Spell spell, int amount) {
        spell.setMaxCooldown(spell.getMaxCooldown() - amount);
    }

    void increaseDuration(DamageOverTime dot, int amount) {
        double newBaseDamage = dot.getBaseDamage() + ((dot.getBaseDamage() / dot.getTotalTickNumber()) * (amount / dot.getTickInterval()));
        dot.setBaseDamage(newBaseDamage);
        dot.setTotalDamage(newBaseDamage);
        dot.setMaxDuration(dot.getMaxDuration() + amount);
        dot.setTotalTickNumber(dot.getMaxDuration() / dot.getTickInterval());
    }

    void unlearnSpell(String spell) {
        List<String> keysToRemove = new ArrayList<>();
        for (Map.Entry<String, Spell> entry : classTemplate.getSpells().entrySet()){
            if (entry.getValue().getName().equals(spell)){
                keysToRemove.add(entry.getKey());
            }
        }
        for (String s : keysToRemove){
            classTemplate.getSpells().remove(s);
        }
    }

    void increaseSchoolDamage(Spell.School school, double amount) {
        for(Map.Entry<Spell.School, Double> entry : classTemplate.getSchoolAmp().entrySet()) {
            if(entry.getKey() == school) {
                classTemplate.getSchoolAmp().put(entry.getKey(), entry.getValue() + amount);
            }
        }
    }
}
