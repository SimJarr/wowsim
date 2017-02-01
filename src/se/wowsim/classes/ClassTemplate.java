package se.wowsim.classes;

import se.wowsim.Target;
import se.wowsim.spells.CurseOfAgony;
import se.wowsim.spells.types.Channeling;
import se.wowsim.spells.types.DamageOverTime;
import se.wowsim.spells.types.DirectDamage;
import se.wowsim.spells.types.Spell;

import static se.wowsim.classes.GeneralRules.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ClassTemplate {

    private double totalDamageDone;
    private int level;
    private int intellect;
    private int stamina;
    private int spirit;
    private List<String> usedSpells = new ArrayList<>();
    protected Map<String, Spell> spells;
    protected boolean busyCasting;
    protected double critChance;
    protected int castProgress;
    protected int globalCooldown;
    protected int downTime;
    protected Spell nextSpell;
    protected Classes myClass;

    protected ClassTemplate(int level, int intellect) {
        this.busyCasting = false;
        this.globalCooldown = GLOBAL_COOLDOWN;
        this.totalDamageDone = 0.0;
        this.nextSpell = null;
        this.spells = new HashMap<>();
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

    private Spell determineSpell(Target target, int timeLeft) {

        //TODO future-sight, understand what order of spells yields the greatest ePeen

        DamageOverTime highestDamageDot = calculateHighestDamageDot(target, timeLeft);

        Map<Spell, Double> spellCandidates = insertSpellsWithValues(target, timeLeft, highestDamageDot);

        return selectSpell(target, timeLeft, spellCandidates);
    }

    private DamageOverTime calculateHighestDamageDot(Target target, int timeLeft) {

        DamageOverTime highestDamageDot = null;
        double highestDamageDotDps = 0.0;
        DamageOverTime secondHighestDamageDot = null;

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DamageOverTime && !(currentSpell instanceof Channeling)) {
                if (highestDamageDot == null && (currentSpell.calculateDamageDealt(target, timeLeft) != 0.0)) {
                    highestDamageDot = (DamageOverTime) currentSpell;
                    highestDamageDotDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                } else {
                    double currentSpellDps = ((currentSpell.calculateDamageDealt(target, timeLeft)) / (((DamageOverTime) currentSpell).getDuration() + currentSpell.getCastTime())) * 10;
                    if (currentSpellDps > highestDamageDotDps) {
                        secondHighestDamageDot = highestDamageDot;
                        highestDamageDot = (DamageOverTime) currentSpell;
                        highestDamageDotDps = currentSpellDps;
                    }
                }
            }
        }

        if (highestDamageDot != null && secondHighestDamageDot != null) {
            if (highestDamageDot.getTimeTakenFromCaster() + secondHighestDamageDot.getCastTime() + secondHighestDamageDot.getMaxDuration() > timeLeft) {
                if (secondHighestDamageDot.getTimeTakenFromCaster() + highestDamageDot.getCastTime() + highestDamageDot.getMaxDuration() <= timeLeft) {
                    return secondHighestDamageDot;
                }
            }
        }

        return highestDamageDot;
    }

    private Map<Spell, Double> insertSpellsWithValues(Target target, int timeLeft, DamageOverTime highestDamageDot){

        Map<Spell, Double> result = new HashMap<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DirectDamage) {
                ((DirectDamage) currentSpell).setCritChance(this.myClass.calculateCritChance(level, intellect));
            }

            if (currentSpell instanceof DirectDamage || currentSpell instanceof Channeling || currentSpell == highestDamageDot) {

                result.put(currentSpell, (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());
                //System.out.println(currentSpell.getName() + " value: " + (currentSpell.calculateDamageDealt(target, timeLeft)) / currentSpell.getTimeTakenFromCaster());
            }

        }

        return result;
    }

    private Spell selectSpell(Target target, int timeLeft, Map<Spell, Double> candidates){

        Spell selectedSpell = null;
        double highestSoFar = 0.0;

        for (Map.Entry<Spell, Double> entry : candidates.entrySet()) {

            if (selectedSpell == null) {
                selectedSpell = entry.getKey();
                highestSoFar = entry.getValue();
            } else {
                if (entry.getValue() > highestSoFar) {
                    selectedSpell = entry.getKey();
                    highestSoFar = entry.getValue();
                }
            }
        }

        if (selectedSpell != null && highestSoFar == 0.0 || (worthDoingNothing(target, selectedSpell, timeLeft))) {
            selectedSpell = null;
        }

        if (selectedSpell != null) {
            totalDamageDone += highestSoFar * selectedSpell.getTimeTakenFromCaster();
            usedSpells.add(selectedSpell.getName() + " " + (int) (highestSoFar * selectedSpell.getTimeTakenFromCaster()));
        }

        return selectedSpell;
    }

    private boolean worthDoingNothing(Target target, Spell nextCalculatedSpell, int timeLeft) {

        DamageOverTime dot = target.getNextDotTimeOut();

        if (dot != null && nextCalculatedSpell != null && dot != nextCalculatedSpell) {


            int timeSpentOnNextSpell = (nextCalculatedSpell.getCastTime() < globalCooldown) ? globalCooldown : nextCalculatedSpell.getCastTime();
            int dotDurationIfWeCastNext = dot.getDuration() - timeSpentOnNextSpell;

            if (dotDurationIfWeCastNext < dot.getCastTime()) {
                int timeTheDotWouldBeGone = dot.getCastTime() - dotDurationIfWeCastNext;

                int timeTheDotCouldBeUp = (dot.getMaxDuration() < timeLeft) ? dot.getMaxDuration() : timeLeft;
                double damageDotWouldHaveDone = (dot.calculateDotDamage(timeLeft) / timeTheDotCouldBeUp) * timeTheDotWouldBeGone;
                damageDotWouldHaveDone = (damageDotWouldHaveDone < 0) ? 0 : damageDotWouldHaveDone;

                int timeTheNextSpellWouldMiss = (dot.getDuration() - dot.getCastTime());
                double damageNextSpellWouldHaveDone = ((nextCalculatedSpell.calculateDamageDealt(target, timeLeft)) / timeSpentOnNextSpell) * timeTheNextSpellWouldMiss;
                damageNextSpellWouldHaveDone = (damageNextSpellWouldHaveDone < 0) ? 0 : damageNextSpellWouldHaveDone;

                if (false) {
                    System.out.println(dot.getName() + " har lägst tid: " + dot.getDuration() + " decisec");
                    System.out.println("om vi castar: " + nextCalculatedSpell.getName() + " så har vi bara: " + dotDurationIfWeCastNext + " decisec");
                    System.out.println("då skulle " + dot.getName() + " vara nere i: " + timeTheDotWouldBeGone + " decisec");
                    System.out.println("på " + timeTheDotWouldBeGone + " decisec skulle " + dot.getName() + " hinna göra: " + damageDotWouldHaveDone + " damage");
                    System.out.println("på " + timeTheNextSpellWouldMiss + " decisec skulle " + nextCalculatedSpell.getName() + " hinna göra: " + damageNextSpellWouldHaveDone + " damage");
                }

                if ((damageDotWouldHaveDone > damageNextSpellWouldHaveDone) && timeTheNextSpellWouldMiss > 0) {
                    System.out.println("HAAAAAAAAAAAAAAALTAR i: " + (dot.getDuration() - dot.getCastTime()) + " decisec");
                    return true;
                }

            }

        }
        return false;
    }
}























