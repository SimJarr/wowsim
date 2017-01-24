package se.wowsim.classes;

import java.util.Map;

import se.wowsim.Target;
import se.wowsim.spells.Spell;

public abstract class ClassTemplate {
	
	protected Map<String, Spell> spells;
	
	protected void addSpell(String name, Spell spell) {
		spells.put(name, spell);
	}
	
	public Map<String, Spell> getSpells() {
		return spells;
	}
	
	public abstract void currentActivity(Target target, int timeLeft);
}