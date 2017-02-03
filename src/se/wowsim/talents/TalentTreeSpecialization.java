package se.wowsim.talents;

import se.wowsim.classes.Classes;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.Spell;

public class TalentTreeSpecialization {

    protected Classes spellClass;

    public TalentTreeSpecialization() {

    }

    void reduceCooldown(Spell spell, int amount){
        spell.setMaxCooldown(spell.getMaxCooldown() - amount);
    }

    void increaseDuration(DamageOverTime dot, int amount){
        double newTotalDamage = dot.getTotalDamage() + ((dot.getTotalDamage() / dot.getTotalTickNumber()) * (amount / dot.getTickInterval()));
        dot.setMaxDuration(dot.getMaxDuration() + amount);
        dot.setTotalDamage(newTotalDamage);
    }

    void addSpell(Spell spell){
        //TODO figure out a way to add a spell
    }

    void increaseSchoolDamage(String school, double amount){
        //TODO
    }
}
