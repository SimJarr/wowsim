package se.wowsim.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.wowsim.Target;
import se.wowsim.spells.Corruption;
import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.DirectDamage;
import se.wowsim.spells.types.Spell;

public abstract class ClassTemplate {

    private double totalDamageDone;
    private int level;
    private int intellect;
    private int stamina;
    private int spirit;
    private List<String> usedSpells = new ArrayList<>();
    protected Map<String, Spell> spells;
    protected boolean busyCasting;
    protected int castProgress;
    protected int globalCooldown;
    protected int downTime;
    protected double critChance;
    protected double critMulti;
    protected Spell nextSpell;
    protected Classes myClass;

    protected ClassTemplate(int level, int intellect) {
        this.busyCasting = false;
        this.globalCooldown = 15;
        this.totalDamageDone = 0.0;
        this.nextSpell = null;
        this.spells = new HashMap<>();
        this.critMulti = 1.5;
        this.intellect = intellect;
        this.level = level;
    }

    public double getTotalDamageDone() {
        return totalDamageDone;
    }

    public List<String> getUsedSpells() {
        return usedSpells;
    }

    public double resetTotalDamageDone() {
        this.totalDamageDone = 0.0;
        return 0.0;
    }

    public Map<String, Spell> getSpells() {
        return spells;
    }

    protected void addSpell(String name, Spell spell) {
        spells.put(name, spell);
    }

    public void currentActivity(Target target, int timeLeft) {

        //TODO pre-casta en spell innan decisekund 0

        castProgress--;
        downTime--;
        decrementEverySpellsCooldown();

        if (nextSpell != null && castProgress <= 0) {
            nextSpell.applySpell();
            nextSpell = null;
        }
        if (downTime == 0) {
            busyCasting = false;
        }
        if (busyCasting) {
            return;
        }

        nextSpell = determineSpell(target, timeLeft);

        if (nextSpell != null) {
            nextSpell.setTarget(target);

            castProgress = nextSpell.getCastTime();
            if (nextSpell instanceof Channeling) {
                downTime = ((Channeling) nextSpell).getMaxDuration();
            } else {
                downTime = (nextSpell.getCastTime() > globalCooldown) ? nextSpell.getCastTime() : globalCooldown;
            }

            busyCasting = true;
            System.out.println("Casting " + nextSpell.getName());
            usedSpells.add(nextSpell.getName());
            if (castProgress == 0) {
                nextSpell.applySpell();
                nextSpell = null;
            }
        }
    }

    private void decrementEverySpellsCooldown() {
        for (Map.Entry<String, Spell> entry : spells.entrySet()) {
            Spell currentSpell = entry.getValue();
            currentSpell.decrementCooldown();
        }
    }

    private DamageOverTime calculateHighestDamageDot(Target target, int timeLeft) {

        DamageOverTime highestDamageDot = null;
        double highestDamageDotDps = 0.0;

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DamageOverTime && !(currentSpell instanceof Channeling)) {
                if (highestDamageDot == null && (currentSpell.calculateDamageDealt(target, timeLeft) != 0.0)) {
                    highestDamageDot = (DamageOverTime) currentSpell;
                    highestDamageDotDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                } else {
                    double currentSpellDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                    if (currentSpellDps > highestDamageDotDps) {
                        highestDamageDot = (DamageOverTime) currentSpell;
                        highestDamageDotDps = currentSpellDps;
                    }
                }
            }
        }
        return highestDamageDot;
    }

    private Spell determineSpell(Target target, int timeLeft) {

        //TODO future-sight, understand what order of spells yields the greatest ePeen

        //TODO future-sight milestone when simulating with 240 deciseconds it want to use corruption first and make agony miss one tick

        DamageOverTime highestDamageDot = calculateHighestDamageDot(target, timeLeft);

        Map<Spell, Double> result = new HashMap<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DirectDamage) {
                ((DirectDamage) currentSpell).setCritChance(this.myClass.calculateCritChance(level, intellect));
            }

            if (currentSpell instanceof DirectDamage || currentSpell instanceof Channeling || currentSpell == highestDamageDot) {
                int timeTakenFromCaster = (currentSpell.getCastTime() <= 15) ? 15 : currentSpell.getCastTime();

                result.put(currentSpell, (currentSpell.calculateDamageDealt(target, timeLeft)) / timeTakenFromCaster);
                System.out.println(currentSpell.getName() + " value: " + (currentSpell.calculateDamageDealt(target, timeLeft)) / timeTakenFromCaster);
            }

        }

        Spell determinedSpell = null;
        double highestSoFar = 0.0;

        for (Map.Entry<Spell, Double> entry : result.entrySet()) {

            if (determinedSpell == null) {
                determinedSpell = entry.getKey();
                highestSoFar = entry.getValue();
            } else {
                if (entry.getValue() > highestSoFar) {
                    determinedSpell = entry.getKey();
                    highestSoFar = entry.getValue();
                }
            }
        }

        if (determinedSpell != null && highestSoFar == 0.0) {
            determinedSpell = null;
        }

        if (determinedSpell != null) {

            if (determinedSpell.getCastTime() < globalCooldown) {
                totalDamageDone += highestSoFar * globalCooldown;
            } else {
                totalDamageDone += highestSoFar * determinedSpell.getCastTime();
            }
        }

        // only useful in extreme cases
        /*if (worthDoingNothing_old(target, determinedSpell, timeLeft)) {
            determinedSpell = null;
        }*/

        if (worthDoingNothing(target, determinedSpell, timeLeft)) {
            determinedSpell = null;
        }

        return determinedSpell;
    }

    private boolean worthDoingNothing(Target target, Spell nextCalculatedSpell, int timeLeft) {

        DamageOverTime dot = getNextDotTimeOut(target);

        if (dot != null && nextCalculatedSpell != null && dot != nextCalculatedSpell) {

            boolean testPrint = true;

            if (testPrint) System.out.println(dot.getName() + " har låg tid: " + dot.getDuration() + " decisec");

            int timeSpentOnNextSpell = (nextCalculatedSpell.getCastTime() < globalCooldown) ? globalCooldown : nextCalculatedSpell.getCastTime();

            int dotDurationIfWeCastNext = dot.getDuration() - timeSpentOnNextSpell;
            if (testPrint)
                System.out.println("om vi castar: " + nextCalculatedSpell.getName() + " så har vi bara: " + dotDurationIfWeCastNext + " decisec");

            if (dotDurationIfWeCastNext < dot.getCastTime()) {
                int timeTheDotWouldBeGone = dot.getCastTime() - dotDurationIfWeCastNext;
                if (testPrint)
                    System.out.println("då skulle " + dot.getName() + " vara nere i: " + timeTheDotWouldBeGone + " decisec");

                int timeTheDotCouldBeUp = (dot.getMaxDuration() < timeLeft) ? dot.getMaxDuration() : timeLeft;
                double damageDotWouldHaveDone = (dot.calculateDotDamage(timeLeft) / timeTheDotCouldBeUp) * timeTheDotWouldBeGone;
                if (testPrint)
                    System.out.println("på " + timeTheDotWouldBeGone + " decisec skulle " + dot.getName() + " hinna göra: " + damageDotWouldHaveDone + " damage");

                int timeTheNextSpellWouldMiss = (dot.getDuration() - dot.getCastTime());
                double damageNextSpellWouldHaveDone = ((nextCalculatedSpell.calculateDamageDealt(target, timeLeft)) / timeSpentOnNextSpell) * timeTheNextSpellWouldMiss;
                if (testPrint)
                    System.out.println("på " + timeTheNextSpellWouldMiss + " decisec skulle " + nextCalculatedSpell.getName() + " hinna göra: " + damageNextSpellWouldHaveDone + " damage");

                if (damageDotWouldHaveDone > damageNextSpellWouldHaveDone) {
                    System.out.println("HAAAAAAAAAAAAAAALTAR i: " + (dot.getDuration() - dot.getCastTime()) + " decisec");
                    return true;
                }

            }

        }
        return false;
    }


    private boolean worthDoingNothing_old(Target target, Spell nextCalculatedSpell, int timeLeft) {
        DamageOverTime dot = getNextDotTimeOut(target);
        if (dot != null && nextCalculatedSpell != null) {

            // time(in deciseconds) that dot would be down if next calculated spell is cast
            int downtime = Math.abs((dot.getDuration() - dot.getCastTime() - nextCalculatedSpell.getCastTime()));
            // damage dot would do during its downtime
            double waitValueThreshold = getWaitValueThreshold(dot, downtime, timeLeft);
            // time to afk before starting to cast dot
            int potentialAfkTime = dot.getDuration() - dot.getCastTime();

            potentialAfkTime = Math.abs(potentialAfkTime);

            if (potentialAfkTime >= nextCalculatedSpell.getCastTime()) {
                return false;
            }
            // damage next calculated spell would do during potential afk time
            double nextSpellValue = (nextCalculatedSpell.getTotalDamage() / nextCalculatedSpell.getCastTime()) * potentialAfkTime;

            if (waitValueThreshold > nextSpellValue) {
                System.out.println("=== WAITING FOR " + dot.getName() + ", dmg dot would do during downtime: " + waitValueThreshold + ", damage next calculated spell would do during potential afk time: " + nextSpellValue + ", total damage from: " + nextCalculatedSpell.getName() + " : " + nextCalculatedSpell.getTotalDamage() + ", casttime: " + nextCalculatedSpell.getCastTime() + ", afktime: " + potentialAfkTime + ", dot downtime: " + downtime);
                return true;
            }
        }
        return false;
    }

    private double getWaitValueThreshold(DamageOverTime dot, int downtime, int timeLeft) {
        return ((dot.calculateDotDamage(timeLeft) / timeLeft)) * downtime;
    }

    //TODO move getNextDotTimeOut to Target.java
    private DamageOverTime getNextDotTimeOut(Target target) {
        DamageOverTime nextDot = null;
        for (Object dot : target.getObservers()) {
            if (dot instanceof DamageOverTime && !(dot instanceof Channeling)) {
                if (nextDot == null) {
                    nextDot = (DamageOverTime) dot;
                } else {
                    if (nextDot.getDuration() > ((DamageOverTime) dot).getDuration()) {
                        nextDot = (DamageOverTime) dot;
                    }
                }
            }
        }
        return nextDot;
    }
}























