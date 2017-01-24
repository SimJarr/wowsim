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

        double damage = 0;
		int timeAfterCast = (this.getCastTime() <= 15) ? timeLeft - 15 : timeLeft - this.getCastTime();
        int stopLoop = timeAfterCast > this.getMaxDuration() ? this.getMaxDuration() : timeAfterCast;
		for (int i = 1; i <= stopLoop; i++) {
			if (i % this.getTickInterval() == 0) {
				damage += this.getTotalDamage() / (this.getMaxDuration() / this.getTickInterval());
			}
		}
		return damage;
	}
}
