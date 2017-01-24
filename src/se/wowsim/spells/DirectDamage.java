package se.wowsim.spells;

public abstract class DirectDamage extends Spell {

    protected double totalDamage;
    protected double critChance;
    protected double critMulti = 1.5;

	public DirectDamage(int rank) {
		super(rank);
	}
	
	public double getTotalDamage() {
		return totalDamage;
	}

	public void setCritChance(double critChance) {
		this.critChance = critChance;
	}

    public abstract void init();
}
