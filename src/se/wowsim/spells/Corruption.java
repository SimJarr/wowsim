package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public final class Corruption extends DamageOverTime implements Observer {

	private int duration;
	private int maxDuration;
	private int tickInterval;
	private int totalDamage;
	private int tickNumber;
	private int totalTickNumber;
	
	public Corruption(Subject s) {
		s.register(this);
		this.duration = 180;
		this.maxDuration = 180;
		this.tickInterval = 30;
		this.totalDamage = 222;
		this.tickNumber = 1;
		this.totalTickNumber = duration/tickInterval;
	}
	
	@Override
	public void update() {
		this.duration --;
		if((duration % tickInterval == 0) && duration != maxDuration) {
			System.out.println("Corruption tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber + " damage");
			tickNumber++;
		}
	}
	
	@Override
	public String getName() {
		return "Corruption";
	}
}
