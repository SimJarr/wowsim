package se.wowsim.spells;

public class DirectDamage extends Spell {

	public DirectDamage(int rank) {
		super(rank);
	}

	protected double totalDamage;
	
	public double getTotalDamage() {
		return totalDamage;
	}
}
