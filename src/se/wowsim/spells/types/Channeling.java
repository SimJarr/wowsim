package se.wowsim.spells.types;

public abstract class Channeling extends DamageOverTime {

	//TODO be able to stop channeling

	public Channeling(int rank) {
		super(rank);
		castTime = 0;
	}
	
	
}
