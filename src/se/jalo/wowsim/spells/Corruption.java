package se.jalo.wowsim.spells;

import se.jalo.wowsim.Observer;
import se.jalo.wowsim.Subject;

public final class Corruption extends Spell implements Observer {

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
		if((duration % tickInterval == 0) && duration != maxDuration) {
			System.out.println("Corruption tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber);
			tickNumber++;
		}
		this.duration --;
	}
	
	@Override
	public String toString() {
		return "Corruption";
	}
}
