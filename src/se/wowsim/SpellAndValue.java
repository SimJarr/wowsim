package se.wowsim;

import se.wowsim.spells.types.Spell;

public class SpellAndValue {

    private Spell spell;
    private double value;

    public SpellAndValue(Spell spell, double value) {
        this.spell = spell;
        this.value = value;
    }

    public Spell getSpell() {
        return spell;
    }

    public double getValue() {
        return value;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
