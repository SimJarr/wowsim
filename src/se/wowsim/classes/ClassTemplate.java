package se.wowsim.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import se.wowsim.Target;
import se.wowsim.spells.DamageOverTime;
import se.wowsim.spells.DirectDamage;
import se.wowsim.spells.Spell;

public abstract class ClassTemplate {

    private double totalDamageDone;
    private int level;
    private double intellect;
    private double stamina;
    private double spirit;
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

        if (nextSpell != null && castProgress <= 0) {
            nextSpell.applySpell();
            nextSpell = null;
        }
        if (downTime == 0 && castProgress <= 0) {
            busyCasting = false;
        }
        if (busyCasting) {
            return;
        }

        nextSpell = determineSpell(target, timeLeft);

        if (nextSpell != null) {
            nextSpell.setTarget(target);

            if (nextSpell.getCastTime() > globalCooldown) {
                castProgress = nextSpell.getCastTime();
                downTime = nextSpell.getCastTime();
            } else {
                castProgress = nextSpell.getCastTime();
                downTime = globalCooldown;
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

    private DamageOverTime calculateHighestDamageDot(Target target, int timeLeft) {

        DamageOverTime highestDamageDot = null;
        double highestDamageDotDps = 0.0;

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DamageOverTime) {
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

        DamageOverTime highestDamageDot = calculateHighestDamageDot(target, timeLeft);

        Map<Spell, Double> result = new HashMap<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DirectDamage || currentSpell == highestDamageDot) {
                int timeTakenFromCaster = (currentSpell.getCastTime() <= 15) ? 15 : currentSpell.getCastTime();

                result.put(currentSpell, (currentSpell.calculateDamageDealt(target, timeLeft)) / timeTakenFromCaster);
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

        if (determinedSpell != null && determinedSpell.getCastTime() > timeLeft) {
            determinedSpell = null;
        }

        if (determinedSpell != null) {

            if (determinedSpell.getCastTime() < globalCooldown) {
                totalDamageDone += highestSoFar * globalCooldown;
            } else {
                totalDamageDone += highestSoFar * determinedSpell.getCastTime();
            }
        }

        return determinedSpell;
    }
}
