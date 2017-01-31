package se.wowsim.spells.types;

public abstract class Channeling extends Spell {

    protected int duration;
    protected int maxDuration;
    protected int tickInterval;
    protected int tickNumber;
    protected int totalTickNumber;
	
	public Channeling(int rank) {
		super(rank);
		castTime = 0;
	}
	
    public int getDuration() {
        return duration;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int getTickInterval() {
        return tickInterval;
    }
	
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
