package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Target;

public final class Corruption extends DamageOverTime implements Observer {

	private int tickNumber;
	private int totalTickNumber;
	private Target target;
	
	public Corruption(int rank) {
		init(rank);
	}
	
	public Corruption(Target t, int rank) {
		this.target = t;
		init(rank);
	}
	
	@Override
	public void applySpell() {
		target.register(this);
	}

	@Override
	public void setTarget(Target target) {
		this.target = target;
	}

	@Override
	public void update() {
		this.duration --;
		if((duration % tickInterval == 0) && duration != maxDuration) {
			System.out.println("Corruption tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber + " damage");
			tickNumber++;
		}
		if(duration == 0) { target.unregister(this); }
	}
	
	@Override
	public String getName() {
		return "Corruption";
	}
	
	private void init(int rank) {
		switch(rank) {
		case 3:
			this.duration = 180;
			this.maxDuration = 180;
			this.totalDamage = 222;
			break;
		default:
			break;
		}
		this.tickInterval = 30;
		this.tickNumber = 1;
		this.totalTickNumber = duration/tickInterval;
		this.castTime = 20;
	}
}
