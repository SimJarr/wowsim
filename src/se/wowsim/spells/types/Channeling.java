package se.wowsim.spells.types;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Channeling extends DamageOverTime {

    private int channelTime;

    public Channeling(int rank) {
        super(rank);
        castTime = 0;
        channelTime = this.getMaxDuration();
    }

    @Override
    public int getTimeTakenFromCaster() {
        return (this.channelTime < GLOBAL_COOLDOWN) ? GLOBAL_COOLDOWN : this.channelTime;
    }

    public void setChannelTime(int channelTime) {
        this.totalDamage = (this.totalDamage / this.maxDuration) * channelTime;
        this.channelTime = channelTime;
        this.maxDuration = channelTime;
        this.duration = channelTime;
        this.totalTickNumber = channelTime / tickInterval;

    }

    public double calculateChannelingDamage(int timeLeft, int channelDuration) {

        if (cooldown > 0) return 0.0;

        double damage = 0;

        int timeAfterCast = timeLeft > this.getMaxDuration() ? this.getMaxDuration() : timeLeft;
        int stopLoop = timeAfterCast > channelDuration ? channelDuration : timeAfterCast;

        for (int i = 1; i <= stopLoop; i++) {
            if (i % this.getTickInterval() == 0) {
                damage += this.getTotalDamage() / (this.getMaxDuration() / this.getTickInterval());
            }
        }
        return damage;
    }


}
