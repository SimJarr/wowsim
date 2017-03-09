package se.wowsim.spells.types;

public abstract class DamageOverTime extends Spell {

    protected int duration;
    protected int maxDuration;
    protected int tickInterval;
    protected int tickNumber;
    protected int totalTickNumber;
    protected boolean oneMoreTick;

    public DamageOverTime(int rank) {
        super(rank);
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

    public int getTotalTickNumber() {
        return totalTickNumber;
    }

    public void setTotalTickNumber(int totalTickNumber) {
        this.totalTickNumber = totalTickNumber;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public double calculateDotDamage(int timeLeft) {

        if (cooldown > 0) return 0.0;

        double damage = 0;
        int timeAfterCast = timeLeft - this.getCastTime();
        int stopLoop = timeAfterCast > this.getMaxDuration() ? this.getMaxDuration() : timeAfterCast;
        for (int i = 1; i <= stopLoop; i++) {
            if (i % this.getTickInterval() == 0) {
                damage += this.getTotalDamage() / (this.getMaxDuration() / this.getTickInterval());
            }
        }
        return damage;
    }

    public void applySpell() {
        if (duration == 0 && target.getObservers().contains(this)) {
            oneMoreTick = true;
        }
        this.duration = this instanceof Channeling ? ((Channeling) this).channelTime : this.maxDuration;
        this.tickNumber = 1;
        target.register(this);
    }

    public void update() {
        boolean letFirstTickOfChannel = true;
        if (this instanceof Channeling && duration == ((Channeling) this).channelTime) {
            letFirstTickOfChannel = false;
        }

        if (duration % tickInterval == 0 && duration != maxDuration && letFirstTickOfChannel) {
            System.out.println(getName() + " tick(" + tickNumber + "/" + totalTickNumber + "): "
                    + (int) (totalDamage / totalTickNumber) + " damage");
            tickNumber++;
            damageDoneDuringSim += (totalDamage / totalTickNumber);
        }

        if (oneMoreTick) {
            oneMoreTick = false;
            System.out.println(getName() + " tick(" + totalTickNumber + "/" + totalTickNumber + "): "
                    + (int) (totalDamage / totalTickNumber) + " damage");
            damageDoneDuringSim += (totalDamage / totalTickNumber);
        }

        if (duration == 0) {
            oneMoreTick = false;
            target.unregister(this);
            return;
        }
        this.duration--;
    }
}
