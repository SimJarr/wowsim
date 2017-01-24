package se.wowsim.spells;

public abstract class DamageOverTime extends Spell {

    protected int duration;
    protected int maxDuration;
    protected int totalDamage;
    protected int tickInterval;
	
	public DamageOverTime(int rank) {
		super(rank);
	}
	
	public int getDuration() {
		return duration;
	}
	
	public int getMaxDuration() {
		return maxDuration;
	}
	
	public int getTotalDamage() {
		return totalDamage;
	}
	
	public int getTickInterval() {
		return tickInterval;
	}

	public abstract void init();

	public double calculateDotDamage(int timeLeft) {
		int timeAfterCast = (this.getCastTime() <= 15) ? timeLeft - 15 : timeLeft - this.getCastTime();
        double damage = 0;
		for (int i = 1; i < timeAfterCast; i++) {
			if (i % this.getTickInterval() == 0) {
				damage += this.getTotalDamage() / (this.getMaxDuration() / this.getTickInterval());
			}
		}
		return damage;
	}
}
