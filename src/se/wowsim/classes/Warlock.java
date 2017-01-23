package se.wowsim.classes;

import java.util.HashMap;
import se.wowsim.Observer;
import se.wowsim.Target;
import se.wowsim.spells.Corruption;
import se.wowsim.spells.DamageOverTime;
import se.wowsim.spells.Shadowbolt;

public final class Warlock extends Class {
	
	private boolean busyCasting;
	
	public Warlock(int level) {
		this.busyCasting = false;
		this.spells = new HashMap<>();
		
		addSpell("Corruption", new Corruption(3));
		addSpell("Shadowbolt", new Shadowbolt(5));
	}

	@Override
	public void determineSpell(Target target, double timeLeft) {
		
		if(castDot(getCorruption(), target, timeLeft)) { new Corruption(target, 3); }
		else if(timeLeft > getShadowbolt().getCastTime()) { new Shadowbolt(target, 5); }
		
	}
	
	private boolean castDot(DamageOverTime dot, Target target, double timeLeft) {
		if(alreadyAffected(target, dot)) { return false; }
		if(timeLeft - dot.getCastTime() >= dot.getMaxDuration()) { return true; }
		if(getDotDamage(timeLeft, dot) > getShadowbolt().getTotalDamage()) { return true; }
		return false;
	}
	
	private int getDotDamage(double timeLeft, DamageOverTime dot) {
		double timeAfterCast = timeLeft - dot.getCastTime();
		int damage = 0;
		for(int i = 1; i < (int)timeAfterCast; i ++) {
			if(i % dot.getTickInterval() == 0) {
				damage += dot.getTotalDamage() / (dot.getMaxDuration()/dot.getTickInterval());
			}
		}
		return damage;
	}
	
	private boolean alreadyAffected(Target target, DamageOverTime dot) {
		for(Observer o : target.getObservers()) {
			if(o.getClass().equals(dot.getClass())) {
				return true;
			}
		}
		return false;
	}
	
	private Shadowbolt getShadowbolt() {
		return ((Shadowbolt)spells.get("Shadowbolt"));
	}
	
	private Corruption getCorruption() {
		return ((Corruption)spells.get("Corruption"));
	}
}
