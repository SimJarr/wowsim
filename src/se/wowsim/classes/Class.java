package se.wowsim.classes;

import java.util.List;

import se.wowsim.spells.Spell;

public abstract class Class {
	
	protected List<Spell> spells;
	
	protected void addSpell(Spell spell) {
		spells.add(spell);
	}
}
