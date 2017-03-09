package se.wowsim.classes;

/**
 * Object to send around more than one result from a calculation
 */
public class WaitTimeAndDamageDiff {

    private int waitTime;
    private double damageDiff;
    private String spellName;

    public WaitTimeAndDamageDiff(int waitTime, double damageDiff, String spellName) {
        this.waitTime = waitTime;
        this.damageDiff = damageDiff;
        this.spellName = spellName;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public double getDamageDiff() {
        return damageDiff;
    }

    public String getSpellName() {
        return spellName;
    }
}
