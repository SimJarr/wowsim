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
}
