package se.wowsim.spells.types;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Channeling extends DamageOverTime {

    private int temporaryChannelTime;

    public Channeling(int rank) {
        super(rank);
        castTime = 0;
        temporaryChannelTime = this.getMaxDuration();
    }

    @Override
    public int getTimeTakenFromCaster() {
        return (this.temporaryChannelTime < GLOBAL_COOLDOWN) ? GLOBAL_COOLDOWN : this.temporaryChannelTime;
    }

    public void setTemporaryChannelTime(int temporaryChannelTime) {
        temporaryChannelTime = (temporaryChannelTime == 15) ? 10 : temporaryChannelTime;
        this.totalDamage = (this.totalDamage / this.maxDuration) * temporaryChannelTime;
        this.temporaryChannelTime = temporaryChannelTime;
        this.maxDuration = temporaryChannelTime;
        this.duration = temporaryChannelTime;
        this.totalTickNumber = temporaryChannelTime / tickInterval;

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
