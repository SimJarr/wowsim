package se.wowsim.classes;

import java.util.Map;

import se.wowsim.Target;
import se.wowsim.spells.Spell;

public abstract class Class {
	
	protected Map<String, Spell> spells;
	
	protected void addSpell(String name, Spell spell) {
		spells.put(name, spell);
	}
	
	public Map<String, Spell> getSpells() {
		return spells;
	}
	
	public abstract void determineSpell(Target target, double timeLeft);
}
