package se.wowsim.spells;

import se.wowsim.Target;

public class Spell {
	
	protected int castTime;
	
	public int getCastTime() {
		return castTime;
	}
	
	public void applySpell() {}

	public void setTarget(Target target) {}

    public String getName() {return "";}
}
