package se.wowsim.spells;

public class DamageOverTime extends Spell {
	
	protected int duration;
	protected int maxDuration;
	protected int totalDamage;
	protected int tickInterval;
	
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

	public double getDotDamage(int timeLeft) {
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
