package se.wowsim.classes;

import java.util.HashMap;
import java.util.Map;

import se.wowsim.Target;
import se.wowsim.spells.DamageOverTime;
import se.wowsim.spells.DirectDamage;
import se.wowsim.spells.Spell;

public abstract class ClassTemplate {

	double totalDamageDone;
	double intellect;
	double stamina;
	double spirit;

	protected Map<String, Spell> spells;
    protected boolean busyCasting;
    protected int castProgress;
    protected int globalCooldown;
    protected int downTime;
    protected double critMulti;
    protected Spell nextSpell;
    protected Classes myClass;
    
	protected ClassTemplate() {
        this.busyCasting = false;
        this.globalCooldown = 15;
        this.totalDamageDone = 0.0;
        this.nextSpell = null;
        this.spells = new HashMap<>();
        this.critMulti = 1.5;
	}

	public double getTotalDamageDone() { return totalDamageDone; }

	public double resetTotalDamageDone(){
		this.totalDamageDone = 0.0;
		return 0.0;
	}

	public Map<String, Spell> getSpells() {
		return spells;
	}
	
	protected void addSpell(String name, Spell spell) {	spells.put(name, spell); }
	
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
                if (target.notAffected((DamageOverTime) currentSpell) && currentSpell.getCastTime() >= globalCooldown) {
                    result.put(currentSpell, (((DamageOverTime) currentSpell).calculateDotDamage(timeLeft) / currentSpell.getCastTime()));
                } else if (target.notAffected((DamageOverTime) currentSpell)) {
                    result.put(currentSpell, (((DamageOverTime) currentSpell).calculateDotDamage(timeLeft) / globalCooldown));
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
}
