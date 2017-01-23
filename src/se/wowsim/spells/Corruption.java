package se.wowsim.spells;

import se.wowsim.Observer;
import se.wowsim.Subject;

public final class Corruption extends DamageOverTime implements Observer {

	private int tickNumber;
	private int totalTickNumber;
	private Subject subject;
	
	public Corruption(int rank) {
		init(rank);
	}
	
	public Corruption(Subject s, int rank) {
		this.subject = s;
		s.register(this);
		init(rank);
	}
	
	@Override
	public void update() {
		this.duration --;
		if((duration % tickInterval == 0) && duration != maxDuration) {
			System.out.println("Corruption tick(" + tickNumber + "/" + totalTickNumber + "): " + totalDamage / totalTickNumber + " damage");
			tickNumber++;
		}
		if(duration == 0) { subject.unregister(this); }
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
		this.castTime = 2;
	}
}
