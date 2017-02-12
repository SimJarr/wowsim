package se.wowsim.talents;

import se.wowsim.classes.ClassTemplate;
import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

import java.util.Map;

public class TalentTreeSpecialization {

    protected Classes spellClass;
    protected ClassTemplate classTemplate;

    public TalentTreeSpecialization() {

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
        classTemplate.getSpells().remove(spell);
    }

    void increaseSchoolDamage(Spell.School school, double amount) {
        for(Map.Entry<Spell.School, Double> entry : classTemplate.getSchoolAmp().entrySet()) {
            if(entry.getKey() == school) {
                classTemplate.getSchoolAmp().put(entry.getKey(), entry.getValue() + amount);
            }
        }
    }
}
