package se.wowsim.spells.types;

public abstract class Channeling extends DamageOverTime {

	public Channeling(int rank) {
		super(rank);
		castTime = 0;
	}
	
	
}
