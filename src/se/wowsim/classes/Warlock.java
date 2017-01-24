package se.wowsim.classes;

import java.util.HashMap;
import java.util.Map;

import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.spells.*;

public final class Warlock extends ClassTemplate {

    private boolean busyCasting;
    private int castProgress;
    private int globalCooldown;
    private int downTime;
    private Spell nextSpell;

    public Warlock() {
        this.busyCasting = false;
        this.globalCooldown = 15;
        this.totalDamageDone = 0.0;
        this.nextSpell = null;
        this.spells = new HashMap<>();
    }

    @Override
    public void currentActivity(Target target, int timeLeft) {
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
            if (castProgress == 0) {
                nextSpell.applySpell();
                nextSpell = null;
            }
        }
    }

    private Spell determineSpell(Target target, int timeLeft) {

        Map<Spell, Double> result = new HashMap<>();

        for (Map.Entry<String, Spell> entry : spells.entrySet()) {

            Spell currentSpell = entry.getValue();

            if (currentSpell instanceof DamageOverTime) {
                if (notAffected(target, (DamageOverTime) currentSpell) && currentSpell.getCastTime() >= globalCooldown) {
                    result.put(currentSpell, (((DamageOverTime) currentSpell).getDotDamage(timeLeft) / currentSpell.getCastTime()));
                } else if (notAffected(target, (DamageOverTime) currentSpell)) {
                    result.put(currentSpell, (((DamageOverTime) currentSpell).getDotDamage(timeLeft) / globalCooldown));
                }
            } else if (currentSpell instanceof DirectDamage) {
                if (currentSpell.getCastTime() >= globalCooldown){
                    result.put(currentSpell, (((DirectDamage) currentSpell).getTotalDamage()) / currentSpell.getCastTime());
                } else {
                    result.put(currentSpell, (((DirectDamage) currentSpell).getTotalDamage()) / globalCooldown);
                }
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

        if (determinedSpell != null){

            if (determinedSpell.getCastTime() < globalCooldown) {
                totalDamageDone += highestSoFar * globalCooldown;
            } else {
                totalDamageDone += highestSoFar * determinedSpell.getCastTime();
            }
        }

        return determinedSpell;
    }

    private boolean notAffected(Target target, DamageOverTime dot) {
        for (Observer o : target.getObservers()) {
            if (o.getClass().equals(dot.getClass())) {
            	return(((DamageOverTime)o).getDuration() < dot.getCastTime());
            }
        }
        return true;
    }
}
