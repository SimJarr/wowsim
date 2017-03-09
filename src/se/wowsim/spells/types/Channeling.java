package se.wowsim.spells.types;

import static se.wowsim.classes.GeneralRules.GLOBAL_COOLDOWN;

public abstract class Channeling extends DamageOverTime {

    /**
     * The time it takes for the Spell to finish
     */
    protected int channelTime;

    /**
     * All Channeling Spells have 0 castTime
     * @param rank the level of the Spell
     */
    public Channeling(int rank) {
        super(rank);
        castTime = 0;
    }

    /**
     * When calculating the time it takes from the Caster to use this Spell,
     * a Channeling Spell will return it's channelTime instead of the castTime.
     * @return the highest out of GLOBAL_COOLDOWN and this Spell's channelTime
     */
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

    /**
     * When setting the TotalDamage for a Channeling Spell we first break it down to how much damage
     * each tick will do with the new given totalDamage, then multiply it for the full channelTime
     * @param totalDamage the new totalDamage to set
     */
    @Override
    public void setTotalDamage(double totalDamage) {
        this.totalDamage = ((totalDamage / (maxDuration / tickInterval)) * (channelTime / 10));
    }

    /**
     * Calculates the damage a Channeling Spell can do given a time
     * @param timeLeft the time we have to work with
     * @return damage the Channeling Spell will do
     */
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
