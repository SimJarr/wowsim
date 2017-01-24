package se.wowsim.spells;

import se.wowsim.Target;
import se.wowsim.classes.Classes;

public abstract class Spell {
	
	protected int castTime;
	protected Classes spellClass;
	protected int rank;
	
	public Spell(int rank) {
		this.rank = rank;
	}
	
	public int getCastTime() {
		return castTime;
	}
	
	public abstract void applySpell();

	public abstract void setTarget(Target target);

	public abstract void init();

	public Classes getSpellClass() {
		return spellClass;
	}

    public abstract String getName();
}
