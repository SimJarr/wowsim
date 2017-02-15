package se.wowsim.spells.types;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Channeling extends DamageOverTime {

    protected int channelTime;

    public Channeling(int rank) {
        super(rank);
        castTime = 0;
    }

    @Override
    public int getTimeTakenFromCaster() {
        return (this.channelTime < GLOBAL_COOLDOWN) ? GLOBAL_COOLDOWN : this.channelTime;
    }

    public void setChannelTime(int channelTime) {
        this.channelTime = channelTime;
    }

    public int getChannelTime() {
        return channelTime;
    }

    @Override
    public void setTotalDamage(double totalDamage) {
        this.totalDamage = ((totalDamage / (maxDuration / tickInterval)) * (channelTime / 10));
    }


    public double calculateChannelingDamage(int timeLeft) {

        if (cooldown > 0) return 0.0;

        double damage = 0;

        int timeAfterCast = timeLeft > channelTime ? channelTime : timeLeft;

        for (int i = 1; i <= timeAfterCast; i++) {
            if (i % this.getTickInterval() == 0) {
                damage += this.getTotalDamage() / this.getTotalTickNumber();
            }
        }
        return damage;
    }


}
