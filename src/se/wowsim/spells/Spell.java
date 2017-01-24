package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.classes.Classes;

public class Spell {
	
	protected int castTime;
	protected Classes spellClass;
	protected int rank;
	
	public Spell(int rank) {
		this.rank = rank;
	}
	
	public int getCastTime() {
		return castTime;
	}
	
	public void applySpell() {}

	public void setTarget(Target target) {}

	public Classes getSpellClass() {
		return spellClass;
	}

    public String getName() {return "";}
}
