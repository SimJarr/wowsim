package se.jalo.wowsim.classes;

import java.util.List;

import se.jalo.wowsim.spells.Spell;

public class Class {
	
	protected List<Spell> spells;
	
	protected void addSpell(Spell spell) {
		spells.add(spell);
	}
}
